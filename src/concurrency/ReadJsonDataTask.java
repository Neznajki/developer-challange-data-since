package concurrency;

import job.data.holder.RiskDataHolder;
import json.Parser;

import java.io.File;
import java.util.concurrent.Callable;

public class ReadJsonDataTask implements Callable<RiskDataHolder> {
    private static Parser knownHistoryParser = new Parser();
    private File historyFile;

    public ReadJsonDataTask(File historyFile) {

        this.historyFile = historyFile;
    }

    @Override
    public RiskDataHolder call() throws Exception {
        return new RiskDataHolder(historyFile.getAbsolutePath(), knownHistoryParser.readFile(historyFile));
    }
}
