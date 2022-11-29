package block.chain.node;

import block.chain.hash.SHA256;
import block.chain.tool.ChangeByteWIthObject;
import block.chain.transaction.Transaction;
import block.chain.transaction.TransactionDto;

import java.io.IOException;
import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.time.LocalDate;
import java.util.*;

public class UserNode {
    private static final String SPEC = "secp256k1";
    private static final String ALGO = "SHA256withECDSA";
    private String publicKey;
    private PrivateKey privateKey;
    private List<String> usersPublicKeyList;

    private String name;

    private Queue<Transaction> transactionHaving = new LinkedList<>();


    public void setName(String name) {
        this.name = name;
    }

    public void setUsersPublicKeyList(List<String> usersPublicKeyList) {
        this.usersPublicKeyList = usersPublicKeyList;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public UserNode() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        ECGenParameterSpec ecSpec = new ECGenParameterSpec(SPEC);
        KeyPairGenerator g = KeyPairGenerator.getInstance("EC");
        g.initialize(ecSpec, new SecureRandom());
        KeyPair keypair = g.generateKeyPair();
        publicKey = Base64.getEncoder().encodeToString(keypair.getPublic().getEncoded());
        privateKey = keypair.getPrivate();
    }

    public TransactionDto makeFirstTransaction(int number) throws InvalidKeyException, NoSuchAlgorithmException, IOException, SignatureException {
        Random random = new Random();
        random.setSeed(System.currentTimeMillis());

        Transaction transaction = new Transaction(publicKey, getRandomPubKey(),  random.nextLong(), name+"'s item_"+number,
                LocalDate.now(), LocalDate.now(), "first");

        Signature ecdsaSign = Signature.getInstance(ALGO);
        ecdsaSign.initSign(privateKey);

        byte[] bytes = ChangeByteWIthObject.convertObjectToBytes(transaction);
        ecdsaSign.update(bytes);
        byte[] signature = ecdsaSign.sign();

        String sig = Base64.getEncoder().encodeToString(signature);
        String trId = SHA256.encrypt(transaction);


        return new TransactionDto(transaction, trId, sig);
    }


    public TransactionDto makeTransaction() throws InvalidKeyException, NoSuchAlgorithmException, IOException, SignatureException, InterruptedException {
        Random random = new Random();
        random.setSeed(System.currentTimeMillis());

        Transaction transactionFound;
        while(true){
            transactionFound = transactionHaving.poll();
            if(transactionFound==null){
                Thread.sleep(1000);
                continue;
            }
            break;
        }
        Transaction transaction = new Transaction(publicKey, getRandomPubKey(),transactionFound.getIdentifier(), transactionFound.getModelNo(),
                transactionFound.getManufacturedDate(), LocalDate.now(), "used");

        Signature ecdsaSign = Signature.getInstance(ALGO);
        ecdsaSign.initSign(privateKey);

        byte[] bytes = ChangeByteWIthObject.convertObjectToBytes(transaction);
        ecdsaSign.update(bytes);
        byte[] signature = ecdsaSign.sign();

        String sig = Base64.getEncoder().encodeToString(signature);
        String trId = SHA256.encrypt(transaction);

        return new TransactionDto(transaction, trId, sig);
    }

    public void readTransaction(TransactionDto transactionDto){
        if(publicKey.equals(transactionDto.getTransaction().getOutput())){
            transactionHaving.add(transactionDto.getTransaction());
        }
    }

    /**
     * 바꿔야함
     */
    private String getRandomPubKey(){
        int size = usersPublicKeyList.size();
        Random random = new Random();
        random.setSeed(System.currentTimeMillis());
        return usersPublicKeyList.get(random.nextInt(size));
    }




}
