package block.chain;

import block.chain.merkletree.LeafNode;
import block.chain.merkletree.Node;
import block.chain.transaction.Transaction;
import block.chain.transaction.TransactionDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class Chain {

    private List<Block> blockChain = new ArrayList<>();
    private Block lastBlock;

    public List<Block> getBlockChain() {
        return blockChain;
    }

    public Chain() {

        Block block = new Block(0, 1L, null, null, null);
        blockChain.add(block);
        lastBlock=block;
    }

    public void addBlock(Block block){
        blockChain.add(block);
        lastBlock = block;
    }

    public List<Trace> printTransactionOfIdentifierAll(String identifier){
        List<Trace> history = new ArrayList<>();
        for(int i = blockChain.size()-1; i>=0; i--){
            if(i==0){
                return history;
            }
            Queue<Node> leafNodes = blockChain.get(i).getMerkleTree().getLeafNode();
            ArrayList<Node> nodes = new ArrayList<>(leafNodes);
            for (Node node : nodes) {
                LeafNode leafNode = (LeafNode) node;
                if(leafNode.getTransactionDto().getTransaction().getIdentifier().equals(Long.parseLong(identifier))){
                    history.add(new Trace(i,leafNode.getTransactionDto().getTransaction() ));
                }
            }
        }
        return history;
    }


    /**
     *
     * @param transactionDto
     * @return
     * 여기 고쳐야함
     * 새로운 트랜잭션을 full에게 보내는데 이때 검증할때 블록 생성이 안되서 i=0으로 주게됨
     * 그냥 검증을 마이닝 할때 하자 이거 말이 안된다.
     *
     */
    public boolean validateTransaction(TransactionDto transactionDto){
        for(int i = blockChain.size()-1; i>=0; i--){
            if(i==0){
                return false;
            }
            if(blockChain.get(i).getMerkleTree().validateTransaction(transactionDto)){
                return true;
            }
        }
        return false;
    }

    public Transaction printValidateTransaction(TransactionDto transactionDto){
        for(int i = blockChain.size()-1; i>=0; i--){
            if(i==0){
                return null;
            }
            Transaction transaction = blockChain.get(i).getMerkleTree().printValidateTransaction(transactionDto);
            if(transaction==null){
                continue;
            }
            return transaction;
        }
        return null;
    }

    public Block getLastBlock() {
        return lastBlock;
    }

    public Block findBlockByNo(int no){
        if(no==-1){
            return null;
        }
        return blockChain.get(no);
    }

    public void setBlock(int blockNo, Block blockFound) {
        blockChain.set(blockNo, blockFound);
    }
}
