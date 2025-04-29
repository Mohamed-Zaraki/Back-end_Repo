module com.example.gestiontp {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.net.http;
    requires java.sql;
    requires jbcrypt;
    requires org.apache.poi.ooxml;
    requires java.desktop;

    opens com.example.gestiontp to javafx.fxml;
    exports com.example.gestiontp;
}