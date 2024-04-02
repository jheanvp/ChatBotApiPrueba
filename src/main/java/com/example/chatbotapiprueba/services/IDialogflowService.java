package com.example.chatbotapiprueba.services;

import com.example.chatbotapiprueba.response.DialogflowResponse;

import java.io.IOException;
import java.security.GeneralSecurityException;

public interface IDialogflowService {
    DialogflowResponse obtenerEstado (String id,String accion) throws GeneralSecurityException, IOException;
    DialogflowResponse error();
    DialogflowResponse obtenerDiasTranscurridos(String id,String accion)throws GeneralSecurityException, IOException;

    DialogflowResponse obtenerComentario(String id,String accion) throws GeneralSecurityException, IOException;

}
