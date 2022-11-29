package block.chain.ecdsa;

import block.chain.tool.ChangeByteWIthObject;
import block.chain.transaction.Transaction;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.time.LocalDate;
import java.util.Base64;

public class DigiSig {

    private static final String SPEC = "secp256k1";
    private static final String ALGO = "SHA256withECDSA";

    ObjectMapper mapper = new ObjectMapper();

    private PublicKey publicKey;

    private PrivateKey privateKey;

    public String getPub(){
        return Base64.getEncoder().encodeToString(publicKey.getEncoded());
    }

    public void makeKey() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        ECGenParameterSpec ecSpec = new ECGenParameterSpec(SPEC);
        KeyPairGenerator g = KeyPairGenerator.getInstance("EC");
        g.initialize(ecSpec, new SecureRandom());
        KeyPair keypair = g.generateKeyPair();
        publicKey = keypair.getPublic();
        privateKey = keypair.getPrivate();
    }

    public String getSig(Transaction transaction) throws IOException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        byte[] bytes = ChangeByteWIthObject.convertObjectToBytes(transaction);
        Signature ecdsaSign = Signature.getInstance(ALGO);
        ecdsaSign.initSign(privateKey);
        ecdsaSign.update(bytes);
        byte[] signature = ecdsaSign.sign();
        return Base64.getEncoder().encodeToString(signature);
    }


    public JSONObject sender() throws NoSuchAlgorithmException, InvalidKeyException, IOException, SignatureException {

        String plaintext = "Hello";
        TestObject testObject = new TestObject();
        byte[] bytes = ChangeByteWIthObject.convertObjectToBytes(testObject);
        mapper.registerModule(new JavaTimeModule());
        String s = mapper.writeValueAsString(testObject);

        //...... sign
        Signature ecdsaSign = Signature.getInstance(ALGO);
        ecdsaSign.initSign(privateKey);
        ecdsaSign.update(bytes);
        byte[] signature = ecdsaSign.sign();
        String pub = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        String sig = Base64.getEncoder().encodeToString(signature);
        System.out.println(sig);
        System.out.println(pub);

        JSONObject obj = new JSONObject();
        obj.put("publicKey", pub);
        obj.put("signature", sig);
        obj.put("message", s);
        obj.put("algorithm", ALGO);

        return obj;
    }

    public boolean receiver(JSONObject obj) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, IOException, SignatureException {

        Signature ecdsaVerify = Signature.getInstance(obj.getString("algorithm"));
        KeyFactory kf = KeyFactory.getInstance("EC");

        EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(obj.getString("publicKey")));

        KeyFactory keyFactory = KeyFactory.getInstance("EC");
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

        String message = obj.getString("message");
        mapper.registerModule(new JavaTimeModule());
        TestObject testObject = mapper.readValue(message, TestObject.class);

        byte[] bytes = ChangeByteWIthObject.convertObjectToBytes(testObject);

        testObject.print();
        ecdsaVerify.initVerify(publicKey);
        ecdsaVerify.update(bytes);
        boolean result = ecdsaVerify.verify(Base64.getDecoder().decode(obj.getString("signature")));

        return result;
    }



}
class TestObject implements Serializable {
    private String a1;
    private int a2;
    private Long a3;
    private LocalDate localDate = LocalDate.now();


    public TestObject(){
        this.a1 = "testString";
        this.a2 = 1234;
        this.a3 = 4321L;
    }

    public void print(){
        System.out.println("a1 = " + a1);
        System.out.println("a2 = " + a2);
        System.out.println("a3 = " + a3);
    }

    public void setA1(String a1) {
        this.a1 = a1;
    }

    public void setA2(int a2) {
        this.a2 = a2;
    }

    public void setA3(Long a3) {
        this.a3 = a3;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }

    public String getA1() {
        return a1;
    }

    public int getA2() {
        return a2;
    }

    public Long getA3() {
        return a3;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }
}

