package uk.ac.soton.adDashboard.Interfaces;

import java.util.List;
import uk.ac.soton.adDashboard.filter.Filter;

/**
 * This interface is subject to change and is what the filter "pane" must support.
 */
public interface FilterWindow {

  /**
   * Gets a list of Filter objects.
   * @return Returns all the Filter objects in the filter pane.
   */
  List<Filter> getFilters();

}
