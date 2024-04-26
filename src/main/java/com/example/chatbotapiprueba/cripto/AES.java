package com.example.chatbotapiprueba.cripto;

import com.example.chatbotapiprueba.util.ApiExceptionUtil;
import org.springframework.http.HttpStatus;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class AES {

    private final static String ENCRIPT_ALGORIT = "AES";

    public static String desencriptar(String encrypted, String key) {
        byte[] decrypted = null;
        try {
            byte[] raw = Base64.getDecoder().decode(key);
            SecretKeySpec skeySpec = new SecretKeySpec(raw, AES.ENCRIPT_ALGORIT);
            Cipher cipher = Cipher.getInstance(ENCRIPT_ALGORIT);
            cipher.init(2, skeySpec);
            decrypted = cipher.doFinal(Base64.getDecoder().decode(encrypted));
        } catch (Exception exception) {
            throw ApiExceptionUtil.lanzarApiExceptionPersonalizada(HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrio un error",
                    exception.getMessage());
        }
        return new String(decrypted);
    }

}
