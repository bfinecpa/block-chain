package block.chain.ecdsa;

import block.chain.Block;
import block.chain.hash.SHA256;
import block.chain.merkletree.MerkleTree;
import block.chain.tool.ChangeByteWIthObject;
import block.chain.transaction.Transaction;
import block.chain.transaction.TransactionDto;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;

import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class DigiSigTest {


    @Test
    public void test4() throws IOException {
        Long a = 12345L;
        Long b = 12345L;
        String c ="12345";

        long l = Long.parseLong(c);

        if(a.equals(l)){
            System.out.println("b = " + b);
        }
    }

    @Test
    public void test() throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, IOException, SignatureException, InvalidKeyException, InvalidKeySpecException {
        DigiSig digiSig = new DigiSig();
        digiSig.makeKey();
        JSONObject sender = digiSig.sender();
        Assertions.assertTrue(digiSig.receiver(sender));
    }

    @Test
    public void test2(){
        Random random = new Random();
        random.setSeed(System.currentTimeMillis());
        for(int i=0; i<100; i++){
            System.out.println("val = " + random.nextInt(4));
        }
    }

    @Test
    public void test1()  {
        Queue<TestObject> abc = new LinkedList<>();
        
        TestObject testObject = new TestObject();

        abc.add(testObject);

        TestObject peek = abc.peek();

        System.out.println("peek.getA1() = " + peek.getA1());
        TestObject poll = abc.poll();
        System.out.println("poll = " + poll.getA1());

    }


    @Test
    public void randomTest() throws IOException {

        Random random = new Random();
        random.setSeed(System.currentTimeMillis());
        long beforeTime = System.currentTimeMillis();
        for (int i=0; i<100; i++){
            long l = random.nextLong();
            System.out.println("l = " + l);
        }

    }

    @Test
    public void calculationByte() throws NoSuchAlgorithmException, IOException {
        String test1 = "helloworl";
        String test2 = "leagueoflegend";

        String encrypt = SHA256.encrypt(test1);
        System.out.println("encrypt = " + encrypt);

        String encrypt1 = SHA256.encrypt(test2);
        System.out.println("encrypt1 = " + encrypt1);

        System.out.println("encrypt = " + encrypt.length());
        System.out.println("encrypt1 = " + encrypt1.length());
        System.out.println("encrypt1.charAt(0) = " + encrypt1.charAt(0));
        /*for(int i=0; i<encrypt1.length()-1; i++){
            encrypt.
        }*/

    }


    @Test
    public void miningTimeTest() throws IOException, InvalidAlgorithmParameterException,
            NoSuchAlgorithmException, SignatureException, InvalidKeyException {

        DigiSig digiSig = new DigiSig();
        digiSig.makeKey();
        String pub = digiSig.getPub();

        Random random = new Random();
        random.setSeed(System.currentTimeMillis());

        List<TransactionDto> transactionDtoList = new ArrayList<>();

        for(int i=0; i<8; i++){
            Transaction transaction = new Transaction(pub, pub, random.nextLong(),
                    "test"+i, LocalDate.now(),LocalDate.now(),"test"+i);

            TransactionDto transactionDto = new TransactionDto(transaction, SHA256.encrypt(transaction),
                    digiSig.getSig(transaction));
            transactionDtoList.add(transactionDto);
        }

        MerkleTree merkleTree = new MerkleTree(transactionDtoList);

        Block block = new Block(1, 0L, null, merkleTree.getRootHashValue(), merkleTree);
        String prevHash = SHA256.encrypt(block);
        long beforeTime = System.currentTimeMillis();
        System.out.println("beforeTime = " + beforeTime);
        difficulty(random, merkleTree, prevHash);
        long afterTime = System.currentTimeMillis();
        System.out.println("afterTime = " + afterTime);
        long secDiffTime = (afterTime - beforeTime)/1000;
        System.out.println("시간차이(s) : "+secDiffTime);
    }

    private static void difficulty(Random random, MerkleTree merkleTree, String prevHash) throws NoSuchAlgorithmException, IOException {
        while(true){
            long nonce = random.nextLong();
            Block nextBlock = new Block(1, nonce, prevHash, merkleTree.getRootHashValue(), merkleTree);
            String encrypt = SHA256.encrypt(nextBlock);
            for(int i=0; i<64; i++){
                if(i==5){
                    return;
                }else{
                    if(encrypt.charAt(i)!='0'){
                        break;
                    }
                }
            }
        }
    }
}