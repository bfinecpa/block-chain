package block.chain.merkletree;

import block.chain.hash.SHA256;

import java.io.IOException;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;

public class NoneLeafNode implements Node {

    private int mateNumber;
    private Node left;
    private Node right;

    private String data;

    private String hash;

    public NoneLeafNode(Node left, Node right) throws NoSuchAlgorithmException, IOException {
        this.left = left;
        this.right = right;
        this.data = left.getHash()+right.getHash();
        this.hash = SHA256.encrypt(data);
    }


    @Override
    public String getHash() {
        return hash;
    }

    @Override
    public Node getLeft() {
        return left;
    }

    @Override
    public Node getRight() {
        return right;
    }


}
