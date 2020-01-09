package job;

import concurrency.*;
import db.maria.*;
import db.value.object.FinalResultEntity;
import db.value.object.LoanEntity;
import job.data.holder.RiskDataHolder;
import staticAccess.FileSystem;
import staticAccess.Helper;
import staticAccess.Settings;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Calculate {
    public static void main(String[] args) {
        int minTerm = Integer.parseInt(args[1]);
        FinalResultEntity.pointsRequired = -Integer.parseInt(args[0]);

        try {
            int maxIterations = 1;
            List<RiskDataHolder> knownHistoryDataList = getRiskDataHolders();

            if (ConnectionStorage.isTrainDatabase()) {
                maxIterations = 15;
            }
            for (int i=1; i<maxIterations +1; i++) {
                Settings.trainNumber = i;
                doCalculationIteration(knownHistoryDataList, minTerm);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void doCalculationIteration(List<RiskDataHolder> knownHistoryDataList, int minTerm) throws SQLException, ExecutionException, InterruptedException {
        LoanTable loanSelector = new LoanTable();
//        List<LoanEntity> data = loanSelector.getTableDataForTerm();
        List<LoanEntity> data = loanSelector.getTableDataForTerm(minTerm);

        DataSplitter splitData = new DataSplitter(data);

        gatherRequiredData(splitData);
        List<ResultRisk> riskResults = calculateResultData(splitData, knownHistoryDataList);

        if (ConnectionStorage.isTrainDatabase()) {
            var pointAdjustmentTable = new PointAdjustmentTable();
//            pointAdjustmentTable.addAdjustmentData(riskResults);
        }

        List<FinalResultEntity> finalResultEntityList = new ArrayList<>();
        HashMap<Integer, FinalResultEntity> mappedResultEntity = new HashMap<>();
        for (ResultRisk resultRisk : riskResults) {
            Integer loanId = resultRisk.getLoan().getId();
            FinalResultEntity entity = mappedResultEntity.get(loanId);
            if (entity == null) {
                entity = new FinalResultEntity(loanId, resultRisk.getPoints());
                finalResultEntityList.add(entity);
                mappedResultEntity.put(loanId, entity);

                continue;
            }

            entity.addPoints(resultRisk.getPoints());
        }

        System.out.println("done entity creation");

        FinalResultTable resultTable = new FinalResultTable();

        resultTable.saveResults(finalResultEntityList);
        if (ConnectionStorage.isTrainDatabase()) {
            resultTable.displayFinalResults();
        }
        System.out.println(String.format("done s(%d) f(%d)", Helper.successFound, Helper.failedFound));
    }

    private static void gatherRequiredData(DataSplitter splitData) throws InterruptedException, java.util.concurrent.ExecutionException {
        List<Future<List<LoanEntity>>> futures = new ArrayList<>();
        ExecutorService executorService = Executors.newFixedThreadPool(Settings.getMaxThreads());

        for (List<LoanEntity> loanList : splitData.getThreadDataCollection()) {
            futures.add(executorService.submit(new HistoryGatherTask(loanList)));
        }

        for (Future<List<LoanEntity>> future : futures) {
            future.get();
        }

        executorService.shutdown();
        System.out.println("done required data gathering");
    }

    private static List<RiskDataHolder> getRiskDataHolders() throws ExecutionException, InterruptedException, SQLException {
        List<Future<RiskDataHolder>> futures = new ArrayList<>();
        ExecutorService executorService = Executors.newFixedThreadPool(
                Settings.getMaxThreads()
        );

        List<File> knownHistoryFiles = FileSystem.getAllDirectoryFiles(String.format("/work/java/metadata/history%d/", Settings.trainNumber));

        List<RiskDataHolder> knownHistoryDataList = new ArrayList<>();
        var existingFilesTable = new ExistingFilesTable();
//        List<String> ignoreFileList =  existingFilesTable.ignorableFiles();

        for (File historyFile : knownHistoryFiles) {
//            if (ignoreFileList.indexOf(historyFile.getAbsolutePath().intern()) >= 0) {
//                continue;
//            }

            futures.add(executorService.submit(new ReadJsonDataTask(historyFile)));
        }

        for (Future<RiskDataHolder> future : futures) {
            knownHistoryDataList.add(future.get());
        }

        executorService.shutdown();

        existingFilesTable.updateCreateFileEntity(knownHistoryDataList);

        System.out.println(String.format("received  %d history files", knownHistoryDataList.size()));

        return knownHistoryDataList;
    }

    private static List<ResultRisk> calculateResultData(DataSplitter splitData, List<RiskDataHolder> knownHistoryDataList) throws ExecutionException, InterruptedException {
        List<Future<List<ResultRisk>>> futures = new ArrayList<>();
        ExecutorService executorService = Executors.newFixedThreadPool(
                Settings.getMaxThreads()
        );
        RiskCalculator riskCalculator = new RiskCalculator(knownHistoryDataList);

        for (List<LoanEntity> loanList : splitData.getThreadDataCollection()) {
            futures.add(executorService.submit(new CalculateHistoryRiskTask(loanList, riskCalculator)));
        }

        List<ResultRisk> result = new ArrayList<>();
        for (Future<List<ResultRisk>> future : futures) {
            result.addAll(future.get());
        }

        executorService.shutdown();
        System.out.println("done required data calculation");
        return result;
    }
}
