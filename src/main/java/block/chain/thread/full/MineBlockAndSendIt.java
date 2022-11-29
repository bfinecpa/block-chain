package block.chain.thread.full;

import block.chain.Block;
import block.chain.node.FullNode;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;

public class MineBlockAndSendIt implements Runnable {

    private FullNode fullNode;

    public MineBlockAndSendIt(FullNode fullNode) {
        this.fullNode = fullNode;
    }

    @Override
    public void run() {
        try {
            while(true){
                Block mining = fullNode.mining();
                fullNode.consensusLongestChain(mining);

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
