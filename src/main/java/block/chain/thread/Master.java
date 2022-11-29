package block.chain.thread;

import block.chain.Block;
import block.chain.Chain;
import block.chain.Trace;
import block.chain.merkletree.LeafNode;
import block.chain.merkletree.Node;
import block.chain.node.FullNode;
import block.chain.node.UserNode;
import block.chain.transaction.Transaction;
import block.chain.transaction.TransactionDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

public class Master implements Runnable{


    private List<FullNode> fullNodeList;


    public Master(List<FullNode> fullNodeList) {
        this.fullNodeList = fullNodeList;
    }

    @Override
    public void run() {
        while(true){
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            if(input.contains("snapshot myBlockChain")){
                if(input.contains("ALL")){
                    for (int i=0; i<fullNodeList.size(); i++){
                        System.out.println("F"+i+": ");
                        printFullNode(fullNodeList.get(i));
                    }
                }
                else if (input.equals("F")) {
                    int f = input.indexOf("F");
                    char c = input.charAt(f+1);
                    int numericValue = Character.getNumericValue(c);
                    FullNode fullNode = fullNodeList.get(numericValue);
                    printFullNode(fullNode);
                }
            }
            else if (input.contains("snapshot trPool")) {
                int f = input.indexOf("F");
                char c = input.charAt(f+1);
                int numericValue = Character.getNumericValue(c);
                FullNode fullNode = fullNodeList.get(numericValue);
                List<TransactionDto> transactionDtoList = new ArrayList<>(fullNode.getTransactionPool());
                for (TransactionDto transactionDto : transactionDtoList) {
                    System.out.println("TrId : " + transactionDto.getTrId());
                    System.out.println("Input : " + transactionDto.getTransaction().getInput());
                    System.out.println("Output : " + transactionDto.getTransaction().getOutput());
                    System.out.println("Identifier : " + transactionDto.getTransaction().getIdentifier());
                    System.out.println("ModelNo : " + transactionDto.getTransaction().getModelNo());
                    System.out.println("Manufactured Date : " + transactionDto.getTransaction().getManufacturedDate());
                    System.out.println("Trading Date : " + transactionDto.getTransaction().getTradingDate());
                    System.out.println("Others : " + transactionDto.getTransaction().getOthers());
                    System.out.println("--------------------------------------------------------------");
                }
            }
            else if (input.contains("verifyLastTr")){
                int f = input.indexOf("F");
                char c = input.charAt(f+1);
                int numericValue = Character.getNumericValue(c);
                FullNode fullNode = fullNodeList.get(numericValue);
                Block lastMiningBlock = fullNode.getLastMiningBlock();
                List<Node> leafNodes = new ArrayList<>(lastMiningBlock.getMerkleTree().getLeafNode());
                LeafNode leafNode = (LeafNode)leafNodes.get(leafNodes.size() - 1);
                TransactionDto transactionDto = leafNode.getTransactionDto();
                Transaction lastTransaction = fullNode.printValidation(transactionDto);
                System.out.println("last transaction’s output : " + lastTransaction.getOutput());
                System.out.println("trID’s input : " + transactionDto.getTransaction().getInput());
                System.out.println("last transaction’s " +
                        "\n Identifier : "+ lastTransaction.getIdentifier() +
                        "\n ModelNo : " + lastTransaction.getModelNo() +
                        "\n Manufactured Date : " + lastTransaction.getManufacturedDate() +
                        "\n Trading Date : " + lastTransaction.getTradingDate() +
                        "\n Others : " + lastTransaction.getOthers()
                );
                System.out.println("trID’s " +
                        "\n Identifier : "+ transactionDto.getTransaction().getIdentifier() +
                        "\n ModelNo : " + transactionDto.getTransaction().getModelNo() +
                        "\n Manufactured Date : " + transactionDto.getTransaction().getManufacturedDate() +
                        "\n Trading Date : " + transactionDto.getTransaction().getTradingDate() +
                        "\n Others : " + transactionDto.getTransaction().getOthers()
                );
                System.out.println("trID’s signature: " + transactionDto.getSig());
                System.out.println("verifying using trID’s input: " + transactionDto.getTransaction().getInput());
                System.out.println("verified successfully!!");
            }
            else if (input.contains("trace")) {
                int i = input.indexOf("<");
                int i1 = input.indexOf(">");
                String identifier = input.substring(i + 1, i1);
                FullNode fullNode = fullNodeList.get(0);
                Chain myLongestChain = fullNode.getMyLongestChain();
                List<Trace> traces = myLongestChain.printTransactionOfIdentifierAll(identifier);
                if(input.contains("ALL")){
                    for (Trace trace : traces) {
                        System.out.println("blockNo : "+ trace.getBlockNo()+" trId: " +
                                "\n Input : " + trace.getTransaction().getInput() +
                                "\n Output : " + trace.getTransaction().getOutput()+
                                "\n Identifier : " + trace.getTransaction().getIdentifier()+
                                "\n ModelNo : " + trace.getTransaction().getModelNo()+
                                "\n Manufactured Date : " +trace.getTransaction().getManufacturedDate()+
                                "\n Trading Date : " +trace.getTransaction().getTradingDate()+
                                "\n Others : " + trace.getTransaction().getOthers()
                        );

                    }
                }
                else{
                    char c = input.charAt(input.length() - 1);
                    int numericValue = Character.getNumericValue(c);
                    for (int j=0; j<numericValue; j++){
                        Trace trace = traces.get(j);
                        System.out.println("blockNo : "+ trace.getBlockNo()+" trId: " +
                                "\n Input : " + trace.getTransaction().getInput() +
                                "\n Output : " + trace.getTransaction().getOutput()+
                                "\n Identifier : " + trace.getTransaction().getIdentifier()+
                                "\n ModelNo : " + trace.getTransaction().getModelNo()+
                                "\n Manufactured Date : " +trace.getTransaction().getManufacturedDate()+
                                "\n Trading Date : " +trace.getTransaction().getTradingDate()+
                                "\n Others : " + trace.getTransaction().getOthers()
                        );
                    }
                }

            }

        }
    }

    private static void printFullNode(FullNode fullNode) {
        List<Block> blockChain = fullNode.getMyLongestChain().getBlockChain();
        for (int i=1; i<blockChain.size(); i++){
            System.out.print("blockNo : " + blockChain.get(i).getBlockNo());
            System.out.print("  |  ");
            System.out.print("prevHash : "+ blockChain.get(i).getPrevHash());
            System.out.print("  |  ");
            System.out.print("nonce : "+blockChain.get(i).getNonce());
            System.out.print("  |  ");
            System.out.println("Merkle-root : "+blockChain.get(i).getMerkleTreeRoot());
            Queue<Node> leafNodeQueue = blockChain.get(i).getMerkleTree().getLeafNode();
            List<Node> leafNode = new ArrayList<>(leafNodeQueue);
            for(int j=0; j<leafNode.size();j++){
                LeafNode poll = (LeafNode) leafNode.get(j);
                System.out.println("trId" + (j+1)+"번 : "+ poll.getTransactionDto().getTrId() );
            }

            System.out.println("--------------------------------------------------------------");
        }
    }
}
