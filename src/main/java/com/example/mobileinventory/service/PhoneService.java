package com.example.mobileinventory.service;

import com.example.mobileinventory.model.Phone;
import com.example.mobileinventory.repository.PhoneRepository;
import com.example.mobileinventory.repository.PhoneRepositoryCollectionImpl;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PhoneService {
    private List<Phone> phones;
    private final PhoneRepository repository;

    public static class Result {
        private final List<Phone> phones;
        private final String message;
        private final boolean success;

        public Result(List<Phone> phones, String message, boolean success) {
            this.phones = phones;
            this.message = message;
            this.success = success;
        }

        public List<Phone> getPhones() {
            return phones;
        }

        public String getMessage() {
            return message;
        }

        public boolean isSuccess() {
            return success;
        }
    }

    public PhoneService() {
        this.phones = new ArrayList<>();
        this.repository = new PhoneRepositoryCollectionImpl();

        File file = new File("phones.txt");
        if (file.exists()) {
            loadFromFile("phones.txt");
        } else {
            initTestData();
        }
    }

    private void initTestData() {
        Phone[] initialPhones = {
                new Phone(1, "iPhone 12", 2020, 25000, "SN123456", 128, LocalDate.of(2023, 5, 10)),
                new Phone(2, "Samsung S10", 2019, 15000, "SN654321", 64, LocalDate.of(2022, 7, 15)),
                new Phone(3, "Google Pixel 6", 2021, 35000, "SN789012", 256, LocalDate.of(2024, 2, 28)),
                new Phone(4, "iPhone 11", 2019, 18000, "SN345678", 64, LocalDate.of(2023, 11, 7)),
                new Phone(5, "Xiaomi Mi 11", 2021, 20000, "SN901234", 128, LocalDate.of(2023, 9, 22))
        };
        this.phones = new ArrayList<>(java.util.Arrays.asList(initialPhones));
        repository.saveData("phones.txt");
        loadFromFile("phones.txt");
    }

    public Result initializePhones() {
        List<Phone> initialPhones = getAllPhones();
        if (initialPhones.isEmpty()) {
            return new Result(null, "Помилка ініціалізації: список телефонів порожній.", false);
        }
        return new Result(initialPhones, "Система готова. Усього телефонів: " + initialPhones.size(), true);
    }

    public Result getAllPhonesWithMessage() {
        List<Phone> allPhones = getAllPhones();
        return new Result(allPhones, "Показано всі телефони. Усього: " + allPhones.size(), true);
    }

    public List<Phone> getAllPhones() {
        return new ArrayList<>(phones);
    }

    public void setPhones(List<Phone> phones) {
        this.phones = new ArrayList<>(phones);
    }

    public Result addNewPhone(String idText, String model, String yearText, String priceText, String serialNumber, String storageText, LocalDate checkDate) {
        if (idText.isEmpty() || model.isEmpty() || yearText.isEmpty() || priceText.isEmpty() || serialNumber.isEmpty() || storageText.isEmpty()) {
            return new Result(null, "Помилка: не всі поля заповнені.", false);
        }

        try {
            int id = Integer.parseInt(idText);
            int year = Integer.parseInt(yearText);
            double price = Double.parseDouble(priceText);
            int storage = Integer.parseInt(storageText);

            if (year < 2000 || year > 2025) {
                return new Result(null, "Помилка: рік має бути між 2000 і 2025.", false);
            }
            if (price < 0) {
                return new Result(null, "Помилка: ціна не може бути від’ємною.", false);
            }
            if (phones.stream().anyMatch(phone -> phone.getId() == id)) {
                return new Result(null, "Помилка: ID уже існує.", false);
            }

            Phone newPhone = new Phone(id, model, year, price, serialNumber, storage, checkDate);
            phones.add(newPhone);
            repository.addPhone(newPhone);
            return new Result(getAllPhones(), "Телефон додано. ID: " + id, true);
        } catch (NumberFormatException e) {
            return new Result(null, "Помилка: числові поля мають бути числами.", false);
        }
    }

    public boolean removePhone(int id) {
        Phone phoneToRemove = phones.stream()
                .filter(phone -> phone.getId() == id)
                .findFirst()
                .orElse(null);

        if (phoneToRemove != null) {
            phones.remove(phoneToRemove);
            repository.removePhone(phoneToRemove);
            return true;
        }
        return false;
    }

    public boolean removePhone(Phone phone) {
        boolean removed = phones.remove(phone);
        if (removed) {
            repository.removePhone(phone);
        }
        return removed;
    }

    public Result getPhonesByModel(String model) {
        if (model.isEmpty()) {
            return new Result(getAllPhones(), "Помилка: поле моделі порожнє.", false);
        }
        List<Phone> phones = repository.findByModel(model);
        if (phones.isEmpty()) {
            return new Result(getAllPhones(), "Телефони моделі '" + model + "' не знайдені.", false);
        }
        return new Result(phones, "Знайдено " + phones.size() + " телефонів моделі '" + model + "'", true);
    }

    public Result getPhonesByUsageYears(String yearsText) {
        if (yearsText.isEmpty()) {
            return new Result(getAllPhones(), "Помилка: поле років порожнє.", false);
        }
        try {
            int years = Integer.parseInt(yearsText);
            if (years < 0) {
                return new Result(getAllPhones(), "Помилка: кількість років не може бути від’ємною.", false);
            }
            List<Phone> phones = repository.findPhonesUsedMoreThanYears(years);
            if (phones.isEmpty()) {
                return new Result(getAllPhones(), "Телефони, які використовуються понад " + years + " років, не знайдені.", false);
            }
            return new Result(phones, "Знайдено " + phones.size() + " телефонів, які використовуються понад " + years + " років", true);
        } catch (NumberFormatException e) {
            return new Result(getAllPhones(), "Помилка: роки мають бути числом.", false);
        }
    }

    public Result getPhonesByYearAndPrice(String yearText, String priceText) {
        if (yearText.isEmpty() || priceText.isEmpty()) {
            return new Result(getAllPhones(), "Помилка: поля року або ціни порожні.", false);
        }
        try {
            int year = Integer.parseInt(yearText);
            double price = Double.parseDouble(priceText);

            if (year < 2000 || year > 2025) {
                return new Result(getAllPhones(), "Помилка: рік має бути між 2000 і 2025.", false);
            }
            if (price < 0) {
                return new Result(getAllPhones(), "Помилка: ціна не може бути від’ємною.", false);
            }

            List<Phone> phones = repository.findPhonesByYearAndPriceGreaterThan(year, price);
            if (phones.isEmpty()) {
                return new Result(getAllPhones(), "Телефони " + year + " року з ціною більше " + price + " не знайдені.", false);
            }
            return new Result(phones, "Знайдено " + phones.size() + " телефонів " + year + " року з ціною більше " + price, true);
        } catch (NumberFormatException e) {
            return new Result(getAllPhones(), "Помилка: рік або ціна мають бути числами.", false);
        }
    }

    public Result getSortedByLastCheckAndSerialNumberWithMessage() {
        List<Phone> phones = repository.getSortedByLastCheckAndSerialNumber();
        return new Result(phones, "Телефони відсортовані за датою перевірки та серійним номером", true);
    }

    public Result findPhoneBySerialNumberWithMessage(String serialNumber) {
        if (serialNumber.isEmpty()) {
            return new Result(getAllPhones(), "Помилка: поле серійного номера порожнє.", false);
        }
        Phone phone = repository.findPhoneBySerialNumber(serialNumber);
        if (phone != null) {
            return new Result(List.of(phone), "Телефон з серійним номером '" + serialNumber + "' знайдено", true);
        }
        return new Result(getAllPhones(), "Телефон з серійним номером '" + serialNumber + "' не знайдено.", false);
    }

    public Result getPhonesByYearLessThan(String yearText) {
        if (yearText.isEmpty()) {
            return new Result(getAllPhones(), "Помилка: поле року порожнє.", false);
        }
        try {
            int year = Integer.parseInt(yearText);
            if (year < 2000 || year > 2025) {
                return new Result(getAllPhones(), "Помилка: рік має бути між 2000 і 2025.", false);
            }
            List<Phone> filteredPhones = phones.stream()
                    .filter(phone -> phone.getYear() < year)
                    .collect(Collectors.toList());
            if (filteredPhones.isEmpty()) {
                return new Result(getAllPhones(), "Телефони до " + year + " року не знайдені.", false);
            }
            return new Result(filteredPhones, "Знайдено " + filteredPhones.size() + " телефонів до " + year + " року", true);
        } catch (NumberFormatException e) {
            return new Result(getAllPhones(), "Помилка: рік має бути числом.", false);
        }
    }

    public Result getModelToPhonesMapWithMessage() {
        Map<String, List<Phone>> map = repository.getModelToPhonesMap();
        if (map.isEmpty()) {
            return new Result(getAllPhones(), "Дані відсутні.", false);
        }
        return new Result(getAllPhones(), "Карта моделей відображена. Усього моделей: " + map.size(), true);
    }

    public Result getMostExpensivePerModelWithMessage() {
        Map<String, Phone> map = repository.getModelToMostExpensivePhoneMap();
        if (map.isEmpty()) {
            return new Result(getAllPhones(), "Дані відсутні.", false);
        }
        List<Phone> phones = new ArrayList<>(map.values());
        return new Result(phones, "Найдорожчі телефони відображені. Усього моделей: " + map.size(), true);
    }

    public Result saveToFile(String fileName) {
        repository.saveData(fileName);
        return new Result(null, "Дані збережено у файл: " + new File(fileName).getName(), true);
    }

    public Result loadFromFile(String fileName) {
        try {
            phones = repository.loadData(fileName);
            return new Result(getAllPhones(), "Дані завантажено з файлу: " + new File(fileName).getName() + ". Усього телефонів: " + phones.size(), true);
        } catch (Exception e) {
            return new Result(getAllPhones(), "Помилка завантаження файлу: " + e.getMessage() + ".", false);
        }
    }
}