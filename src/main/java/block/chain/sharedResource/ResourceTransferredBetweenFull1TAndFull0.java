package block.chain.sharedResource;

import block.chain.transaction.TransactionDto;

import java.util.LinkedList;
import java.util.Queue;

public class ResourceTransferredBetweenFull1TAndFull0 {

    private Queue<TransactionDto> transferTransactionToFull0 = new LinkedList<>();

    private Queue<TransactionDto> transferTransactionToFull1 = new LinkedList<>();


    public void addTransactionToFull1(TransactionDto transactionDto){
        transferTransactionToFull0.add(transactionDto);
    }

    public void addTransactionToUser(TransactionDto transactionDto){
        transferTransactionToFull1.add(transactionDto);
    }

    public Queue<TransactionDto> getTransferTransactionToFull0() {
        return transferTransactionToFull0;
    }

    public Queue<TransactionDto> getTransferTransactionToFull1() {
        return transferTransactionToFull1;
    }
    
}
