package job.data.holder;

import json.KnownHistoryFileContent;

import java.util.ArrayList;
import java.util.List;

public class NeighborGroup {
    private List<String> allowedGroupNeighbor = new ArrayList<>();
    private KnownHistoryFileContent fileContent;

    public NeighborGroup(KnownHistoryFileContent fileContent)
    {
        this.fileContent = fileContent;
        this.allowedGroupNeighbor.add("delay5");
        this.allowedGroupNeighbor.add("delay30");
        this.allowedGroupNeighbor.add("delay60");
        this.allowedGroupNeighbor.add("delay90");
        this.allowedGroupNeighbor.add("delay_more");
        this.allowedGroupNeighbor.add("overdue_days");
        this.allowedGroupNeighbor.add("count");
        this.allowedGroupNeighbor.add("loan_amount");
        this.allowedGroupNeighbor.add("n_prolongations");
        this.allowedGroupNeighbor.add("unused_limit");
        this.allowedGroupNeighbor.add("current_debt");
        this.allowedGroupNeighbor.add("overdue_sum");
        this.allowedGroupNeighbor.add("total_amount_paid");

        this.allowedGroupNeighbor.add("client_age");
        this.allowedGroupNeighbor.add("children_count");
        this.allowedGroupNeighbor.add("term");
        this.allowedGroupNeighbor.add("income_per_month");
        this.allowedGroupNeighbor.add("amount");
    }
}
