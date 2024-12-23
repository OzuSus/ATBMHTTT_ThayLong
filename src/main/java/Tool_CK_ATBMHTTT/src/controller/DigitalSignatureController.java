package Tool_CK_ATBMHTTT.src.controller;

import java.security.*;
import java.util.Base64;
import javax.crypto.Cipher;

public class DigitalSignatureController {

    private KeyPair keyPair;

    public void generateKeyPair(int keySize) throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(keySize);
        keyPair = keyPairGenerator.generateKeyPair();
    }

    public String getPublicKey() {
        return Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
    }

    public String getPrivateKey() {
        return Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());
    }

    public String signData(String plainText) throws Exception {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(keyPair.getPrivate());
        signature.update(plainText.getBytes());
        byte[] digitalSignature = signature.sign();
        return Base64.getEncoder().encodeToString(digitalSignature);
    }

    public boolean verifySignature(String plainText, String signatureText) throws Exception {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initVerify(keyPair.getPublic());
        signature.update(plainText.getBytes());
        byte[] digitalSignature = Base64.getDecoder().decode(signatureText);
        return signature.verify(digitalSignature);
    }
}
