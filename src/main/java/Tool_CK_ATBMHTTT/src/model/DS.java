package Tool_CK_ATBMHTTT.src.model;

import java.security.*;
import java.util.Base64;

public class DS {
    public KeyPair keyPair;
    SecureRandom secureRandom;
    Signature signature;
    PublicKey publicKey;
    public PrivateKey privateKey;

    public DS() {
    }

    public DS(String alg, String algRandom) throws NoSuchAlgorithmException, NoSuchProviderException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance(alg);
        secureRandom = SecureRandom.getInstance(algRandom);
        generator.initialize(2048, secureRandom);
        keyPair = generator.genKeyPair();
        signature = Signature.getInstance("SHA256withRSA");
    }

    public boolean genKey() {
        if (keyPair == null) return false;
        publicKey = keyPair.getPublic();
        privateKey = keyPair.getPrivate();
        return true;
    }

    public void loadKey(String key) {

    }

    public String sign(String mess) throws InvalidKeyException, SignatureException {
        byte[] data = mess.getBytes();
        signature.initSign(privateKey);
        signature.update(data);
        byte[] sign = signature.sign();
        return Base64.getEncoder().encodeToString(sign);
    }

    public boolean verify(String mes, String sign) throws SignatureException, InvalidKeyException {
        signature.initVerify(publicKey);
        byte[] data = mes.getBytes();
        byte[] signValue = Base64.getDecoder().decode(sign);
        signature.update(data);
        return signature.verify(signValue);
    }

    public static void main(String[] args) throws SignatureException, InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException {
        DS rsa = new DS("RSA", "SHA1PRNG");
        rsa.genKey();
        String sign = rsa.sign("helo");
        System.out.println(sign);
        System.out.println("Kiem tra chu ki:" + (rsa.verify("hElo", sign) ? "" : "KHONG ") + "HOP LE");
    }
}
