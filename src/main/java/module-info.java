module com.pts.qlcf {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.base;

    opens com.pts.qlcf to javafx.fxml;
    exports com.pts.qlcf;
    requires jbcrypt;
}
