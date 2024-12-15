package utils;
import javax.crypto.Cipher;
import java.io.*;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class ElecSignRSA {
        private static PublicKey publicKey;
        private static PrivateKey privateKey;

        public void generateKeyPair() {
            try {
                SecureRandom sr = new SecureRandom();
                KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
                kpg.initialize(4096, sr);

                KeyPair kp = kpg.genKeyPair();
                publicKey = kp.getPublic();
                privateKey = kp.getPrivate();

                File privateKeyFile = createKeyFile(new File("C:/Users/Public/privateKey.rsa"));

                // phuong thuc uploadPublicKeyToServer()
                File publicKeyFile = createKeyFile(new File("C:/Users/Public/publicKey.rsa"));
                FileOutputStream fos = new FileOutputStream(publicKeyFile);
                fos.write(publicKey.getEncoded());
                fos.close();
//            uploadPublicKeyToServer();


                // luu Private key tai local
                fos = new FileOutputStream(privateKeyFile);
                fos.write(privateKey.getEncoded());
                fos.close();

                System.out.println("Tạo Key Thành Công");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // sua lai upload to Server
        public static boolean uploadPublicKeyToServer(String pubKey){
//        try {
//            FileInputStream fis = new FileInputStream(pubKey);
//            byte[] b = new byte[fis.available()];
//            fis.read(b);
//            fis.close();
//
//            X509EncodedKeySpec spec = new X509EncodedKeySpec(b);
//            KeyFactory factory = KeyFactory.getInstance("RSA");
//            publicKey = factory.generatePublic(spec);
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        } catch (NoSuchAlgorithmException e) {
//            throw new RuntimeException(e);
//        } catch (InvalidKeySpecException e) {
//            throw new RuntimeException(e);
//        }
            return true;
        }
        // luu mac dinh tai local
        public static boolean loadPrivateKey(String priKey){
            try {
                FileInputStream fis = new FileInputStream(priKey);
                byte[] b = new byte[fis.available()];
                fis.read(b);
                fis.close();

                PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(b);
                KeyFactory factory = KeyFactory.getInstance("RSA");
                privateKey = factory.generatePrivate(spec);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            } catch (InvalidKeySpecException e) {
                throw new RuntimeException(e);
            }
            return true;
        }

        public static File createKeyFile(File file) throws IOException {
            if (!file.exists()) {
                file.createNewFile();
            } else {
                file.delete();
                file.createNewFile();
            }
            return file;
        }

        public static String encryption(String sign){
            String strEncrypt = null;
            try {
                Cipher c = Cipher.getInstance("RSA");
                c.init(Cipher.ENCRYPT_MODE, privateKey);
                byte encryptOut[] = c.doFinal(sign.getBytes());
                strEncrypt = Base64.getEncoder().encodeToString(encryptOut);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return strEncrypt;
        }


        public static String decryption(String encryptSign){
            byte decryptOut[] = null;
            try {
                Cipher c = Cipher.getInstance("RSA");
                c.init(Cipher.DECRYPT_MODE, publicKey);
                decryptOut = c.doFinal(Base64.getDecoder().decode(encryptSign));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return new String(decryptOut);
        }

    public static boolean verifySignature(String data, String signature) {
        try {
            Signature sig = Signature.getInstance("SHA256withRSA");
            sig.initVerify(publicKey);
            sig.update(data.getBytes());

            byte[] decodedSignature = Base64.getDecoder().decode(signature);
            return sig.verify(decodedSignature);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    }

