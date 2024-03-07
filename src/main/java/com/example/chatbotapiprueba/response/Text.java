package com.example.chatbotapiprueba.response;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class Text {
    @Getter
    @Setter
    private List<String> text = new ArrayList<>();

}
