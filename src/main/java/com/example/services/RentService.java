package com.example.services;

import com.example.exception.RentException;
import com.example.model.Customer;
import com.example.model.Equipment;
import com.example.repository.CustomerRepository;
import com.example.repository.EquipmentRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.Scanner;

@Service
public class RentService {

    private Scanner scanner;
    private EquipmentRepository equipmentRepository;
    private CustomerRepository customerRepository;

    public RentService(Scanner scanner, EquipmentRepository equipmentRepository, CustomerRepository customerRepository) {
        this.scanner = scanner;
        this.equipmentRepository = equipmentRepository;
        this.customerRepository = customerRepository;
    }

    @Transactional
    public void rentEquipment() {
        try {
            rent();
        } catch(RentException e) {
            System.err.println(e.getMessage());
        }
    }

    private void rent() {
        System.out.println("Podaj nr PESEL klienta:");
        String customerPesel = scanner.nextLine();
        System.out.println("Podaj ID urządzenia:");
        long equipmentId = scanner.nextLong();
        Optional<Customer> customer = customerRepository.findCustomerByPeselEquals(customerPesel);
        Optional<Equipment> equipment = equipmentRepository.findById(equipmentId);
        if(customer.isPresent())
            equipment.ifPresentOrElse(equ -> {
                if(equ.getQuantity() > equ.getCustomers().size())
                    equ.addCustomer(customer.get());
                else
                    throw new RentException("Brak wolnych urządzeń o wskazanym ID");
            }, () -> {
                throw new RentException("Brak urządzenia o wskazanym ID");
            });
        else
            throw new RentException("Brak klienta o wskazanym nr PESEL");
    }
}
