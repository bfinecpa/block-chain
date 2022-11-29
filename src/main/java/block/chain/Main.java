package block.chain;

import java.security.*;
import java.util.ArrayList;
import java.util.List;

import block.chain.node.FullNode;
import block.chain.node.UserNode;
import block.chain.sharedResource.FullResource;
import block.chain.sharedResource.UserResource;
import block.chain.thread.Master;
import block.chain.thread.full.MineBlockAndSendIt;
import block.chain.thread.full.FullReceiveTransaction;
import block.chain.thread.full.ReceiveBlockAndConsensus;
import block.chain.thread.user.MakeTransactionAndSendIt;
import block.chain.thread.user.UserReceiveTransaction;

public class Main {
    public static void main(String[] args) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException {


        /**
         * user, full 생성
         */
        //user0, full1
        FullNode fullNode0 = new FullNode();
        UserNode userNode0 = new UserNode();
        //user1, full1
        FullNode fullNode1 = new FullNode();
        UserNode userNode1 = new UserNode();


        /**
         * master 생성
         */
        Master master = new Master(List.of(fullNode0, fullNode1));


        /**
         * 유저 pubKey 모음
         */
        List<String> pubKeyList = new ArrayList<>();
        //연결
        pubKeyList.add(userNode0.getPublicKey());
        userNode0.setUsersPublicKeyList(pubKeyList);
        pubKeyList.add(userNode1.getPublicKey());
        userNode1.setUsersPublicKeyList(pubKeyList);


        /**
         * 데이터 전송을 위한 공유메모리 생성
         */
        //user0, full0
        FullResource full0Resource = new FullResource();
        UserResource user0Resource = new UserResource();
        //user1, full1
        FullResource full1Resource = new FullResource();
        UserResource user1Resource = new UserResource();


        /**
         * FullReceiveTransaction, MineBlockAndSendIt 나 말고 연결된 풀 노드를 적어 파라미터로 넘겨야 한다.
         * FullReceiveTransaction의 의미는 FullReceiveTransactionAndSendIt이다.
         */

        /**
         * user0, full0
         */
        MakeTransactionAndSendIt user0MakeTransactionAndSendIt = new MakeTransactionAndSendIt(userNode0,full0Resource);
        FullReceiveTransaction full0ReceiveTransaction = new FullReceiveTransaction(fullNode0, full0Resource, user0Resource, full1Resource);
        MineBlockAndSendIt full0MineBlockAndSendIt = new MineBlockAndSendIt(fullNode0, full1Resource);
        UserReceiveTransaction user0ReceiveTransaction = new UserReceiveTransaction(userNode0, user0Resource);
        ReceiveBlockAndConsensus full0ReceiveBlock = new ReceiveBlockAndConsensus(full0Resource, fullNode0);

        /**
         * user1, full0
         */
        MakeTransactionAndSendIt user1MakeTransactionAndSendIt = new MakeTransactionAndSendIt(userNode1, full1Resource);
        FullReceiveTransaction full1ReceiveTransaction = new FullReceiveTransaction(fullNode1, full1Resource,user1Resource,full0Resource);
        MineBlockAndSendIt full1MineBlockAndSendIt = new MineBlockAndSendIt(fullNode1 ,full0Resource);
        UserReceiveTransaction user1ReceiveTransaction = new UserReceiveTransaction(userNode1,user1Resource);
        ReceiveBlockAndConsensus full1ReceiveBlock = new ReceiveBlockAndConsensus(full1Resource, fullNode1);


        /**
         * 실행
         */
        new Thread(user0MakeTransactionAndSendIt).start();
        new Thread(user0ReceiveTransaction).start();
        new Thread(full0ReceiveTransaction).start();
        new Thread(full0MineBlockAndSendIt).start();
        new Thread(full0ReceiveBlock).start();

        new Thread(master).start();

        new Thread(user1MakeTransactionAndSendIt).start();
        new Thread(user1ReceiveTransaction).start();
        new Thread(full1ReceiveTransaction).start();
        new Thread(full1MineBlockAndSendIt).start();
        new Thread(full1ReceiveBlock).start();



    }
}
