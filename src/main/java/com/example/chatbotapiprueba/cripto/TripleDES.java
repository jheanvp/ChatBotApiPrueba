package com.example.chatbotapiprueba.cripto;

import com.example.chatbotapiprueba.util.ApiExceptionUtil;
import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpStatus;


import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class TripleDES {

    private static Cipher cipher;

    public static String desencriptaDatos(int tipoEnc, byte[] tdesKeyData, String datos) {
        String result = "";
        try {
            SecretKeySpec myKey;
            Base64 decoder;
            byte[] raw;
            byte[] stringBytes;
            switch (tipoEnc) {
                case 1:
                    cipher = Cipher.getInstance("DESEDE/ECB/PKCS5Padding");
                    myKey = new SecretKeySpec(tdesKeyData, "DESede");
                    cipher.init(2, myKey);
                    decoder = new Base64();
                    raw = decoder.decodeBase64(datos);
                    stringBytes = cipher.doFinal(raw);
                    result = new String(stringBytes, "UTF8");
                    break;
            }
        } catch (Exception exception) {
            throw ApiExceptionUtil.lanzarApiExceptionPersonalizada(HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrio un error",
                    exception.getMessage());
        }
        return result;
    }

}
