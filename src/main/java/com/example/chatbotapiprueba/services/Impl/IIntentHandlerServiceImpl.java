package com.example.chatbotapiprueba.services.Impl;

import com.example.chatbotapiprueba.handler.IntentHandler;
import com.example.chatbotapiprueba.request.DialogflowRequest;
import com.example.chatbotapiprueba.response.DialogflowResponse;
import com.example.chatbotapiprueba.services.IAuthService;
import com.example.chatbotapiprueba.services.IDialogflowService;
import com.example.chatbotapiprueba.services.IIntentHandlerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Map;

@AllArgsConstructor
@Service
public class IIntentHandlerServiceImpl implements IIntentHandlerService {


    private final Map<String, IntentHandler> intentHandlers;
    private final IDialogflowService dialogflowService;
    private final IAuthService authService;

    @Override
    public DialogflowResponse check(DialogflowRequest request) throws GeneralSecurityException, IOException {
        boolean respuesta = authService.authentication(request.getResponseId());
        if (respuesta){
            if (intentHandlers.containsKey(request.getQueryResult().getIntent().getDisplayName())) {
                return intentHandlers.get(request.getQueryResult()
                                                 .getIntent()
                                                 .getDisplayName())
                                     .handleIntent(request.getQueryResult()
                                                          .getParameters()
                                                          .getCodigo(), request.getQueryResult()
                                                                               .getAction());
            } else {
                return dialogflowService.error("Esta opcion no esta configurada, porfavor intente otra vez");
            }
        }
        else {
            return dialogflowService.error("Error de autenticacion");
        }
    }
}
