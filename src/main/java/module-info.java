module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires java.logging;
    requires org.json;
    requires org.jsoup;
    requires org.apache.commons.io;

    opens com.example.scraperGUI to javafx.fxml;
    exports com.example.scraperGUI;
}