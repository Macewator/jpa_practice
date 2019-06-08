package com.example.services;

import com.example.exception.CategoryNotFoundException;
import com.example.exception.EquipmentNotFoundException;
import com.example.model.Category;
import com.example.model.Equipment;
import com.example.repository.CategoryRepository;
import com.example.repository.EquipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@Service
public class EquipmentService {

    private Scanner scanner;
    private EquipmentRepository equipmentRepository;
    private CategoryRepository categoryRepository;

    @Autowired
    public EquipmentService(Scanner scanner, EquipmentRepository equipmentRepository, CategoryRepository categoryRepository) {
        this.scanner = scanner;
        this.equipmentRepository = equipmentRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public void createEquipment() {
        try {
            Equipment equipment = readEquipment();
            equipmentRepository.save(equipment);
            System.out.println("Dodano urządzenie");
            System.out.println(equipment);
        } catch (CategoryNotFoundException e) {
            System.err.println("Urządzenia nie dodano. " + e.getMessage());
        }
    }

    private Equipment readEquipment() {
        Equipment equipment = new Equipment();
        System.out.println("Nazwa urządzenia:");
        equipment.setName(scanner.nextLine());
        System.out.println("Opis urządzenia:");
        equipment.setDescription(scanner.nextLine());
        System.out.println("Cena urządzenia:");
        equipment.setPrice(scanner.nextDouble());
        System.out.println("Ilość(szt) urządzenia:");
        equipment.setQuantity(scanner.nextInt());
        scanner.nextLine();
        System.out.println("Kategoria urządzenia:");
        String categoryName = scanner.nextLine();
        Optional<Category> category = categoryRepository.findCategoryByName(categoryName);
        category.ifPresentOrElse(equipment::setCategory,
                ()-> {throw new CategoryNotFoundException("Kategoria o takiej nazwie nie istnieje");});
        return equipment;
    }

    public void removeEquipment() {
        System.out.println("Podaj id urządzenia, które chcesz usunąć:");
        long deviceId = scanner.nextLong();
        Optional<Equipment> device = equipmentRepository.findById(deviceId);
        device.ifPresentOrElse(equipmentRepository::delete, () -> System.out.println("Brak urządzenia o wskazanym ID"));
    }

    public void equipmentSearchByName(){
        System.out.println("Podaj nawzę produktu lub jej część");
        String equipmentName = scanner.nextLine();
        List<Equipment> equipments = equipmentRepository.findAllByNameContainingOrderByNameAsc(equipmentName);
        if(!equipments.isEmpty()){
            System.out.println("Znalezione urządzenia:");
            equipments.forEach(System.out::println);
        }else {
            System.out.println("Brak urządzeń o wskazanej nazwie");
        }
    }
}
