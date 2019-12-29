package job;

import concurrency.CalculateHistoryRiskTask;
import concurrency.DataSplitter;
import concurrency.HistoryGatherTask;
import concurrency.ResultRisk;
import db.maria.FinalResultTable;
import db.maria.LoanTable;
import db.value.object.FinalResultEntity;
import db.value.object.LoanEntity;
import job.data.holder.RiskDataHolder;
import json.Parser;
import staticAccess.FileSystem;
import staticAccess.Helper;
import staticAccess.Settings;

import java.io.File;
import java.io.IOException;
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
        FinalResultEntity.pointsRequired = Integer.parseInt(args[0]);

        LoanTable loanSelector = new LoanTable();

        try {
            List<LoanEntity> data = loanSelector.getTableDataForTerm(minTerm);

            DataSplitter splitData = new DataSplitter(data);

            List<ResultRisk> riskResults = calculateResultData(splitData, gatherRequiredData( splitData));

            List<FinalResultEntity> finalResultEntityList = new ArrayList<>();
            HashMap<Integer, FinalResultEntity> mappedResultEntity = new HashMap<>();
            for (ResultRisk resultRisk: riskResults) {
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
            System.out.println(String.format("done s(%d) f(%d)", Helper.successFound, Helper.failedFound));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static List<RiskDataHolder> gatherRequiredData(DataSplitter splitData) throws IOException, InterruptedException, java.util.concurrent.ExecutionException {
        List<Future<List<LoanEntity>>> futures = new ArrayList<>();
        ExecutorService executorService = Executors.newFixedThreadPool(Settings.getMaxThreads());

        for (List<LoanEntity> loanList: splitData.getThreadDataCollection()) {
            futures.add(executorService.submit(new HistoryGatherTask(loanList)));
        }

        List<File> knownHistoryFiles = FileSystem.getAllDirectoryFiles("/work/java/metadata/history");
        Parser knownHistoryParser = new Parser();
        List<RiskDataHolder> knownHistoryDataList = new ArrayList<>();

        for (File historyFile: knownHistoryFiles) {
            knownHistoryDataList.add(new RiskDataHolder(knownHistoryParser.readFile(historyFile)));
        }

        for (Future<List<LoanEntity>> future: futures) {
            future.get();
        }


        executorService.shutdown();
        System.out.println("done required data gathering");
        return knownHistoryDataList;
    }

    private static List<ResultRisk> calculateResultData(DataSplitter splitData, List<RiskDataHolder> knownHistoryDataList) throws ExecutionException, InterruptedException {
        List<Future<List<ResultRisk>>> futures = new ArrayList<>();
        ExecutorService executorService = Executors.newFixedThreadPool(
                Settings.getMaxThreads()
        );
        RiskCalculator riskCalculator = new RiskCalculator(knownHistoryDataList);

        for (List<LoanEntity> loanList: splitData.getThreadDataCollection()) {
            futures.add(executorService.submit(new CalculateHistoryRiskTask(loanList, riskCalculator)));
        }

        List<ResultRisk> result = new ArrayList<>();
        for (Future<List<ResultRisk>> future: futures) {
            result.addAll(future.get());
        }

        executorService.shutdown();
        System.out.println("done required data calculation");
        return result;
    }
}
