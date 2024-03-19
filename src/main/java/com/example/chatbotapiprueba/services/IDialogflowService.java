package com.example.chatbotapiprueba.services;



import com.example.chatbotapiprueba.response.DialogflowResponse;

import java.io.IOException;
import java.security.GeneralSecurityException;

public interface IDialogflowService {
    DialogflowResponse obtenerEstado (String id) throws GeneralSecurityException, IOException;
    DialogflowResponse error();
    DialogflowResponse obtenerUrl() throws GeneralSecurityException, IOException;
    DialogflowResponse obtenerDiasTranscurridos(String id)throws GeneralSecurityException, IOException;

    DialogflowResponse obtenerComentario(String id) throws GeneralSecurityException, IOException;

}
