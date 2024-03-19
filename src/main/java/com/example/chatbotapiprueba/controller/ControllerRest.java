package com.example.chatbotapiprueba.controller;

import com.example.chatbotapiprueba.request.DialogflowRequest;
import com.example.chatbotapiprueba.response.DialogflowResponse;
import com.example.chatbotapiprueba.services.IDialogflowService;
import com.example.chatbotapiprueba.services.IGoogleSheetsService;
import com.example.chatbotapiprueba.services.IntentHandler;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/prueba")
public class ControllerRest {
    private final Map<String, IntentHandler> intentHandlers;
    private final IDialogflowService dialogflowService;
    private final IGoogleSheetsService googleSheetsService;
    @Autowired
    public ControllerRest(Map<String, IntentHandler> intentHandlers, IDialogflowService dialogflowService, IGoogleSheetsService googleSheetsService) {
        this.intentHandlers = intentHandlers;
        this.dialogflowService = dialogflowService;
        this.googleSheetsService = googleSheetsService;
    }
    @PostMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity dialogFlow(@RequestBody DialogflowRequest request) throws GeneralSecurityException, IOException {
        String accion = request.getQueryResult().getAction();
        String intent = request.getQueryResult().getIntent().getDisplayName();
        String parametro = request.getQueryResult().getParameters().getCodigo();
        DialogflowResponse dialogFlowResponse;
        //
        if (intentHandlers.containsKey(intent)) {
            dialogFlowResponse = intentHandlers.get(intent).handleIntent(parametro);
        } else {
            dialogFlowResponse = dialogflowService.error();
        }
        //
        return ResponseEntity.status(HttpStatus.OK).body(dialogFlowResponse);
    }


    @GetMapping("/prueba2")
    public String mensaje3() throws GeneralSecurityException, IOException {
        return googleSheetsService.obtenerurl(GoogleNetHttpTransport.newTrustedTransport());
    }


    @GetMapping("/prueba3")
    public String mensaje4() throws GeneralSecurityException, IOException {
        DialogflowResponse dialogFlowResponse;
        dialogFlowResponse = intentHandlers.get("estadoTaiLoyClass").handleIntent("2345");
        return dialogFlowResponse.getFulfillmentMessages().get(0).getText().getText().get(0);
    }

}
