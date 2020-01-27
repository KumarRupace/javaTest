package com.assesmentdbs.customerjourney.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Prithvi Panchapakeshan
 *
 */
@Entity
@Data
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    private Date dateOfBirth;
    private String address;

    public Customer() {
    }

    public Customer(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
