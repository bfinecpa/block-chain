package block.chain.node;

import block.chain.Block;
import block.chain.Chain;
import block.chain.hash.SHA256;
import block.chain.merkletree.MerkleTree;
import block.chain.tool.ChangeByteWIthObject;
import block.chain.transaction.Transaction;
import block.chain.transaction.TransactionDto;

import java.io.IOException;
import java.security.*;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

public class FullNode {

    private List<Block> blockPool = new ArrayList<>();

    private Queue<TransactionDto> transactionPool = new LinkedList<>();


    private Chain myLongestChain = new Chain();

    private Block lastMiningBlock;

    public Block getLastMiningBlock() {
        return lastMiningBlock;
    }

    public Chain getMyLongestChain() {
        return myLongestChain;
    }

    public Queue<TransactionDto> getTransactionPool() {
        return transactionPool;
    }

    public void addBlockInBlockPool(Block block){
        blockPool.add(block);
    }


    public void consensusLongestChain(Block block) throws NoSuchAlgorithmException, IOException {
        Block lastBlock = myLongestChain.getLastBlock();

        int blockNo = block.getBlockNo();
        String encrypt = SHA256.encrypt(lastBlock);
        if(encrypt.equals(block.getPrevHash()) && blockNo==lastBlock.getBlockNo()+1){
            myLongestChain.addBlock(block);
            System.out.println("체인에 연결했습니다.");
            return;
        }
        if(blockNo==lastBlock.getBlockNo()+1){
            myLongestChain.addBlock(block);
            changeChainBlock();
        }
    }


    /**
     *
     *  Block blockFound = findBlockByNoByPrevHash(curBlock.getBlockNo()-1, curBlock.getPrevHash());
     *  myLongestChain.setBlock(blockFound.getBlockNo(), blockFound);
     *  여기 고쳐야함 NullPointException 발생할 수도 있음
     *
     */
    private void changeChainBlock() throws NoSuchAlgorithmException, IOException {
        Block curBlock = myLongestChain.getLastBlock();
        while(true){
            Block prevBlock = myLongestChain.findBlockByNo(curBlock.getBlockNo() - 1);
            if(prevBlock==null && prevBlock.getBlockNo()==0){
                break;
            }
            if(SHA256.encrypt(prevBlock).equals(curBlock.getPrevHash())){
                break;
            }
            Block blockFound = findBlockByNoByPrevHash(curBlock.getBlockNo()-1, curBlock.getPrevHash());
            myLongestChain.setBlock(blockFound.getBlockNo(), blockFound);
            curBlock=blockFound;
        }
    }

    private Block findBlockByNoByPrevHash(int blockNo, String prevHash) throws NoSuchAlgorithmException, IOException {
        for (Block block : blockPool) {
            if(SHA256.encrypt(block).equals(prevHash) && block.getBlockNo()==blockNo){
                return block;
            }
        }
        return null;
    }


    public Block mining() throws NoSuchAlgorithmException, InvalidKeySpecException, IOException, SignatureException, InvalidKeyException, InterruptedException {
        List<TransactionDto> transactionDtoValidated = new ArrayList<>();
        while(true){
            if(transactionDtoValidated.size()==8){
                break;
            }
            TransactionDto poll = transactionPool.poll();
            if(poll==null){
                System.out.println("마이닝하는데 받은 트랜잭션이 없어서 1초 쉽니다.");
                Thread.sleep(1000);
                continue;
            }
            if(validateTransaction(poll)){
                transactionDtoValidated.add(poll);
            }
        }
        MerkleTree merkleTree = new MerkleTree(transactionDtoValidated);
        Block lastBlock = myLongestChain.getLastBlock();
        int blockNo = lastBlock.getBlockNo() + 1;
        String prevBlockHash = SHA256.encrypt(lastBlock);
        Random random = new Random();
        random.setSeed(System.currentTimeMillis());
        Block blockMined = difficulty(random, merkleTree, prevBlockHash, blockNo);
        System.out.println(blockMined.getBlockNo()+"번 블록을 채굴하였습니다.");
        lastMiningBlock = blockMined;
        return blockMined;
    }

    private Block difficulty(Random random, MerkleTree merkleTree, String prevHash, int blockNo) throws NoSuchAlgorithmException, IOException {
        while(true){
            long nonce = random.nextLong();
            Block nextBlock = new Block(blockNo, nonce, prevHash, merkleTree.getRootHashValue(), merkleTree);
            String encrypt = SHA256.encrypt(nextBlock);
            for(int i=0; i<64; i++){
                if(i==4){
                    return nextBlock;
                }else{
                    if(encrypt.charAt(i)!='0'){
                        break;
                    }
                }
            }
        }
    }

    public boolean validateTransaction(TransactionDto transactionDto) throws NoSuchAlgorithmException, InvalidKeySpecException,
            IOException, SignatureException, InvalidKeyException {

        /**
         * 1) 트랜젹션을 통해 판매하려는 물품의 최종 소유자 즉 합의된 마지막 판매의 구매자가 현재 트랜잭션의 input과 같은지
         * 2) immutable field 가 그 전과 같은지
         * 3) 서명 검사
         */
        if(transactionDto.getTransaction().getOthers().equals("first")){
            return true;
        }

        if(validateLastSellerAndImmutableField(transactionDto) && verifySign(transactionDto)){
            return true;
        }
        return false;
    }

    private boolean verifySign(TransactionDto transactionDto) throws NoSuchAlgorithmException, InvalidKeySpecException,
            InvalidKeyException, IOException, SignatureException {

        Transaction transaction = transactionDto.getTransaction();
        String pub = transaction.getInput();

        Signature ecdsaVerify = Signature.getInstance("SHA256withECDSA");
        EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(pub));
        KeyFactory keyFactory = KeyFactory.getInstance("EC");
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
        ecdsaVerify.initVerify(publicKey);
        byte[] bytes = ChangeByteWIthObject.convertObjectToBytes(transaction);
        ecdsaVerify.update(bytes);
        return ecdsaVerify.verify(Base64.getDecoder().decode(transactionDto.getSig()));
    }

    /**
     * 트랜젹션을 통해 판매하려는 물품의 최종 소유자 즉 합의된 마지막 판매의 구매자가 현재 트랜잭션의 input과 같은지
     * 이느 물품을 알아야 하는데 이는 물품의 identifier을 알고 있어야 한다.
     */
    private boolean validateLastSellerAndImmutableField(TransactionDto transactionDto) {
        return myLongestChain.validateTransaction(transactionDto);
    }

    public Transaction printValidation(TransactionDto transactionDto){
        return myLongestChain.printValidateTransaction(transactionDto);
    }


    public boolean receiveTransactionAndTransfer(TransactionDto transactionDto) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException, SignatureException, InvalidKeyException {
        if(!transactionPool.contains(transactionDto)){
            transactionPool.add(transactionDto);
            System.out.println("풀노드가 없었던 트랜잭션을 받았습니다.");
            return true;
        }
        return false;
    }
}
