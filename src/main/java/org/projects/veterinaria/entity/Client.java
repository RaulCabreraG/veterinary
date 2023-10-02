package org.projects.veterinaria.entity;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name="clients")
public class Client {
    @Id
    @GeneratedValue
    @UuidGenerator
    private String id;
    @Column(name = "Name")
    private String name;
    @Column(name = "LastName")
    private String lastName;
    @Column(name = "Address")
    private String address;
    @Column(name = "RefNumber1")
    private int refNumber1;
    @Column(name = "RefNumber2")
    private int refNumber2;
    @OneToMany(mappedBy = "ownerId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pet> pets;

    public Client() {
    }

    public Client(String id, String name, String lastName, String address, int refNumber1, int refNumber2) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.address = address;
        this.refNumber1 = refNumber1;
        this.refNumber2 = refNumber2;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getRefNumber1() {
        return refNumber1;
    }

    public void setRefNumber1(int refNumber1) {
        this.refNumber1 = refNumber1;
    }

    public int getRefNumber2() {
        return refNumber2;
    }

    public void setRefNumber2(int refNumber2) {
        this.refNumber2 = refNumber2;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", address='" + address + '\'' +
                ", refNumber1=" + refNumber1 +
                ", refNumber2=" + refNumber2 +
                '}';
    }
}
