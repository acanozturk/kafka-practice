package com.kafkaproducerdemo;

import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Data
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Message implements Serializable {

    static final long serialVersionUID = 42L;

    private UUID uuid;
    private Date timestamp;
    private String message;

}
