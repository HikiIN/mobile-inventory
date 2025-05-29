package com.example.mobileinventory.controller;

import com.example.mobileinventory.model.Phone;
import com.example.mobileinventory.service.PhoneService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.time.LocalDate;

public class PhoneController {
    @FXML
    private TableView<Phone> phoneTable;
    @FXML
    private TableColumn<Phone, Integer> idColumn;
    @FXML
    private TableColumn<Phone, String> modelColumn;
    @FXML
    private TableColumn<Phone, Integer> yearColumn;
    @FXML
    private TableColumn<Phone, Double> priceColumn;
    @FXML
    private TableColumn<Phone, String> serialNumberColumn;
    @FXML
    private TableColumn<Phone, Integer> storageColumn;
    @FXML
    private TableColumn<Phone, LocalDate> lastCheckColumn;

    @FXML
    private TextField modelField;
    @FXML
    private TextField yearsField;
    @FXML
    private TextField yearField;
    @FXML
    private TextField priceField;
    @FXML
    private TextField serialNumberField;
    @FXML
    private TextField yearLessThanField;
    @FXML
    private TextArea outputArea;

    @FXML
    private TextField newIdField;
    @FXML
    private TextField newModelField;
    @FXML
    private TextField newYearField;
    @FXML
    private TextField newPriceField;
    @FXML
    private TextField newSerialNumberField;
    @FXML
    private TextField newStorageField;
    @FXML
    private DatePicker newCheckDatePicker;

    private final PhoneService phoneService = new PhoneService();

    @FXML
    private void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        modelColumn.setCellValueFactory(new PropertyValueFactory<>("model"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        serialNumberColumn.setCellValueFactory(new PropertyValueFactory<>("serialNumber"));
        storageColumn.setCellValueFactory(new PropertyValueFactory<>("storageGb"));
        lastCheckColumn.setCellValueFactory(new PropertyValueFactory<>("lastCheckDate"));

        idColumn.setPrefWidth(50);
        modelColumn.setPrefWidth(120);
        yearColumn.setPrefWidth(80);
        priceColumn.setPrefWidth(100);
        serialNumberColumn.setPrefWidth(120);
        storageColumn.setPrefWidth(80);
        lastCheckColumn.setPrefWidth(120);

        PhoneService.Result result = phoneService.initializePhones();
        displayPhones(result.getPhones());
        outputArea.setText(result.getMessage());
    }

    private void displayPhones(java.util.List<Phone> phones) {
        phoneTable.getItems().clear();
        if (phones != null) {
            phoneTable.setItems(FXCollections.observableArrayList(phones));
        }
        phoneTable.refresh();
    }

    @FXML
    private void showAllPhones() {
        PhoneService.Result result = phoneService.getAllPhonesWithMessage();
        displayPhones(result.getPhones());
        outputArea.setText(result.getMessage());
    }

    @FXML
    private void listPhonesByModel() {
        PhoneService.Result result = phoneService.getPhonesByModel(modelField.getText().trim());
        displayPhones(result.getPhones());
        outputArea.setText(result.getMessage());
    }

    @FXML
    private void listPhonesByUsageYears() {
        PhoneService.Result result = phoneService.getPhonesByUsageYears(yearsField.getText().trim());
        displayPhones(result.getPhones());
        outputArea.setText(result.getMessage());
    }

    @FXML
    private void listPhonesByYearAndPrice() {
        PhoneService.Result result = phoneService.getPhonesByYearAndPrice(yearField.getText().trim(), priceField.getText().trim());
        displayPhones(result.getPhones());
        outputArea.setText(result.getMessage());
    }

    @FXML
    private void sortByCheckAndSerialNumber() {
        PhoneService.Result result = phoneService.getSortedByLastCheckAndSerialNumberWithMessage();
        displayPhones(result.getPhones());
        outputArea.setText(result.getMessage());
    }

    @FXML
    private void checkPhoneBySerialNumber() {
        PhoneService.Result result = phoneService.findPhoneBySerialNumberWithMessage(serialNumberField.getText().trim());
        displayPhones(result.getPhones());
        outputArea.setText(result.getMessage());
    }

    @FXML
    private void listUniqueModels() {
        PhoneService.Result result = phoneService.getPhonesByYearLessThan(yearLessThanField.getText().trim());
        displayPhones(result.getPhones());
        outputArea.setText(result.getMessage());
    }

    @FXML
    private void showModelToPhonesMap() {
        PhoneService.Result result = phoneService.getModelToPhonesMapWithMessage();
        displayPhones(result.getPhones());
        outputArea.setText(result.getMessage());
    }

    @FXML
    private void showMostExpensivePerModel() {
        PhoneService.Result result = phoneService.getMostExpensivePerModelWithMessage();
        displayPhones(result.getPhones());
        outputArea.setText(result.getMessage());
    }

    @FXML
    private void addNewPhone() {
        PhoneService.Result result = phoneService.addNewPhone(
                newIdField.getText().trim(),
                newModelField.getText().trim(),
                newYearField.getText().trim(),
                newPriceField.getText().trim(),
                newSerialNumberField.getText().trim(),
                newStorageField.getText().trim(),
                newCheckDatePicker.getValue()
        );
        if (result.isSuccess()) {
            clearNewPhoneFields();
            displayPhones(phoneService.getAllPhones());
        }
        outputArea.setText(result.getMessage());
    }

    @FXML
    private void clearFields() {
        modelField.clear();
        yearsField.clear();
        yearField.clear();
        priceField.clear();
        serialNumberField.clear();
        yearLessThanField.clear();
        clearNewPhoneFields();
        outputArea.clear();
    }

    private void clearNewPhoneFields() {
        newIdField.clear();
        newModelField.clear();
        newYearField.clear();
        newPriceField.clear();
        newSerialNumberField.clear();
        newStorageField.clear();
        newCheckDatePicker.setValue(null);
    }

    @FXML
    private void saveToFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Зберегти дані");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Текстові файли", "*.txt"),
                new FileChooser.ExtensionFilter("Усі файли", "*.*")
        );
        fileChooser.setInitialFileName("phones.txt");

        Stage stage = (Stage) phoneTable.getScene().getWindow();
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            PhoneService.Result result = phoneService.saveToFile(file.getAbsolutePath());
            outputArea.setText(result.getMessage());
        }
    }

    @FXML
    private void loadFromFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Завантажити дані");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Текстові файли", "*.txt"),
                new FileChooser.ExtensionFilter("Усі файли", "*.*")
        );

        Stage stage = (Stage) phoneTable.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            PhoneService.Result result = phoneService.loadFromFile(file.getAbsolutePath());
            displayPhones(result.getPhones());
            outputArea.setText(result.getMessage());
        }
    }
}