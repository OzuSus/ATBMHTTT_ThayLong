package utils;

import java.security.*;
import java.util.Base64;

public class RSAKeyUtil {

    public static KeyPair generateKeyPair() throws NoSuchAlgorithmException{
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(4096);
        return keyPairGenerator.generateKeyPair();
    }
    public static String encodeKeyToBase64(PublicKey publicKey){
        return Base64.getEncoder().encodeToString(publicKey.getEncoded());
    }
    public static String encodeKeyToBase64(PrivateKey privateKey){
        return Base64.getEncoder().encodeToString(privateKey.getEncoded());
    }
}
