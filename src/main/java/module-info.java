module com.gamehouses.gamehouses {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.gamehouses to javafx.fxml;
    exports com.gamehouses;
}