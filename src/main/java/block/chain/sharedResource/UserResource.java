package block.chain.sharedResource;

import block.chain.transaction.TransactionDto;

import java.util.LinkedList;
import java.util.Queue;

public class UserResource {

    private Queue<TransactionDto> transferTransactionToUser = new LinkedList<>();

    public void addTransactionToUser(TransactionDto transactionDto){
        transferTransactionToUser.add(transactionDto);
    }

    public Queue<TransactionDto> getTransferTransactionToUser() {
        return transferTransactionToUser;
    }
}
