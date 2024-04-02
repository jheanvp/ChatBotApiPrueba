package com.example.chatbotapiprueba.services.Impl;


import com.example.chatbotapiprueba.response.DialogflowResponse;
import com.example.chatbotapiprueba.response.FulfillmentMessages;
import com.example.chatbotapiprueba.response.Text;
import com.example.chatbotapiprueba.services.IDialogflowService;
import com.example.chatbotapiprueba.services.IGoogleSheetsService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class DialogflowServiceImpl implements IDialogflowService {

    private final IGoogleSheetsService googleSheetsService;

    public DialogflowServiceImpl(IGoogleSheetsService googleSheetsService) {
        this.googleSheetsService = googleSheetsService;
    }

    //101
    @Override
    public DialogflowResponse obtenerEstado(String id,String accion) throws GeneralSecurityException, IOException {
        return getDialogflowResponse(id, accion);
    }

    @Override
    public DialogflowResponse obtenerDiasTranscurridos(String id,String accion) throws GeneralSecurityException, IOException {
        return getDialogflowResponse(id, accion);
    }
    @Override
    public DialogflowResponse obtenerComentario(String id,String accion) throws GeneralSecurityException, IOException {
        return getDialogflowResponse(id, accion);
    }
    private DialogflowResponse getDialogflowResponse(String id, String accion) throws GeneralSecurityException, IOException {
        String respuesta = googleSheetsService.BuscarValorEnFila(id,accion);
        List<FulfillmentMessages> listFull = new ArrayList<>();
        listFull.add(crearRespuesta(respuesta));
        listFull.add(crearRespuesta("Para regresar al menu principal escribe *menú*"));
        DialogflowResponse dialogFlowResponse = new DialogflowResponse();
        dialogFlowResponse.setFulfillmentMessages(listFull);
        return dialogFlowResponse;
    }

    private FulfillmentMessages crearRespuesta(String message) {
        FulfillmentMessages fulfillment = new FulfillmentMessages();
        Text text = new Text();
        text.setText(Collections.singletonList(message));
        fulfillment.setText(text);
        return fulfillment;
    }

    @Override
    public DialogflowResponse error() {
        List<FulfillmentMessages> listFull = new ArrayList<>();
        listFull.add(crearRespuesta("Esta Opcion no está Configurada, porfavor contactarse con ffierro@cajapiura.pe"));
        DialogflowResponse dialogFlowResponse = new DialogflowResponse();
        dialogFlowResponse.setFulfillmentMessages(listFull);
        return dialogFlowResponse;
    }

}
