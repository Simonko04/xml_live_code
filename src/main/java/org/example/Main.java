package org.example;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.Comparator;
import java.util.Scanner;
import java.io.File;



public class Main {


    private enum MenuOption {
        ALL("1"),
        BY_ID("2"),
        STRONGEST("3"),
        ALL_DOM("4"),
        EXIT("5");

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
            System.out.println("4. Vypis vsetky zbrane cez DOM");
            System.out.println("5. Koniec");
            System.out.print("Vyber moznosti: ");

            String choice = scanner.nextLine();

            MenuOption option = MenuOption.fromInput(choice);

            if (option == null) {
                System.out.println("Neplatna volba.");
                continue;
            }

            switch (option) {
                case ALL:
                    printAllWeapons();
                    break;
                case BY_ID:
                    printWeaponPowerById(scanner);
                    break;
                case STRONGEST:
                    printStrongestWeapon();
                    break;
                case ALL_DOM:
                    printAllWeaponsDOM();
                    break;
                case EXIT:
                    System.out.println("Program sa ukoncuje.");
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
            //potrebne prepisat pri pouziti
            File file = new File(System.getProperty("user.dir"), "skola/src/main/resources/items.xml");//potrebne prepisat

            return (Inventory) unmarshaller.unmarshal(file);
        } catch (Exception e) {
            throw new RuntimeException("Chyba pri nacitani XML: " + e.getMessage(), e);
        }
    }
    private static Inventory loadInventoryDOM() {
        try {
            File file = new File(System.getProperty("user.dir"), "skola/src/main/resources/itemsLarger.xml");

            //Podobne ako pri JAXB, ale pouzivame Factory, pomocou ktorej vytvarame builder (parser) dokumentov
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            //Pouzivame builder na sparsovanie suboru do Document
            Document document = builder.parse(file);

            // document.getDocumentElement().normalize();  //Best practice, ale nerobi to nic
            Inventory inventory = new Inventory();

            //Vytvaram NodeList v ktorom budem mat vsetky Nodes z DOM stromu, ziskavam ich podla TagName weapon
            NodeList weaponNodes = document.getElementsByTagName("weapon");

            //Iterujem kazdou Node z NodeListu (pomocou for i, aby som si mohol vybrat napr prvych 10 iba)
            for (int i = 0; i < weaponNodes.getLength(); i++) {

                //Current Node s ktorym pracujem
                Node node = weaponNodes.item(i);

            /*Kedze DOM cita vsetko co sa v subore nachadza (aj komentare) preto
            musime filtrovat podla ELEMENT_NODE aby sme pracovali len s realnymi datami*/
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    //Vytvaram Element s ktorym viem jednoducho pracovat
                    Element weaponElement = (Element) node;

                    //Ziskavam data z Element na zaklade tagov v subore Int a Float musim parsovat, kedze su ulozene ako text
                    int id = Integer.parseInt(
                            weaponElement
                                    .getElementsByTagName("id")
                                    .item(0)
                                    .getTextContent()
                    );

                    String name = weaponElement
                            .getElementsByTagName("name")
                            .item(0)
                            .getTextContent();

                    int power = Integer.parseInt(
                            weaponElement
                                    .getElementsByTagName("power")
                                    .item(0)
                                    .getTextContent()
                    );

                    float weight = Float.parseFloat(
                            weaponElement
                                    .getElementsByTagName("weight")
                                    .item(0)
                                    .getTextContent()
                    );

                    String element = weaponElement
                            .getElementsByTagName("element")
                            .item(0)
                            .getTextContent();

                    //Vytvaram Weapon a pridavam data ziskane z Node
                    Weapon weapon = new Weapon();
                    weapon.setId(id);
                    weapon.setName(name);
                    weapon.setPower(power);
                    weapon.setWeight(weight);
                    weapon.setElement(element);

                    //Pridavam do inventara
                    inventory.getWeapons().add(weapon);
                }
            }

            return inventory;
            //Ak nastane chyba, vypisem ju
        } catch (Exception e) {
            throw new RuntimeException("Chyba pri nacitani XML (DOM): " + e.getMessage(), e);
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
    private static void printAllWeaponsDOM() {
        Inventory inventory = loadInventoryDOM();
        //Pridany maximum aby sa vypisali len prvych x itemov
        int maximum = 10;
        System.out.println("\n=== ZOZNAM ZBRANI ===");
        for (Weapon weapon : inventory.getWeapons()) {
            System.out.println(
                    "ID: " + weapon.getId() +
                            ", nazov: " + weapon.getName() +
                            ", sila: " + weapon.getPower() +
                            ", weight: " + weapon.getWeight() +
                            ", element: " + weapon.getElement()
            );
            //Kazdou vypisanou zbranou znizujem maximum a ak vypisem vsetko, breaknem for loop
            maximum -= 1;
            if (maximum <= 0) break;
        }
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