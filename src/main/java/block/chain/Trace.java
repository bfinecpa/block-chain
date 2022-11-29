package block.chain;

import block.chain.transaction.Transaction;
import block.chain.transaction.TransactionDto;

public class Trace {

    private int blockNo;

    private Transaction transaction;

    public Trace(int blockNo, Transaction transaction) {
        this.blockNo = blockNo;
        this.transaction = transaction;
    }

    public int getBlockNo() {
        return blockNo;
    }

    public Transaction getTransaction() {
        return transaction;
    }
}
