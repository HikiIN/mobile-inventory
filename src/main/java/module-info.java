module com.example.mobileinventory {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.example.mobileinventory.model to javafx.base;
    opens com.example.mobileinventory.controller to javafx.fxml;

    exports com.example.mobileinventory;
    exports com.example.mobileinventory.controller;
}