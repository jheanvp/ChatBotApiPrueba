package com.example.chatbotapiprueba.services;

import java.io.IOException;
import java.security.GeneralSecurityException;

public interface IGoogleSheetsService {
    String busquedaEstado(String id) throws GeneralSecurityException, IOException;
    String busquedaDemora(String id) throws GeneralSecurityException, IOException;

    String busquedaComentario(String id) throws GeneralSecurityException, IOException;
}
