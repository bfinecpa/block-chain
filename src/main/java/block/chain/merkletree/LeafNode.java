package block.chain.merkletree;

import block.chain.hash.SHA256;
import block.chain.transaction.Transaction;
import block.chain.transaction.TransactionDto;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class LeafNode implements Node {

    private TransactionDto transactionDto;

    private String hash;


    public LeafNode(TransactionDto transactionDto) throws NoSuchAlgorithmException, IOException {
        this.transactionDto = transactionDto;
        this.hash = SHA256.encrypt(transactionDto);
    }

    public String getHash() {
        return hash;
    }

    @Override
    public Node getLeft() {
        return null;
    }

    @Override
    public Node getRight() {
        return null;
    }

    public TransactionDto getTransactionDto() {
        return transactionDto;
    }

    public Transaction findTransaction() {
        return transactionDto.getTransaction();
    }
}
