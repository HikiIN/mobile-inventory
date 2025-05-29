package com.example.mobileinventory.util;

import com.example.mobileinventory.model.Phone;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

    public static void savePhonesToFile(List<Phone> phones, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (Phone phone : phones) {
                writer.write(phone.toFileString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Помилка запису в текстовий файл: " + e.getMessage());
        }
    }

    public static List<Phone> loadPhonesFromFile(String fileName) {
        List<Phone> loadedPhones = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Phone phone = Phone.fromFileString(line);
                if (phone != null) {
                    loadedPhones.add(phone);
                }
            }
        } catch (IOException e) {
            System.err.println("Помилка читання з текстового файлу: " + e.getMessage());
        }
        return loadedPhones;
    }
}