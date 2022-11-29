package block.chain.thread.full;

import block.chain.Block;
import block.chain.sharedResource.FullResource;

import java.util.ArrayList;
import java.util.List;

public class SendBlock implements Runnable{

    private Block block ;

    private List<FullResource> otherFullResource = new ArrayList<>();


    public SendBlock(Block block, List<FullResource> otherFullResource) {
        this.block = block;
        this.otherFullResource = otherFullResource;
    }

    @Override
    public void run() {

    }
}
