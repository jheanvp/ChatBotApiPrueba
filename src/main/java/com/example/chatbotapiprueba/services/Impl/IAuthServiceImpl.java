package com.example.chatbotapiprueba.services.Impl;

import com.example.chatbotapiprueba.services.IAuthService;
import com.example.chatbotapiprueba.util.Util;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class IAuthServiceImpl implements IAuthService {

    @Value("${response.pasword}")
    private String pass;
    @Value("${google.token.actualizacion}")

    @Override
    public Boolean authentication(String passSesion) {
         return passSesion.substring(passSesion.length() - 8)
                .equals(Util.getEncryptedText(pass).substring(Util.getEncryptedText(pass).length() - 8));
    }
}