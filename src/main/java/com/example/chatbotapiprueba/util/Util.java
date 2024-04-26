package com.example.chatbotapiprueba.util;

import com.example.chatbotapiprueba.cripto.AES;
import com.example.chatbotapiprueba.cripto.TripleDES;
import org.springframework.http.HttpStatus;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class Util {

    public static Date convertirStringFechaDate(String fecha) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(fecha);
        } catch (ParseException exception) {
            throw ApiExceptionUtil.lanzarApiExceptionPersonalizada(HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrio un error",
                    "Error al convertir tipo de dato fecha : " + exception.getMessage());
        }
    }

    public static String getEncryptedText(String sPassEnc) {
        String sExtracto = sPassEnc.trim().substring(sPassEnc.length() - 24);
        String key = sPassEnc.trim().substring(0, 24);
        String sTexto = sPassEnc.trim().substring(24, sPassEnc.trim().length() - 24);
        String clave = AES.desencriptar(sTexto, key);
        return TripleDES.desencriptaDatos(1, sExtracto.getBytes(), clave);
    }

    public static Integer getInteger(String value) {
        Integer valueInteger = null;
        if (!Objects.isNull(value) && !value.isEmpty()) {
            try {
                valueInteger = Integer.parseInt(value);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return valueInteger;
    }

}
