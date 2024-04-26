package com.example.chatbotapiprueba.services;

import com.example.chatbotapiprueba.request.DialogflowRequest;
import com.example.chatbotapiprueba.response.DialogflowResponse;

import java.io.IOException;
import java.security.GeneralSecurityException;

public interface IIntentHandlerService {

    DialogflowResponse check(DialogflowRequest request) throws GeneralSecurityException, IOException;
}
