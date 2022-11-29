package block.chain;

import block.chain.merkletree.MerkleTree;

import java.io.Serializable;
import java.util.Random;

public class Block  implements Serializable {

    //header
    private int blockNo;


    private Long nonce;


    private String prevHash;


    private String merkleTreeRoot;

    //body
    //머클트리 데이터를 갖고 있어야함
    private MerkleTree merkleTree;

    public MerkleTree getMerkleTree() {
        return merkleTree;
    }



    public Block(int blockNo, Long nonce, String prevHash, String merkleTreeRoot, MerkleTree merkleTree) {
        this.blockNo = blockNo;
        this.nonce = nonce;
        this.prevHash = prevHash;
        this.merkleTreeRoot = merkleTreeRoot;
        this.merkleTree = merkleTree;
    }

    /*public Block mining(Long blockNo, String prevHash, String merkleTreeRoot, MerkleTree merkleTree){
        Random random = new Random();
        random.setSeed(System.currentTimeMillis());
        long l = random.nextLong();

    }*/
    public int getBlockNo() {
        return blockNo;
    }

    public Long getNonce() {
        return nonce;
    }

    public String getPrevHash() {
        return prevHash;
    }

    public String getMerkleTreeRoot() {
        return merkleTreeRoot;
    }

    public void printBlockMined(){

    }

}
