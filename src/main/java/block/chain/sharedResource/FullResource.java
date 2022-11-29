package block.chain.sharedResource;

import block.chain.Block;
import block.chain.transaction.TransactionDto;

import java.util.LinkedList;
import java.util.Queue;

public class FullResource {
    private Queue<TransactionDto> transferTransactionToFull = new LinkedList<>();

    private Queue<Block> blocks = new LinkedList<>();



    public void addTransactionToFull(TransactionDto transactionDto){
        transferTransactionToFull.add(transactionDto);
    }

    public void addBlock(Block block){
        blocks.add(block);
    }


    public Queue<TransactionDto> getTransferTransactionToFull() {
        return transferTransactionToFull;
    }

    public Queue<Block> getBlocks() {
        return blocks;
    }
}
