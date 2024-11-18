package com.kafka.example.kafkafilterdlt.model;

import lombok.Data;

@Data
public class User {
    private String id;
    private String name;
    private String email;

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
