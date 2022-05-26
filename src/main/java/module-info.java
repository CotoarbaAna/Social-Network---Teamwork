module lab {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires org.postgresql.jdbc;
    requires java.sql;
    requires java.desktop;
    //requires org.junit.jupiter.api;
    opens lab6.domain to javafx.fxml;
    opens lab6.service to javafx.fxml;
    opens lab6.repository to javafx.fxml;
    opens lab6.lab6 to javafx.fxml;
    exports lab6.lab6;
    exports lab6.domain;
    exports lab6.service;
    exports lab6.repository;
}