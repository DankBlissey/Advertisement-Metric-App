package uk.ac.soton.adDashboard.records;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import javafx.util.Pair;

public class DataSet {

  private final ArrayList<Click> clicks;
  private final ArrayList<Impression> impressions;
  private final ArrayList<ServerAccess> serverAccess;
  private final HashMap<Long, User> users;

  private boolean pagesViewedBounceMetric = true;
  private int pagesForBounce = 1;

  private int interval = 1;

  public DataSet(ArrayList<Click> clicks,
      ArrayList<Impression> impressions,
      ArrayList<ServerAccess> serverAccesses, HashMap<Long, User> users) {
    this.clicks = clicks;
    this.impressions = impressions;
    this.serverAccess = serverAccesses;
    this.users = users;

  }

  public ArrayList<Click> getClicks() {
    return clicks;
  }

  public ArrayList<Impression> getImpressions() {
    return impressions;
  }

  public ArrayList<ServerAccess> getServerAccess() {
    return serverAccess;
  }

  public HashMap<Long, User> getUsers() {
    return users;
  }

  public boolean isPagesViewedBounceMetric() {
    return pagesViewedBounceMetric;
  }

  public int getPagesForBounce() {
    return pagesForBounce;
  }

  public int getInterval() {
    return interval;
  }

  public boolean matchesFilters(User user) {
    return true;
  }

  public int calcTotalCost(LocalDateTime granularStart, LocalDateTime granularEnd) {
    int totalCost = 0;
    for (Impression impression : impressions) {
      if (matchesFilters(users.get(impression.getId()))) { //if user legal
        if (impression.getDate().compareTo(granularStart) >= 0
            && impression.getDate().compareTo(granularEnd) <= 0) {
          totalCost += impression.getCost();
        }
      }
    }
    for (Click click : clicks) {
      //if user legal
      if (matchesFilters(users.get(click.getId()))) {
        if (click.getDate().compareTo(granularStart) >= 0
            && click.getDate().compareTo(granularEnd) <= 0) {
          totalCost += click.getCost();
        }
      }
    }
    return totalCost;
  }


  public int totalClicks(LocalDateTime granularStart, LocalDateTime granularEnd) {
    int count = 0;
    for (Click click : clicks) {
      if (matchesFilters(users.get(click.getId()))) { //if user legal
        if (click.getDate().compareTo(granularStart) >= 0
            && click.getDate().compareTo(granularEnd) <= 0) {
          count += 1;
        }
      }
    }
    return count;
  }

  public int totalImpressions(LocalDateTime granularStart, LocalDateTime granularEnd) {
    int count = 0;
    for (Impression impression : impressions) {
      if (matchesFilters(users.get(impression.getId()))) { //if user legal
        if (impression.getDate().compareTo(granularStart) >= 0
            && impression.getDate().compareTo(granularEnd) <= 0) {
          count += 1;
        }
      }
    }
    return count;
  }

  public int calcClickThrough(LocalDateTime granularStart, LocalDateTime granularEnd) {
    int imp = totalImpressions(granularStart, granularEnd);
    int clicks = totalClicks(granularStart, granularEnd);
    return clicks / imp;
  }

  public int calcNumConversions(LocalDateTime granularStart, LocalDateTime granularEnd) {
    int conversions = 0;
    for (ServerAccess access : serverAccess) {
      if (access.isConversion() && matchesFilters(users.get(access.getId()))) {
        if (access.getStartDate().compareTo(granularStart) >= 0
            && access.getStartDate().compareTo(granularEnd) <= 0) {
          conversions += 1;
        }
      }
    }
    return conversions;
  }

  public boolean isBounce(ServerAccess access) {
    if (isPagesViewedBounceMetric()) {
      if (access.getPagesViewed() < getPagesForBounce()) {
        return false;

      } else {
        if (ChronoUnit.SECONDS.between(access.getStartDate(), access.getEndDate())
            < getInterval()) {
          return false;
        }
      }

    }
    return true;
  }

  public int calcBounces(LocalDateTime granularStart, LocalDateTime granularEnd) {
    int bounces = 0;
    for (ServerAccess access : serverAccess) {
      if (isBounce(access) && matchesFilters(users.get(access.getId()))) {
        if (access.getStartDate().compareTo(granularStart) >= 0
            && access.getStartDate().compareTo(granularEnd) <= 0) {
          bounces += 1;
        }
      }
    }
    return bounces;
  }

  public int calcBounceRate(LocalDateTime granularStart, LocalDateTime granularEnd) {
    int bounces = calcBounces(granularStart, granularEnd);
    int clicks = totalClicks(granularStart, granularEnd);
    return bounces / clicks;
  }

  public int calcCostAcquisition(LocalDateTime granularStart, LocalDateTime granularEnd) {
    int cost = calcTotalCost(granularStart, granularEnd);
    int conversions = calcNumConversions(granularStart, granularEnd);
    return cost / conversions;
  }

  public int calcCostPerClick(LocalDateTime granularStart, LocalDateTime granularEnd) {
    int cost = calcTotalCost(granularStart, granularEnd);
    int clicks = totalClicks(granularStart, granularEnd);
    return cost / clicks;
  }

  public int calcUniqueUsersClick(LocalDateTime granularStart, LocalDateTime granularEnd) {
    HashSet<Long> visited = new HashSet<>();
    int uniques = 0;
    for (Click click : clicks) {
      if (matchesFilters(users.get(click.getId()))) { //if user legal
        if (click.getDate().compareTo(granularStart) >= 0
            && click.getDate().compareTo(granularEnd) <= 0) {
          if (!visited.contains(click.getId())) {
            uniques += 1;
            visited.add(click.getId());
          }
        }
      }
    }
    return uniques;
  }

  public int costPerThousandImpre(LocalDateTime granularStart, LocalDateTime granularEnd) {
    int cost = calcTotalCost(granularStart, granularEnd);
    int impr = totalImpressions(granularStart, granularEnd);
    return cost / impr;
  }

//  public ArrayList<Pair<Integer, Integer>> makePlotPoints(LocalDateTime startTime,
//      LocalDateTime end) {
//    for (int day = 0; day <; day++) {
//
//    }
//  }
}
