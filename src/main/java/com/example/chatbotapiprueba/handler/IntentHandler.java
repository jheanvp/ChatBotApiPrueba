package com.example.chatbotapiprueba.handler;

import com.example.chatbotapiprueba.response.DialogflowResponse;

import java.io.IOException;
import java.security.GeneralSecurityException;

public interface IntentHandler {
    DialogflowResponse handleIntent(String parametro,String accion) throws GeneralSecurityException, IOException;
}
