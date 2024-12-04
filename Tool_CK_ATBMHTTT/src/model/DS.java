package model;

import java.security.*;
import java.util.Base64;

public class DS {
    public KeyPair keyPair;
    SecureRandom secureRandom;
    Signature signature;
    PublicKey publicKey;
    public PrivateKey privateKey;

    public DS(){

    }
    public DS(String alg, String algRandom, String prov) throws NoSuchAlgorithmException, NoSuchProviderException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance(alg, prov);
        secureRandom = SecureRandom.getInstance(algRandom, prov);
        generator.initialize(1024,secureRandom);
        keyPair = generator.genKeyPair();
        signature = Signature.getInstance(alg,prov);
    }

    public boolean genKey(){
        if(keyPair==null) return false;
        publicKey = keyPair.getPublic();
        privateKey = keyPair.getPrivate();
        return true;
    }
    public void loadKey(String key){

    }
    public String sign(String mess) throws InvalidKeyException, SignatureException {
        byte[] data= mess.getBytes();
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

//    public static void main(String[] args) throws SignatureException, InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException {
//        DS ds= new DS("DSA", "SHA1PRNG", "SUN");
//        ds.genKey();
//        String sign = ds.sign("helo");
//        System.out.println(sign);
//        System.out.println("Kiem tra chu ki:"+(ds.verify("helo",sign)?"":"KHONG ")+"HOP LE");
//    }
}