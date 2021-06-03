package com.kafkaproducerdemo;

import lombok.*;

import java.util.Date;

@Data
@EqualsAndHashCode
@ToString
public class Message {

    private String url;
    private String id;
    private String city;
    private String user;
    private String email;
    private Date timestamp;
    private String message;

}
