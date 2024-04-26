package com.example.chatbotapiprueba.controller;

import com.example.chatbotapiprueba.request.DialogflowRequest;
import com.example.chatbotapiprueba.services.IIntentHandlerService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@AllArgsConstructor
@RestController
@RequestMapping("/prueba")
public class ControllerRest {
    private final IIntentHandlerService IIntentHandlerService;

    @PostMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity dialogFlow(@RequestBody DialogflowRequest request) throws GeneralSecurityException, IOException {

        return ResponseEntity.status(HttpStatus.OK).body(IIntentHandlerService.check(request));
    }

//    @GetMapping("/prueba3")
//    public String mensaje4() throws GeneralSecurityException, IOException {
//        DialogflowResponse dialogFlowResponse;
//        dialogFlowResponse = intentHandlers.get("estadoTaiLoyClass").handleIntent("2345","3");
//        return dialogFlowResponse.getFulfillmentMessages().get(0).getText().getText().get(0);
//    }
//
//    @GetMapping("/prueba4/{parametro}/{accion}")
//    public String mensaje5(@PathVariable String parametro,@PathVariable String accion) throws GeneralSecurityException, IOException {
//        return googleSheetsService.BuscarValorEnFila(parametro,accion);
//    }
}
