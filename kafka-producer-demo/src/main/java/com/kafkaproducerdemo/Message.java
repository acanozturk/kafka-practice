package com.kafkaproducerdemo;

import lombok.*;

import java.util.Date;

@Data
public class Message {

    private String url;
    private String id;
    private String city;
    private String user;
    private Integer age;
    private String email;
    private Date timestamp;
    private String message;

}
