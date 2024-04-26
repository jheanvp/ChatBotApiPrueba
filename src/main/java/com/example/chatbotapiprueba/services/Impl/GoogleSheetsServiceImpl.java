package com.example.chatbotapiprueba.services.Impl;

import com.example.chatbotapiprueba.services.IGoogleSheetsService;
import static com.example.chatbotapiprueba.util.Constants.*;

import com.example.chatbotapiprueba.util.Util;
import com.google.api.client.auth.oauth2.Credential;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.security.GeneralSecurityException;
import java.sql.SQLOutput;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

@Service
public class GoogleSheetsServiceImpl implements IGoogleSheetsService {
    @Value("${google.token.actualizacion}")
    private String tokenactualizacion;
    @Value("${google.token}")
    public String token;

    private static final String APPLICATION_NAME = "Google Sheets API Java Quickstart";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    private static final List<String> SCOPES =
            Collections.singletonList(SheetsScopes.SPREADSHEETS_READONLY);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    private Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        InputStream in = GoogleSheetsServiceImpl.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
        GoogleCredential credential = new GoogleCredential.Builder()
                .setJsonFactory(JSON_FACTORY)
                .setTransport(HTTP_TRANSPORT)
                .setClientSecrets(clientSecrets)
                .build();

        credential.setAccessToken(Util.getEncryptedText(token));
        credential.setRefreshToken(Util.getEncryptedText(tokenactualizacion));

        try {
            credential.refreshToken();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        System.out.println("Credenciales existentes:");
//        System.out.println("Token de acceso: " + credential.getAccessToken());
//        System.out.println("Token de actualización: " + credential.getRefreshToken());
        return credential;
    }
    public Sheets ObtenerSheet() throws GeneralSecurityException, IOException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        return  new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public List<Object> obtenerFila(String parametro) throws GeneralSecurityException, IOException {
        final String spreadsheetId = "1QFp2jjezMXGsiH_LjTNr60zCt3HJ7OW1zTTVu8S-80s";
        final String range = "pedidos!A1:J134";
        ValueRange rowResponse = null;
        Sheets service = ObtenerSheet();
        ValueRange response = service.spreadsheets().values()
                .get(spreadsheetId, range)
                .execute();
        List<List<Object>> values = response.getValues();
        boolean found = false;
        if (values != null && !values.isEmpty()) {
            for (int i = 0; i < values.size(); i++) {
                List<Object> row = values.get(i);
                if ( row != null &&!row.isEmpty() && row.get(0).toString().equals(parametro)) {
                    String fullRange = "pedidos!A" + (i + 1) + ":Z" + (i + 1);
                     rowResponse = service.spreadsheets().values()
                            .get(spreadsheetId, fullRange)
                            .execute();
                    found = true;
                    break;
                }
            }
        }
        if (!found) {
            return Collections.emptyList();
        }
        return rowResponse.getValues().get(0);
    }

    @Override
    public String BuscarValorEnFila(String parametro,String indiceItems) throws GeneralSecurityException, IOException {
        int indiceItem= Integer.parseInt(indiceItems);
        DateTimeFormatter formatter;
        String fechaEncontrada;
        String respuesta = "Aun no existe informacion para el pedido *"+parametro+"* porfavor intente en unos minutos";
        List<Object> fila = obtenerFila(parametro);
        System.out.println(fila.size());
        if (!fila.isEmpty()) {
            if (indiceItem >= 0) {
                switch (indiceItem) {
                    case ESTADO:
                        if (fila.get(ESTADO).toString() != null && !fila.get(ESTADO).toString().isEmpty() &&
                                fila.get(FECHA_ESTADO).toString() != null && !fila.get(FECHA_ESTADO).toString().isEmpty()) {
                            respuesta = "El estado para el pedido *" + parametro +
                                    "* es : *" + fila.get(ESTADO).toString() + "* ⏳ y\n" +
                                    "se encuentra en este estado desde el *" + fila.get(FECHA_ESTADO).toString() +
                                    "* " + "\uD83D\uDE9A";
                            break;
                        } else if (fila.get(ESTADO).toString() != null && !fila.get(ESTADO).toString().isEmpty() && (
                                fila.get(FECHA_ESTADO).toString() == null || fila.get(FECHA_ESTADO).toString().isEmpty())) {
                            respuesta = "El estado para el pedido *" + parametro +
                                    "* es : *" + fila.get(ESTADO).toString() + "* ⏳ y\n" +
                                    "aun no tenemos informacion acerca de la fecha " + "\uD83D\uDE9A";
                            break;

                        } else if (fila.get(FECHA_ESTADO).toString() != null || !fila.get(FECHA_ESTADO).toString().isEmpty() && (
                                fila.get(ESTADO).toString() == null && fila.get(ESTADO).toString().isEmpty())) {
                            respuesta = "Aun no se tiene informacion sobre el estado para el pedido *" + parametro +
                                    "*" + "⏳ y\n" +
                                    "Pero inicio proceso el *" + fila.get(FECHA_ESTADO).toString() +
                                    "* " + "\uD83D\uDE9A";
                            break;
                        }else {
                            respuesta = "Aun no existe informacion de estado o fecha para el pedido*"+parametro+
                                    "*, intente en unos minutos";
                        }
                        break;
                    case FECHA_ESTADO:
                        if (fila.get(indiceItem).toString() != null && !fila.get(indiceItem).toString().isEmpty()){
                            fechaEncontrada = fila.get(FECHA_ESTADO).toString();
                            formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
                            LocalDate fecha = LocalDate.parse(fechaEncontrada, formatter);
                            long diasTranscurridos = ChronoUnit.DAYS.between(fecha, LocalDate.now());
                            respuesta = "El pedido *" + parametro + "* lleva *" + diasTranscurridos + "* ⏳ días en proceso";
                            break;
                        }else {
                            respuesta = "Aun no existe informacion de estado o fecha para el pedido*"+parametro+
                                    "*, intente en unos minutos";
                        }
                        break;
                    case COMENTARIO:
                        if (fila.get(indiceItem).toString() != null && !fila.get(indiceItem).toString().isEmpty()){
                            respuesta = "El Comentario para el pedido *" + parametro +
                                    "* es : *" + fila.get(COMENTARIO).toString() + "* \uD83D\uDCAC ";
                        }else {
                            respuesta = "Aun no existe un comentario para el pedido*"+parametro+
                                    "*, intente en unos minutos";
                        }
                        break;
                    case REPUESTA_PEDIDO:
                        if (fila.get(indiceItem).toString() != null && !fila.get(indiceItem).toString().isEmpty()){
                            respuesta = "La informacion para el pedido *" + parametro +
                                    "* es : *" + fila.get(REPUESTA_PEDIDO).toString() + "* \uD83D\uDCAC ";
                        }else {
                            respuesta = "Aun no existe un comentario para el pedido*"+parametro+
                                    "*, intente en unos minutos";
                        }
                        break;
                    default:
                        respuesta="No existe esta accion, porfavor revisar nombre de accion en *Dialogflow*";
                }
            } //else {
                //return "Este campo no existe en la base de datos";
            //}
        }else {
            respuesta="El pedido *"+parametro+"* no existe en la base datos";
        }
        return respuesta;
    }
}
