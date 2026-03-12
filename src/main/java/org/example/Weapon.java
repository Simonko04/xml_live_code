package org.example;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class Weapon {

    @XmlElement
    private int id;

    @XmlElement
    private String name;

    @XmlElement
    private int power;

    @XmlElement
    private double weight;

    @XmlElement
    private String element;

    public Weapon() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setElement(String element) {
        this.element = element;
    }

    public String getName() {
        return name;
    }


    public int getPower() {
        return power;
    }


    public double getWeight() {
        return weight;
    }


    public String getElement() {
        return element;
    }


    @Override
    public String toString() {
        return "Weapon{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", power=" + power +
                ", weight=" + weight +
                ", element='" + element + '\'' +
                '}';
    }
}
