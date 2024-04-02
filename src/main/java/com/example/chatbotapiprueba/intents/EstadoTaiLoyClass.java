package com.example.chatbotapiprueba.intents;

import com.example.chatbotapiprueba.response.DialogflowResponse;
import com.example.chatbotapiprueba.services.IDialogflowService;
import com.example.chatbotapiprueba.services.IntentHandler;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;

@Service
public class EstadoTaiLoyClass implements IntentHandler {
    private final IDialogflowService dialogflowService;

    public EstadoTaiLoyClass(IDialogflowService dialogflowService) {
        this.dialogflowService = dialogflowService;
    }
    @Override
    public DialogflowResponse handleIntent(String id,String accion) throws GeneralSecurityException, IOException {
      return dialogflowService.obtenerEstado(id,accion);
    }
}
