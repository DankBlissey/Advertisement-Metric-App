package uk.ac.soton.adDashboard.records;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class Main {

  public static void main(String[] args) throws Exception {
//    Click click = new Click("10/12/2015 17:02:05", 2, 5);
//    System.out.println(click.getDate());

//    DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
//    format.withResolverStyle(ResolverStyle.SMART);
//    LocalDateTime time = LocalDateTime.parse("10/12/2015 17:02", format);
//    System.out.println(time);
    LocalDateTime t = LocalDateTime.now();
    LocalDateTime tM5mins = t.minus(5, ChronoUnit.MINUTES);
    System.out.println(ChronoUnit.SECONDS.between(t, tM5mins));

    ArrayList<Click> clicks = new ArrayList<>();
    ArrayList<Impression> impressions = new ArrayList<>();
    ArrayList<ServerAccess> accesses = new ArrayList<>();
    HashMap<Long, User> users = new HashMap<>();
    impressions.add(new Impression("01/01/2015  12:03:01", 6122207195878290000L, 10));
    impressions.add(new Impression("01/01/2015  12:05:01", 6122207195878290001L, 12));
    users.put(6122207195878290000L,
        new User(6122207195878290000L, "Shopping", "<25", "Female", "High"));
    users.put(6122207195878290001L,
        new User(6122207195878290001L, "Shopping", "<25", "Female", "High"));

    DataSet dataSet = new DataSet(clicks, impressions, accesses, users);
    System.out.println(
        dataSet.generateY(impressions.get(0).getDate(),
            impressions.get(0).getDate().plus(Duration.ofDays(5)),
            ChronoUnit.DAYS));
  }

}
