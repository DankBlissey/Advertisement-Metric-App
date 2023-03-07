package uk.ac.soton.adDashboard;

import java.util.ArrayList;
import javax.xml.crypto.Data;
import org.junit.jupiter.api.Test;
import uk.ac.soton.adDashboard.records.Click;
import uk.ac.soton.adDashboard.records.DataSet;
import uk.ac.soton.adDashboard.records.Impression;
import uk.ac.soton.adDashboard.records.ServerAccess;
import uk.ac.soton.adDashboard.records.User;

public class DataSetTest {

  DataSet dataSet = new DataSet();

  @Test
  void impressionCost() throws Exception {
    dataSet.setImpressions(generateImpressions());
  }


  ArrayList<Impression> generateImpressions() throws Exception {
    var impressions = new ArrayList<Impression>();
    impressions.add(new Impression("2015-01-01 23:00:02", "25", "12.5", "Shopping"));
    impressions.add(new Impression("2015-02-01 23:00:02", "26", "10", "Shopping"));
    impressions.add(new Impression("2015-02-03 23:00:02", "27", "15", "Blog"));
    return impressions;
  }

  ArrayList<User> generateUser() throws Exception {
    var users = new ArrayList<User>();
    users.add(new User("25", "25", "Male", "Medium"));
    users.add(new User("26", "25", "Female", "High"));
    return users;
  }

  ArrayList<ServerAccess> generateServerAccess() throws Exception {
    var access = new ArrayList<ServerAccess>();
    access.add(new ServerAccess("2015-01-01 23:00:03", "25", "2015-01-01 23:01:02", "2", "Yes"));
    return access;
  }

  ArrayList<Click> generateClicks() throws Exception {
    var clicks = new ArrayList<Click>();
    clicks.add(new Click("2015-01-01 00:00:02", "23", "2.5"));
    clicks.add(new Click("2015-01-01 00:00:02", "23", "2.5"));

    return clicks;
  }


}
