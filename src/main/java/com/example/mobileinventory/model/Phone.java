package com.example.mobileinventory.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Year;
import java.util.Objects;

public class Phone implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String model;
    private int year;
    private double price;
    private String serialNumber;
    private int storageGb;
    private LocalDate lastCheckDate;

    public Phone() {
    }

    public Phone(int id, String model, int year, double price, String serialNumber, int storageGb, LocalDate lastCheckDate) {
        this.id = id;
        this.model = model;
        this.year = year;
        this.price = price;
        this.serialNumber = serialNumber;
        this.storageGb = storageGb;
        this.lastCheckDate = lastCheckDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public int getStorageGb() {
        return storageGb;
    }

    public void setStorageGb(int storageGb) {
        this.storageGb = storageGb;
    }

    public LocalDate getLastCheckDate() {
        return lastCheckDate;
    }

    public void setLastCheckDate(LocalDate lastCheckDate) {
        this.lastCheckDate = lastCheckDate;
    }

    public boolean isUsedMoreThanYears(int years) {
        int currentYear = Year.now().getValue();
        return (currentYear - year) > years;
    }

    public int getLastCheckYear() {
        return lastCheckDate != null ? lastCheckDate.getYear() : 0;
    }

    @Override
    public String toString() {
        return "Phone{" +
                "id=" + id +
                ", model='" + model + '\'' +
                ", year=" + year +
                ", price=" + price +
                ", serialNumber='" + serialNumber + '\'' +
                ", storageGb=" + storageGb +
                ", lastCheckDate=" + lastCheckDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Phone phone = (Phone) o;
        return id == phone.id &&
                year == phone.year &&
                Double.compare(phone.price, price) == 0 &&
                storageGb == phone.storageGb &&
                Objects.equals(model, phone.model) &&
                Objects.equals(serialNumber, phone.serialNumber) &&
                Objects.equals(lastCheckDate, phone.lastCheckDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, model, year, price, serialNumber, storageGb, lastCheckDate);
    }

    public String toFileString() {
        return id + "," + model + "," + year + "," + price + "," + serialNumber + "," + storageGb + "," +
                (lastCheckDate != null ? lastCheckDate.toString() : "null");
    }

    public static Phone fromFileString(String line) {
        String[] parts = line.split(",");
        if (parts.length == 7) {
            LocalDate checkDate = parts[6].equals("null") ? null : LocalDate.parse(parts[6]);
            return new Phone(
                    Integer.parseInt(parts[0]),
                    parts[1],
                    Integer.parseInt(parts[2]),
                    Double.parseDouble(parts[3]),
                    parts[4],
                    Integer.parseInt(parts[5]),
                    checkDate
            );
        }
        return null;
    }
}