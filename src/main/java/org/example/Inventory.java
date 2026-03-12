package org.example;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.ArrayList;
import java.util.List;


@XmlRootElement(name = "inventory")
@XmlAccessorType(XmlAccessType.FIELD)
public class Inventory {

    @XmlElement(name = "weapon")
    private List<Weapon> weapons = new ArrayList<>();

    public Inventory() {
    }

    public List<Weapon> getWeapons() {
        return weapons;
    }

}
