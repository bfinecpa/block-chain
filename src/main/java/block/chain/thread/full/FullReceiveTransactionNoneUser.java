package block.chain.thread.full;

import block.chain.node.FullNode;
import block.chain.sharedResource.FullResource;
import block.chain.sharedResource.UserResource;
import block.chain.transaction.TransactionDto;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class FullReceiveTransactionNoneUser implements Runnable{

    private FullNode fullNode;

    private FullResource myFullResource;

    private List<FullResource> otherFullResource = new ArrayList<>();

    public FullReceiveTransactionNoneUser(FullNode fullNode, FullResource myFullResource, FullResource ...otherResource) {
        this.fullNode = fullNode;
        this.myFullResource = myFullResource;
        for (FullResource fullResource : otherResource) {
            otherFullResource.add(fullResource);
        }
    }

    @Override
    public void run() {
        Queue<TransactionDto> transferTransaction = myFullResource.getTransferTransactionToFull();
        while(true){
            TransactionDto transactionDto = transferTransaction.poll();
            if(transactionDto==null){
                try {
                    System.out.println("Full이 받을 트랜잭션이 없어서 1초 쉬는중 ");
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }else{
                try {
                    if(fullNode.receiveTransactionAndTransfer(transactionDto)){
                        for (FullResource fullResource : otherFullResource) {
                            fullResource.addTransactionToFull(transactionDto);
                        }
                    }
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                } catch (InvalidKeySpecException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (SignatureException e) {
                    throw new RuntimeException(e);
                } catch (InvalidKeyException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
