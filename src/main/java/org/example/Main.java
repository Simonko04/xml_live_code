package org.example;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;

import java.io.InputStream;
import java.util.Comparator;
import java.util.Scanner;


public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== RPG Inventory System ===");
            System.out.println("1. Vypis vsetky zbrane");
            System.out.println("2. Vypis silu zbrane podla ID");
            System.out.println("3. Vypis najsilnejsiu zbran");
            System.out.println("4. Znovu nacitaj XML");
            System.out.println("5. Koniec");
            System.out.print("Vyber moznosti: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    printAllWeapons();
                    break;
                case "2":
                    printWeaponPowerById(scanner);
                    break;
                case "3":
                    printStrongestWeapon();
                    break;
                case "4":
                    System.out.println("XML bolo znovu nacitane zo suboru.");
                    printAllWeapons();
                    break;
                case "5":
                    System.out.println("Program sa ukoncuje.");
                    return;
                default:
                    System.out.println("Neplatna volba.");
            }
        }
    }

    private static Inventory loadInventory() {
        try {
            JAXBContext context = JAXBContext.newInstance(Inventory.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();

            InputStream is = Main.class.getClassLoader().getResourceAsStream("items.xml");
            if (is == null) {
                throw new RuntimeException("Subor items.xml sa nenasiel v resources.");
            }

            return (Inventory) unmarshaller.unmarshal(is);
        } catch (Exception e) {
            throw new RuntimeException("Chyba pri nacitani XML: " + e.getMessage(), e);
        }
    }

    private static void printAllWeapons() {
        Inventory inventory = loadInventory();

        System.out.println("\n=== ZOZNAM ZBRANI ===");
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