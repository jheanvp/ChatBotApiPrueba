package com.example.chatbotapiprueba.services.Impl;


import com.example.chatbotapiprueba.response.DialogflowResponse;
import com.example.chatbotapiprueba.response.FulfillmentMessages;
import com.example.chatbotapiprueba.response.Text;
import com.example.chatbotapiprueba.services.IDialogflowService;
import com.example.chatbotapiprueba.services.IGoogleSheetsService;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
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
    public DialogflowResponse obtenerEstado(String id) throws GeneralSecurityException, IOException {
        String respuesta = googleSheetsService.busquedaEstado(id);

        List<FulfillmentMessages> listFull = new ArrayList<>();
        listFull.add(crearRespuesta(respuesta));
        listFull.add(crearRespuesta("Para regresar al menu principal escribe *menú*"));
        DialogflowResponse dialogFlowResponse = new DialogflowResponse();
        dialogFlowResponse.setFulfillmentMessages(listFull);
        return dialogFlowResponse;
    }

    @Override
    public DialogflowResponse obtenerDiasTranscurridos(String id) throws GeneralSecurityException, IOException {
        String respuesta = googleSheetsService.busquedaDemora(id);
        List<FulfillmentMessages> listFull = new ArrayList<>();
        listFull.add(crearRespuesta(respuesta));
        listFull.add(crearRespuesta("Para regresar al menu principal escribe *menú*"));
        DialogflowResponse dialogFlowResponse = new DialogflowResponse();
        dialogFlowResponse.setFulfillmentMessages(listFull);
        return dialogFlowResponse;
    }

    @Override
    public DialogflowResponse obtenerComentario(String id) throws GeneralSecurityException, IOException {
        String respuesta = googleSheetsService.busquedaComentario(id);
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

    @Override
    public DialogflowResponse obtenerUrl() throws GeneralSecurityException, IOException {
        List<FulfillmentMessages> listFull = new ArrayList<>();
        listFull.add(crearRespuesta(googleSheetsService.obtenerurl(GoogleNetHttpTransport.newTrustedTransport())));
        DialogflowResponse dialogFlowResponse = new DialogflowResponse();
        dialogFlowResponse.setFulfillmentMessages(listFull);
        return dialogFlowResponse;
    }
}
