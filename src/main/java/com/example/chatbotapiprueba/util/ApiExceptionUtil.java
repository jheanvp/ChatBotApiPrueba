package com.example.chatbotapiprueba.util;

import com.example.chatbotapiprueba.exception.ApiException;
import org.springframework.http.HttpStatus;


public class ApiExceptionUtil {

    public static ApiException lanzarApiExceptionSinResultados(String noEncontrado) {
        return new ApiException(HttpStatus.NOT_FOUND, "Sin resultados",
                "No se ha encontrado informacion para ".concat(noEncontrado));
    }

    public static ApiException lanzarApiExceptionPersonalizada(HttpStatus httpStatus, String cabecera, String detalle) {
        return new ApiException(httpStatus, cabecera, detalle);
    }

}
