package block.chain.thread.full;

import block.chain.Block;
import block.chain.node.FullNode;
import block.chain.sharedResource.FullResource;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;

public class MineBlockAndSendIt implements Runnable {

    private FullNode fullNode;

    private List<FullResource> otherFullResource = new ArrayList<>();

    public MineBlockAndSendIt(FullNode fullNode , FullResource ...otherResources) {
        this.fullNode = fullNode;
        for (FullResource otherResource : otherResources) {
            otherFullResource.add(otherResource);
        }
    }



    @Override
    public void run() {
        try {
            while(true){
                Block mining = fullNode.mining();
                fullNode.consensusLongestChain(mining);
                for (FullResource fullResource : otherFullResource) {
                    fullResource.addBlock(mining);
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
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
