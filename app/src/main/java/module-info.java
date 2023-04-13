module uk.ac.soton.adDashboard {
    requires javafx.controls;
    requires java.scripting;
    requires org.apache.logging.log4j;
    requires javafx.graphics;
    requires javafx.swing;
    exports uk.ac.soton.adDashboard;
    //remove the following lines if you don't want to use the test modules
    exports uk.ac.soton.adDashboard.controller;
    exports uk.ac.soton.adDashboard.filter;
    exports uk.ac.soton.adDashboard.records;
    exports uk.ac.soton.adDashboard.enums;
    exports uk.ac.soton.adDashboard.Interfaces;
    exports uk.ac.soton.adDashboard.ui;


}
