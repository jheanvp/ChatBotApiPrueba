package com.example.chatbotapiprueba.request;

import lombok.Getter;

@Getter
public class QueryResult {
    private Intent intent;

    private String action;

    private Parameters parameters;
}




