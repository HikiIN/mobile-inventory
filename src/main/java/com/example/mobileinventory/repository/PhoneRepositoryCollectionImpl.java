package com.example.mobileinventory.repository;

import com.example.mobileinventory.model.Phone;
import com.example.mobileinventory.util.FileUtil;

import java.time.Year;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PhoneRepositoryCollectionImpl implements PhoneRepository {
    private List<Phone> phones = new ArrayList<>();
    private String defaultFileName = "phones.txt";

    @Override
    public void saveData(String fileName) {
        FileUtil.savePhonesToFile(phones, fileName);
        this.defaultFileName = fileName;
    }

    @Override
    public List<Phone> loadData(String fileName) {
        this.phones = FileUtil.loadPhonesFromFile(fileName);
        this.defaultFileName = fileName;
        return new ArrayList<>(phones);
    }

    @Override
    public Phone findById(int id) {
        return phones.stream()
                .filter(phone -> phone.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Phone> findByModel(String model) {
        return phones.stream()
                .filter(phone -> phone.getModel().equals(model))
                .collect(Collectors.toList());
    }

    @Override
    public List<Phone> findByYear(int year) {
        return phones.stream()
                .filter(phone -> phone.getYear() == year)
                .collect(Collectors.toList());
    }

    @Override
    public List<Phone> findByPriceRange(double minPrice, double maxPrice) {
        return phones.stream()
                .filter(phone -> phone.getPrice() >= minPrice && phone.getPrice() <= maxPrice)
                .collect(Collectors.toList());
    }

    @Override
    public void addPhone(Phone phone) {
        if (!phones.contains(phone)) {
            phones.add(phone);
            saveData(defaultFileName);
        }
    }

    @Override
    public boolean removePhone(int id) {
        boolean removed = phones.removeIf(phone -> phone.getId() == id);
        if (removed) {
            saveData(defaultFileName);
        }
        return removed;
    }

    @Override
    public boolean removePhone(Phone phone) {
        boolean removed = phones.remove(phone);
        if (removed) {
            saveData(defaultFileName);
        }
        return removed;
    }

    @Override
    public boolean updatePhone(Phone phone) {
        for (int i = 0; i < phones.size(); i++) {
            if (phones.get(i).getId() == phone.getId()) {
                phones.set(i, phone);
                saveData(defaultFileName);
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Phone> getAllPhones() {
        return new ArrayList<>(phones);
    }

    @Override
    public List<Phone> findPhonesUsedMoreThanYears(int years) {
        int currentYear = Year.now().getValue();
        return phones.stream()
                .filter(phone -> (currentYear - phone.getYear()) > years)
                .collect(Collectors.toList());
    }

    @Override
    public List<Phone> findPhonesByYearAndPriceGreaterThan(int year, double price) {
        return phones.stream()
                .filter(phone -> phone.getYear() == year && phone.getPrice() > price)
                .collect(Collectors.toList());
    }

    @Override
    public List<Phone> getSortedByLastCheckAndSerialNumber() {
        return phones.stream()
                .sorted(Comparator.comparing(Phone::getLastCheckDate,
                                Comparator.nullsLast(Comparator.naturalOrder()))
                        .thenComparing(Phone::getSerialNumber))
                .collect(Collectors.toList());
    }

    @Override
    public Phone findPhoneBySerialNumber(String serialNumber) {
        return phones.stream()
                .filter(phone -> phone.getSerialNumber().equals(serialNumber))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Set<String> getUniqueModelsWithYearLessThan(int year) {
        return phones.stream()
                .filter(phone -> phone.getYear() < year)
                .map(Phone::getModel)
                .collect(Collectors.toSet());
    }

    @Override
    public Map<String, List<Phone>> getModelToPhonesMap() {
        Map<String, List<Phone>> modelToPhonesMap = phones.stream()
                .collect(Collectors.groupingBy(Phone::getModel));

        modelToPhonesMap.forEach((model, phoneList) ->
                phoneList.sort(Comparator.comparing(Phone::getYear).reversed()));

        return modelToPhonesMap;
    }

    @Override
    public Map<String, Phone> getModelToMostExpensivePhoneMap() {
        return phones.stream()
                .collect(Collectors.toMap(
                        Phone::getModel,
                        Function.identity(),
                        (phone1, phone2) -> phone1.getPrice() > phone2.getPrice() ? phone1 : phone2
                ));
    }
}