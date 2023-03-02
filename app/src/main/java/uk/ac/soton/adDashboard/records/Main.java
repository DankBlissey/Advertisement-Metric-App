package uk.ac.soton.adDashboard.records;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Main {

  public static void main(String[] args) throws Exception {
    Click click = new Click("10/12/2015 17:02", 2, 5);
    System.out.println(click.getDate());

//    DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
//    format.withResolverStyle(ResolverStyle.SMART);
//    LocalDateTime time = LocalDateTime.parse("10/12/2015 17:02", format);
//    System.out.println(time);

  }

}
