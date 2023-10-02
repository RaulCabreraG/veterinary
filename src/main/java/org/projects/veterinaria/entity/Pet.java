package org.projects.veterinaria.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "pets")
public class Pet {
    @Id
    @GeneratedValue
    @UuidGenerator
    private String id;
    @ManyToOne
    @JoinColumn(name = "ownerId")
    private Client ownerId;
    @Column(name = "petName")
    private String petName;
    @Column(name = "type")
    private  String type;
    @Column(name = "race")
    private String race;
    @Column(name = "color")
    private String color;
    @Column(name = "gender")
    private char gender;
    @Column(name = "age")
    private byte age;

    public Pet() {
    }

    public Pet(String id, Client client, String petName, String type, String race, String color, char gender, byte age) {
        this.id = id;
        this.ownerId = client;
        this.petName = petName;
        this.type = type;
        this.race = race;
        this.color = color;
        this.gender = gender;
        this.age = age;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Client getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Client ownerId) {
        this.ownerId = ownerId;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    public byte getAge() {
        return age;
    }

    public void setAge(byte age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Pet{" +
                "id='" + id + '\'' +
                ", ownerId='" + ownerId + '\'' +
                ", petName='" + petName + '\'' +
                ", type='" + type + '\'' +
                ", race='" + race + '\'' +
                ", color='" + color + '\'' +
                ", gender=" + gender +
                ", age=" + age +
                '}';
    }
}
