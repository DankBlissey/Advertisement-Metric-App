package uk.ac.soton.adDashboard.records;

import java.util.HashMap;

public class DataSet {

  private final HashMap<Long, Click> clicks;
  private final HashMap<Long, Impression> impressions;
  private final HashMap<Long, ServerAccess> serverAccess;

  public DataSet(HashMap<Long, Click> clicks, HashMap<Long, Impression> impressions,
      HashMap<Long, ServerAccess> serverAccesses) {
    this.clicks = clicks;
    this.impressions = impressions;
    this.serverAccess = serverAccesses;
  }

  public HashMap<Long, Click> getClicks() {
    return clicks;
  }

  public HashMap<Long, Impression> getImpressions() {
    return impressions;
  }

  public HashMap<Long, ServerAccess> getServerAccess() {
    return serverAccess;
  }


}
