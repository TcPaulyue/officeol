package com.example.officeol.bean;

import org.springframework.boot.autoconfigure.domain.EntityScan;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 */
@Entity
@Table(name = "TABLE_USER")
public class User {

    @Id
    @Column(name = "ID")
    private String id;

    public User(){}
    public User(String userId) {
        this.id=userId;
    }

    public String getId() {
        return id;
    }
    public void setId(String id){
        this.id = id;
    }
}
