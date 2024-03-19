package com.example.chatbotapiprueba.services.Impl;

import com.example.chatbotapiprueba.services.IGoogleSheetsService;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

@Service
public class GoogleSheetsServiceImpl implements IGoogleSheetsService {
    @Value("${google.token.actualizacion}")
    public String tokenActualizacion;
    @Value("${google.token}")
    public String token;

    private static final String APPLICATION_NAME = "Google Sheets API Java Quickstart";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    private static final List<String> SCOPES =
            Collections.singletonList(SheetsScopes.SPREADSHEETS_READONLY);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";
    public int encontrarFila(Object[][] matriz, String id) {
        for (int i = 1; i < matriz.length; i++) {
            if (matriz[i][0].equals(id)) {
                return i;
            }
        }
        return -1;
    }

    public String obtenerInformacion(int fila, Object[][] matriz, String id,Integer columna) {
        String fechaProporcionada;
        String respuesta;
        String resultadoBusqueda= (String) matriz[fila][columna];
        if (columna.equals(3) && resultadoBusqueda != null && !resultadoBusqueda.isEmpty()) {
            respuesta = "El estado para el pedido *" + id + "* es : *" + resultadoBusqueda+"* ⏳ y\n"+
                    "se encuentra en este estado desde el *" + matriz[fila][4] +"* "+ "\uD83D\uDE9A";
        } else if (columna.equals(4) && resultadoBusqueda != null && !resultadoBusqueda.isEmpty()) {
            fechaProporcionada = resultadoBusqueda;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
            LocalDate fecha = LocalDate.parse(fechaProporcionada, formatter);
            long diasTranscurridos = ChronoUnit.DAYS.between(fecha, LocalDate.now());
            respuesta = "El pedido *" + id + "* lleva *" +diasTranscurridos+"* ⏳ días en proceso";
        } else if (columna.equals(6) && resultadoBusqueda != null && !resultadoBusqueda.isEmpty()) {
            respuesta = "El Comentario para el pedido *" + id + "* es : *" + resultadoBusqueda+"* \uD83D\uDCAC ";
        }else {
            respuesta = "No se encontró información para el pedido *" + id+"*";
        }
        return respuesta;
    }
    @Override
    public String busquedaEstado(String id) throws GeneralSecurityException, IOException {
        Object[][] matrizData = getDataSheet();
        int fila = encontrarFila(matrizData, id);
        if (fila==-1){
            return "El pedido *" + id+"* no existe en la base de datos";
        }
        return obtenerInformacion(fila, matrizData, id,3);
    }

    @Override
    public String busquedaDemora(String id) throws GeneralSecurityException, IOException {
        Object[][] matrizData = getDataSheet();
        int fila = encontrarFila(matrizData, id);
        if (fila==-1){
            return "El pedido *" + id+"* no existe en la base de datos";
        }
        return obtenerInformacion(fila, matrizData, id,4);
    }
    @Override
    public String busquedaComentario(String id) throws GeneralSecurityException, IOException {
        Object[][] matrizData = getDataSheet();
        int fila = encontrarFila(matrizData, id);
        if (fila==-1){
            return "El pedido *" + id+"* no existe en la base de datos";
        }
        return obtenerInformacion(fila, matrizData, id,6);
    }



    private Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {

        InputStream in = GoogleSheetsServiceImpl.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();

        GoogleCredential credential = new GoogleCredential.Builder()
                .setJsonFactory(JSON_FACTORY)
                .setTransport(HTTP_TRANSPORT)
                .setClientSecrets(clientSecrets)
                .build();

        credential.setAccessToken(token);
        credential.setRefreshToken(tokenActualizacion);

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
    public Object[][] getDataSheet() throws GeneralSecurityException, IOException {
        int numRows;
        int maxNumCols;
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        final String spreadsheetId = "1QFp2jjezMXGsiH_LjTNr60zCt3HJ7OW1zTTVu8S-80s";
        final String range = "pedidos!A1:H134";
        Sheets service =
                new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                        .setApplicationName(APPLICATION_NAME)
                        .build();
        ValueRange response = service.spreadsheets().values()
                .get(spreadsheetId, range)
                .execute();
        List<List<Object>> values = response.getValues();
        Object[][] matrizData = new Object[0][];
        if (values == null || values.isEmpty()) {
            System.out.println("no hay datos");
        } else {
            numRows = values.size();
            maxNumCols = 0;
            for (List<Object> row : values) {
                if (row.size() > maxNumCols) {
                    maxNumCols = row.size();
                }
            }
            matrizData = new Object[numRows][maxNumCols];
            for (int i = 0; i < numRows; i++) {
                List<Object> row = values.get(i);
                for (int j = 0; j < row.size(); j++) {
                    matrizData[i][j] = row.get(j);
                }
            }
        }
        return matrizData;
    }

    public  String obtenerurl(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        InputStream in = GoogleSheetsServiceImpl.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        //LocalServerReceiver receiver = new LocalServerReceiver.Builder().setHost("").setPort(8889).build();
        //LocalServerReceiver receiver = new LocalServerReceiver.Builder().setHost("chatbotapiprueba.azurewebsites.net").setPort(443).setProtocol("https").build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder()
                .setHost("chatbotapiprueba.azurewebsites.net")
                .setPort(443)  // Puerto HTTPS predeterminado
                .build();

        //LocalServerReceiver receiver = new LocalServerReceiver.Builder().setHost("chatbotapiprueba.azurewebsites.net").build();
        return flow.newAuthorizationUrl().setRedirectUri(receiver.getRedirectUri()).build() ;
    }
}








