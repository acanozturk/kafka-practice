package com.kafkaproducerdemo;

import lombok.*;

import java.util.Date;

@Data
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Message{

    private String url;
    private String id;
    private String user;
    private Date timestamp;
    private String message;


}
