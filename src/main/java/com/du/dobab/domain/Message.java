package com.du.dobab.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

@ToString
@NoArgsConstructor
@Getter
public class Message implements Serializable {

    private String sender;
    private String content;
    private String timestamp = LocalDateTime.now().toString();

    public Message(String sender, String content) {
        this.sender = sender;
        this.content = content;
    }
}
