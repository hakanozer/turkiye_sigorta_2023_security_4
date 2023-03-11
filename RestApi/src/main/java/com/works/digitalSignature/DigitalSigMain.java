package com.works.digitalSignature;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.json.JSONObject;

public class DigitalSigMain {

    private static final String spec = "secp256k1";
    private static final String algo = "SHA256withECDSA";

    // sender
    public JSONObject sender() throws Exception {
        JSONObject obj = new JSONObject();

        ECGenParameterSpec ecGenParameterSpec = new ECGenParameterSpec(spec);
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC");
        keyPairGenerator.initialize(ecGenParameterSpec, new SecureRandom());
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        String plainText = "security data";
        Map<String, Object> hm = new HashMap<>();
        hm.put("status", true);
        hm.put("name", "Ali Bilmem");
        JSONObject objHm = new JSONObject(hm);

        Signature signature = Signature.getInstance(algo);
        signature.initSign(privateKey);
        signature.update(plainText.getBytes("UTF-8"));
        byte[] singBytes = signature.sign();

        String pub = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        String sig = Base64.getEncoder().encodeToString(singBytes);

        obj.put("publicKey", pub);
        obj.put("signature", sig);
        obj.put("message", plainText);
        obj.put("algo", algo);
        obj.put("datas", objHm);
        System.out.println( obj );

        return obj;
    }


    public boolean receiver ( JSONObject obj ) throws Exception {

        Signature signature = Signature.getInstance(obj.getString("algo"));
        KeyFactory keyFactory = KeyFactory.getInstance("EC");

        EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(obj.getString("publicKey")));
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

        signature.initVerify(publicKey);
        signature.update(obj.getString("message").getBytes("UTF-8"));

        boolean status = signature.verify(Base64.getDecoder().decode(obj.getString("signature")));

        if ( status ) {
            JSONObject ob = obj.getJSONObject("datas");
            System.out.println(ob);
        }else {
            System.err.println("Data or Signature Fail");
        }

        return status;
    }


    public static void main(String[] args) throws Exception {
        DigitalSigMain sigMain = new DigitalSigMain();
        sigMain.receiver(sigMain.sender());
    }

}
