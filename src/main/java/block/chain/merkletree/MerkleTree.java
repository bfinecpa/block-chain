package block.chain.merkletree;

import block.chain.transaction.Transaction;
import block.chain.transaction.TransactionDto;

import java.io.IOException;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class MerkleTree  implements Serializable {

    private Node root;


    public String getRootHashValue(){
        return root.getHash();
    }

    // 무조건 2^n개의 트랜잭션만 가져온다.
    public MerkleTree(List<TransactionDto> transactionDtos) throws NoSuchAlgorithmException, IOException {
        Queue<Node> dataSet = makeLeafNodes(transactionDtos);
        while(true){
            Node node1 = dataSet.poll();
            Node node2 = dataSet.poll();

            if(node2==null){
                root = node1;
                break;
            }

            NoneLeafNode noneLeafNode = new NoneLeafNode(node1, node2);
            dataSet.add(noneLeafNode);
        }
    }


    private Queue<Node> makeLeafNodes(List<TransactionDto> transactionDtos) throws NoSuchAlgorithmException, IOException {
        Queue<Node> dataSet = new LinkedList<>();
        for (TransactionDto transactionDto : transactionDtos) {
            LeafNode leafNode = new LeafNode(transactionDto);
            dataSet.add(leafNode);
        }
        return dataSet;
    }

    // 여기 고쳐야함
    public boolean validateTransaction(TransactionDto transactionDto) {
        Transaction curTransaction = transactionDto.getTransaction();
        Queue<Node> dataSet = new LinkedList<>();
        dataSet.add(root);
        while(true){
            Node curNode = dataSet.poll();
            if(curNode==null){
                return false;
            }
            Node left = curNode.getLeft();
            Node right = curNode.getRight();
            if(left != null){
                dataSet.add(left);
                dataSet.add(right);
                continue;
            }
            LeafNode curLeafNode = (LeafNode) curNode;
            Transaction transaction = curLeafNode.findTransaction();
            if(transaction.getIdentifier()==curTransaction.getIdentifier()
                    && transaction.getInput().equals(curTransaction.getOutput())
                    && transaction.compareImmutable(curTransaction)){
                return true;
            }
        }
    }

    public Transaction printValidateTransaction(TransactionDto transactionDto) {
        Transaction curTransaction = transactionDto.getTransaction();
        Queue<Node> dataSet = new LinkedList<>();
        dataSet.add(root);
        while(true){
            Node curNode = dataSet.poll();
            if(curNode==null){
                return null;
            }
            Node left = curNode.getLeft();
            Node right = curNode.getRight();
            if(left != null){
                dataSet.add(left);
                dataSet.add(right);
                continue;
            }
            LeafNode curLeafNode = (LeafNode) curNode;
            Transaction transaction = curLeafNode.findTransaction();
            if(transaction.getIdentifier()==curTransaction.getIdentifier()
                    && transaction.getInput().equals(curTransaction.getOutput())
                    && transaction.compareImmutable(curTransaction)){
                return transaction;
            }
        }
    }

    public Queue<Node> getLeafNode() {
        Queue<Node> dataSet = new LinkedList<>();
        dataSet.add(root);
        while(true){
            Node peek = dataSet.peek();
            if(peek.getLeft()==null){
                break;
            }
            Node curNode = dataSet.poll();
            Node left = curNode.getLeft();
            Node right = curNode.getRight();
            dataSet.add(left);
            dataSet.add(right);
        }
        return dataSet;
    }
}
