package org.example;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;

import java.util.Comparator;
import java.util.Scanner;
import java.io.File;



public class Main {


    private enum MenuOption {//enum na najdenie moznosti z inputu
        ALL("1"),
        BY_ID("2"),
        STRONGEST("3"),
        EXIT("4");

        private final String value;

        MenuOption(String value) {
            this.value = value;
        }

        public static MenuOption fromInput(String input) {
            for (MenuOption option : values()) {
                if (option.value.equals(input)) {
                    return option;
                }
            }
            return null;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in); // trieda na citanie vstupu od pouzivatela

        while (true) {
            System.out.println("\n=== RPG Inventory System ===");
            System.out.println("1. Vypis vsetky zbrane");
            System.out.println("2. Vypis silu zbrane podla ID");
            System.out.println("3. Vypis najsilnejsiu zbran");
            System.out.println("4. Koniec");
            System.out.print("Vyber moznosti: ");

            String choice = scanner.nextLine();

            MenuOption option = MenuOption.fromInput(choice);

            if (option == null) {
                System.out.println("Neplatna volba.");
                continue;
            }

            switch (option) {
                case ALL:
                    printAllWeapons();//strukturovany vypis celeho xml
                    break;
                case BY_ID:
                    printWeaponPowerById(scanner);//vypise jeden int podla zadaneho id
                    break;
                case STRONGEST:
                    printStrongestWeapon();//vypise zbran s najvysou power
                    break;
                case EXIT:
                    System.out.println("Program sa ukoncuje.");//riadne ukoncenie programu
                    return;
            }
        }
    }

    private static Inventory loadInventory() {
        try {
            // Vytvorenie JAXB kontextu pre triedu Inventory
            JAXBContext context = JAXBContext.newInstance(Inventory.class);

            //deserializer xml
            Unmarshaller unmarshaller = context.createUnmarshaller();

            // Vytvorenie cesty k XML súboru podľa aktuálneho pracovného adresára
            File file = new File(System.getProperty("user.dir"), "skola/src/main/resources/items.xml");

            return (Inventory) unmarshaller.unmarshal(file);
        } catch (Exception e) {
            throw new RuntimeException("Chyba pri nacitani XML: " + e.getMessage(), e);
        }
    }

    private static void printAllWeapons() {
        Inventory inventory = loadInventory();

        System.out.println("\n=== ZOZNAM ZBRANI ===");

        //for each loop na prechadzanie zbrani a ich atributov v inventary
        for (Weapon weapon : inventory.getWeapons()) {
            System.out.println(
                    "ID: " + weapon.getId() +
                            ", nazov: " + weapon.getName() +
                            ", sila: " + weapon.getPower() +
                            ", weight: " + weapon.getWeight() +
                            ", element: " + weapon.getElement()
            );
        }
    }

    //tato method si do searchedId nacita id a v range-based cycle ho najde
    private static void printWeaponPowerById(Scanner scanner) {
        Inventory inventory = loadInventory();

        System.out.print("Zadaj ID zbrane: ");
        int searchedId = Integer.parseInt(scanner.nextLine());

        for (Weapon weapon : inventory.getWeapons()) {
            if (weapon.getId() == searchedId) {
                System.out.println("Zbran " + weapon.getName() + " ma silu " + weapon.getPower());
                return;
            }
        }

        System.out.println("Zbran s tymto ID nebola najdena.");
    }

    //metoda na najdenie najsilnejsej zbrane podla power
    private static void printStrongestWeapon() {
        Inventory inventory = loadInventory();

        Weapon strongest = inventory.getWeapons()
                .stream()
                .max(Comparator.comparingInt(Weapon::getPower))
                .orElse(null);

        if (strongest == null) {
            System.out.println("Inventory je prazdny.");
            return;
        }

        System.out.println("Najsilnejsia zbran je " + strongest.getName()
                + " so silou " + strongest.getPower());
    }
}