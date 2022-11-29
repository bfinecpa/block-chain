package block.chain.sharedResource;

import block.chain.transaction.TransactionDto;

import java.util.LinkedList;
import java.util.Queue;

public class ResourceTransferredFromUserToFull {
    private Queue<TransactionDto> transferTransactionToFull = new LinkedList<>();
    private Queue<TransactionDto> transferTransactionToUser = new LinkedList<>();


    public void addTransactionToFull(TransactionDto transactionDto){
        transferTransactionToFull.add(transactionDto);
    }

    public void addTransactionToUser(TransactionDto transactionDto){
        transferTransactionToUser.add(transactionDto);
    }

    public Queue<TransactionDto> getTransferTransactionToFull() {
        return transferTransactionToFull;
    }

    public Queue<TransactionDto> getTransferTransactionToUser() {
        return transferTransactionToUser;
    }
}

