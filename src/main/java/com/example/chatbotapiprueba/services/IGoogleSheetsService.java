package com.example.chatbotapiprueba.services;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

public interface IGoogleSheetsService {
    String BuscarValorEnFila(String parametro, String indiceItem) throws GeneralSecurityException, IOException;
}
