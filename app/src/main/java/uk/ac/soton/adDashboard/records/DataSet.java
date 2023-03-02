package uk.ac.soton.adDashboard.records;

import java.time.LocalDateTime;
import java.util.HashMap;
import javafx.util.Pair;

public class DataSet {

  private final HashMap<Pair<Long, LocalDateTime>, Click> clicks;
  private final HashMap<Pair<Long, LocalDateTime>, Impression> impressions;
  private final HashMap<Pair<Long, LocalDateTime>, ServerAccess> serverAccess;

  public DataSet(HashMap<Pair<Long, LocalDateTime>, Click> clicks,
      HashMap<Pair<Long, LocalDateTime>, Impression> impressions,
      HashMap<Pair<Long, LocalDateTime>, ServerAccess> serverAccesses) {
    this.clicks = clicks;
    this.impressions = impressions;
    this.serverAccess = serverAccesses;

  }

  public HashMap<Pair<Long, LocalDateTime>, Click> getClicks() {
    return clicks;
  }

  public HashMap<Pair<Long, LocalDateTime>, Impression> getImpressions() {
    return impressions;
  }

  public HashMap<Pair<Long, LocalDateTime>, ServerAccess> getServerAccess() {
    return serverAccess;
  }

  /**
   * Sums impression costs and click costs.
   *
   * @param granularStart
   * @param granularEnd
   * @return
   */
  public int calcTotalCost(LocalDateTime granularStart, int granularEnd) {
    int totalCost = 0;
    for (Pair<Long, LocalDateTime> aLong : impressions.keySet()) {
      if (aLong.getValue().compareTo(granularStart) >= 0) {
        //need to check if impression matches type
        totalCost += impressions.get(aLong).getCost();
//        if (clicks.get())
      }
    }
    return totalCost;
  }

//+calcClickThrough (granularStart,granularEnd)
//+calcCostAcquisition (granularStart,granularEnd)
//+calcBounceRate (granularStart,granularEnd)
//+filterItems/getFilteredIDs (granularStart,granularEnd)
//+calcNumBounces (granularStart,granularEnd)
//+calcNumConversions (granularStart,granularEnd)
//+calcTotalImpressions (granularStart,granularEnd)
//+calcNumClicks (granularStart,granularEnd)
//+calcCostPerClick (granularStart,granularEnd)
//+calcUniqueUsersClick (granularStart,granularEnd)
}
