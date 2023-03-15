package uk.ac.soton.adDashboard.Interfaces;

import java.time.LocalDateTime;

/**
 * An interface that allows functions to be passed around.
 */
public interface Function {


  double run(LocalDateTime start, LocalDateTime end);

}
