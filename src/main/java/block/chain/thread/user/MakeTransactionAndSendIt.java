package block.chain.thread.user;

import block.chain.node.UserNode;
import block.chain.sharedResource.FullResource;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

public class MakeTransactionAndSendIt implements Runnable{

    private UserNode userNode;

    private FullResource fullResource;

    public MakeTransactionAndSendIt(UserNode userNode,  FullResource fullResource) {
        this.userNode = userNode;
        this.fullResource =fullResource;
    }

    @Override
    public void run() {
        for(int i=0; i<3; i++){
            try {
                fullResource.addTransactionToFull(userNode.makeFirstTransaction(i));
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (SignatureException e) {
                throw new RuntimeException(e);
            } catch (InvalidKeyException e) {
                throw new RuntimeException(e);
            }
        }
        while(true){
            try {
                fullResource.addTransactionToFull(userNode.makeTransaction());
                Thread.sleep(5000);
            } catch (InvalidKeyException e) {
                throw new RuntimeException(e);
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (SignatureException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}