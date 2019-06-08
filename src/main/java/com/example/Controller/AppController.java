package com.example.Controller;

import com.example.exception.InvalidOptionException;
import com.example.services.CategoryService;
import com.example.services.CustomerService;
import com.example.services.EquipmentService;
import com.example.services.RentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Scanner;

@Component
public class AppController {

    private Scanner scanner;
    private EquipmentService equipmentService;
    private CategoryService categoryService;
    private CustomerService customerService;
    private RentService rentService;

    public AppController(Scanner scanner, EquipmentService equipmentService, CategoryService categoryService,
                         CustomerService customerService, RentService rentService) {
        this.scanner = scanner;
        this.equipmentService = equipmentService;
        this.categoryService = categoryService;
        this.customerService = customerService;
        this.rentService = rentService;
    }

    public void mainLoop() {
        Options option;
        do {
            Options.printOptions();
            option = readOption();
            executeOption(option);
        } while(option != Options.EXIT_APP);
    }

    private Options readOption() {
        boolean correctOptionSelected = false;
        Options option = null;
        while(!correctOptionSelected) {
            System.out.println("Podaj ID opcji:");
            int optionId = scanner.nextInt();
            scanner.nextLine();
            try {
                option = Options.choosenOption(optionId);
                correctOptionSelected = true;
            } catch(InvalidOptionException e) {
                System.err.println(e.getMessage());
            }
        }
        return option;
    }

    private void executeOption(Options option) {
        switch (option) {
            case ADD_EQUIPMENT:
                equipmentService.createEquipment();
                break;
            case ADD_CATEGORY:
                categoryService.createCategory();
                break;
            case ADD_CUSTOMER:
                customerService.createCustomer();
                break;
            case SEARCH_EQUIPMENT:
                equipmentService.equipmentSearchByName();
                break;
            case RENT_EQUIPMENT:
                rentService.rentEquipment();
                break;
            case DELETE_EQUIPMENT:
                equipmentService.removeEquipment();
                break;
            case DELETE_CATEGORY:
                categoryService.removeCategory();
                break;
            case DELETE_CUSTOMER:
                customerService.removeCustomer();
                break;
            case EXIT_APP:
                closeApp();
        }
    }

    private void closeApp() {
        scanner.close();
        System.out.println("Bye bye!");
    }

    private enum Options{
        ADD_EQUIPMENT(1,"Dodaj urządzenie"),
        ADD_CATEGORY(2,"Dodaj kategorię"),
        ADD_CUSTOMER(3,"Dodaj klienta"),
        SEARCH_EQUIPMENT(4,"Wszyszukaj urządzenie"),
        RENT_EQUIPMENT(5,"Wypożycz urządzenie"),
        DELETE_EQUIPMENT(6,"Usuń urządzenie"),
        DELETE_CATEGORY(7,"Usuń kategorię"),
        DELETE_CUSTOMER(8,"Usuń klienta"),
        EXIT_APP(9,"Koniec");

        int option;
        String optionDescription;

        Options(int option, String optionDescreption) {
            this.option = option;
            this.optionDescription = optionDescreption;
        }

        @Override
        public String toString() {
            return option + " - " + optionDescription;
        }

        private static void printOptions(){
            Arrays.stream(Options.values()).forEach(System.out::println);
        }

        private static Options choosenOption(int option){
            if(option < 1 ||option > values().length) {
                throw new InvalidOptionException();
            }
            return values()[option-1];
        }
    }
}
