package block.chain.thread.user;

import block.chain.node.UserNode;
import block.chain.sharedResource.UserResource;
import block.chain.transaction.TransactionDto;

import java.util.Queue;

public class UserReceiveTransaction implements Runnable{

    private UserNode userNode;
    private UserResource userResource;

    public UserReceiveTransaction(UserNode userNode, UserResource userResource) {
        this.userNode = userNode;
        this.userResource = userResource;
    }


    @Override
    public void run() {
        Queue<TransactionDto> transferTransaction = userResource.getTransferTransactionToUser();

        while(true){
            TransactionDto transactionDto = transferTransaction.poll();
            if(transactionDto==null){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }else{
                userNode.readTransaction(transactionDto);
            }
        }
    }
}
