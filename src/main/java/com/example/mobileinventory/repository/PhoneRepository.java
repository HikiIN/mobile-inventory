package com.example.mobileinventory.repository;

import com.example.mobileinventory.model.Phone;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface PhoneRepository {
    Phone findById(int id);
    List<Phone> findByModel(String model);
    List<Phone> findByYear(int year);
    List<Phone> findByPriceRange(double minPrice, double maxPrice);

    void addPhone(Phone phone);
    boolean removePhone(int id);
    boolean removePhone(Phone phone);
    boolean updatePhone(Phone phone);

    List<Phone> getAllPhones();
    List<Phone> findPhonesUsedMoreThanYears(int years);
    List<Phone> findPhonesByYearAndPriceGreaterThan(int year, double price);
    List<Phone> getSortedByLastCheckAndSerialNumber();
    Phone findPhoneBySerialNumber(String serialNumber);

    Set<String> getUniqueModelsWithYearLessThan(int year);
    Map<String, List<Phone>> getModelToPhonesMap();
    Map<String, Phone> getModelToMostExpensivePhoneMap();

    void saveData(String fileName);
    List<Phone> loadData(String fileName);
}