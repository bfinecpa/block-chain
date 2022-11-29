package block.chain.thread.full;

import block.chain.Block;
import block.chain.node.FullNode;
import block.chain.sharedResource.FullResource;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class ReceiveBlockAndConsensus implements Runnable{

    private  FullResource fullResource;

    private FullNode fullNode;

    private List<FullResource> otherFullResources = new ArrayList<>();


    public ReceiveBlockAndConsensus(FullResource fullResource, FullNode fullNode ,FullResource ...otherResource) {
        this.fullResource = fullResource;
        this.fullNode = fullNode;
        for (FullResource resource : otherResource) {
            otherFullResources.add(resource);
        }
    }

    @Override
    public void run() {
        Queue<Block> blocks = fullResource.getBlocks();
        while(true){
            Block blockReceived = blocks.poll();
            if(blockReceived==null){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }else {
                try {
                    List<Block> blockPool = fullNode.getBlockPool();
                    if(blockPool.contains(blockReceived)){
                        continue;
                    }
                    fullNode.addBlockInBlockPool(blockReceived);
                    //다른 full node에게 보내 줘야함
                    for (FullResource otherFullResource : otherFullResources) {
                        otherFullResource.addBlock(blockReceived);
                    }
                    //확인
                    fullNode.consensusLongestChain(blockReceived);
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
