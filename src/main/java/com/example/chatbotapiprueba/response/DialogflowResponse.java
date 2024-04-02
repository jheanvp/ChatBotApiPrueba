package com.example.chatbotapiprueba.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@JsonSerialize
public class DialogflowResponse {

    @Setter
    List<FulfillmentMessages> fulfillmentMessages = new ArrayList<>();

}
