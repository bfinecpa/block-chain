package block.chain;

import java.security.*;
import java.util.ArrayList;
import java.util.List;

import block.chain.node.FullNode;
import block.chain.node.UserNode;
import block.chain.sharedResource.FullResource;
import block.chain.sharedResource.UserResource;
import block.chain.thread.Master;
import block.chain.thread.full.FullReceiveTransactionNoneUser;
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
        //user0, full0
        FullNode fullNode0 = new FullNode();
        UserNode userNode0 = new UserNode();
        userNode0.setName("User0");
        //user1, full1
        FullNode fullNode1 = new FullNode();
        UserNode userNode1 = new UserNode();
        userNode1.setName("User1");
        //user2, full2
        FullNode fullNode2 = new FullNode();
        UserNode userNode2 = new UserNode();
        userNode2.setName("User2");
        //user3, full3
        FullNode fullNode3 = new FullNode();
        UserNode userNode3 = new UserNode();
        userNode3.setName("User3");
        //full4, full5
        FullNode fullNode4 = new FullNode();
        FullNode fullNode5 = new FullNode();


        /**
         * master 생성
         */
        Master master = new Master(List.of(fullNode0, fullNode1, fullNode2, fullNode3, fullNode4, fullNode5));


        /**
         * 유저 pubKey 모음
         */
        List<String> pubKeyList = new ArrayList<>();
        //user 추가
        pubKeyList.add(userNode0.getPublicKey());
        pubKeyList.add(userNode1.getPublicKey());
        pubKeyList.add(userNode2.getPublicKey());
        pubKeyList.add(userNode3.getPublicKey());
        //user 마다 연결
        userNode0.setUsersPublicKeyList(pubKeyList);
        userNode1.setUsersPublicKeyList(pubKeyList);
        userNode2.setUsersPublicKeyList(pubKeyList);
        userNode3.setUsersPublicKeyList(pubKeyList);

        /**
         * 데이터 전송을 위한 공유메모리 생성
         */
        //user0, full0
        FullResource full0Resource = new FullResource();
        UserResource user0Resource = new UserResource();
        //user1, full1
        FullResource full1Resource = new FullResource();
        UserResource user1Resource = new UserResource();
        //user2, full2
        FullResource full2Resource = new FullResource();
        UserResource user2Resource = new UserResource();
        //user3, full3
        FullResource full3Resource = new FullResource();
        UserResource user3Resource = new UserResource();
        //full4, full5
        FullResource full4Resource = new FullResource();
        FullResource full5Resource = new FullResource();


        /**
         * FullReceiveTransaction, MineBlockAndSendIt 나 말고 연결된 풀 노드를 적어 파라미터로 넘겨야 한다.
         * FullReceiveTransaction의 의미는 FullReceiveTransactionAndSendIt이다.
         */

        /**
         * user0, full1
         */
        MakeTransactionAndSendIt user0MakeTransactionAndSendIt = new MakeTransactionAndSendIt(userNode0,full1Resource);
        UserReceiveTransaction user0ReceiveTransaction = new UserReceiveTransaction(userNode0, user0Resource);

        FullReceiveTransaction full1ReceiveTransaction = new FullReceiveTransaction(fullNode1, full1Resource, user0Resource,
                full0Resource, full2Resource, full3Resource, full4Resource);
        MineBlockAndSendIt full1MineBlockAndSendIt = new MineBlockAndSendIt(fullNode1,
                full0Resource, full2Resource, full3Resource, full4Resource);
        ReceiveBlockAndConsensus full1ReceiveBlock = new ReceiveBlockAndConsensus(full1Resource, fullNode1,
                full0Resource, full2Resource, full3Resource, full4Resource);

        /**
         * user1, full2
         */
        MakeTransactionAndSendIt user1MakeTransactionAndSendIt = new MakeTransactionAndSendIt(userNode1, full2Resource);
        UserReceiveTransaction user1ReceiveTransaction = new UserReceiveTransaction(userNode1,user1Resource);

        FullReceiveTransaction full2ReceiveTransaction = new FullReceiveTransaction(fullNode2, full2Resource,user1Resource,
                full1Resource, full4Resource, full5Resource);
        MineBlockAndSendIt full2MineBlockAndSendIt = new MineBlockAndSendIt(fullNode1 ,
                full1Resource, full4Resource, full5Resource);
        ReceiveBlockAndConsensus full2ReceiveBlock = new ReceiveBlockAndConsensus(full2Resource, fullNode2,
                full1Resource, full4Resource, full5Resource);

        /**
         * user2, full4
         */
        MakeTransactionAndSendIt user2MakeTransactionAndSendIt = new MakeTransactionAndSendIt(userNode2, full4Resource);
        UserReceiveTransaction user2ReceiveTransaction = new UserReceiveTransaction(userNode2, user2Resource);

        FullReceiveTransaction full4ReceiveTransaction = new FullReceiveTransaction(fullNode4, full4Resource,user2Resource,
                full0Resource, full1Resource, full2Resource, full3Resource, full5Resource);
        MineBlockAndSendIt full4MineBlockAndSendIt = new MineBlockAndSendIt(fullNode1 ,
                full0Resource, full1Resource, full2Resource, full3Resource, full5Resource);
        ReceiveBlockAndConsensus full4ReceiveBlock = new ReceiveBlockAndConsensus(full4Resource, fullNode4,
                full0Resource, full1Resource, full2Resource, full3Resource, full5Resource);

        /**
         * user3, full0
         */
        MakeTransactionAndSendIt user3MakeTransactionAndSendIt = new MakeTransactionAndSendIt(userNode3, full0Resource);
        UserReceiveTransaction user3ReceiveTransaction = new UserReceiveTransaction(userNode3,user3Resource);

        FullReceiveTransaction full0ReceiveTransaction = new FullReceiveTransaction(fullNode0, full0Resource, user3Resource,
                full1Resource, full4Resource);
        MineBlockAndSendIt full0MineBlockAndSendIt = new MineBlockAndSendIt(fullNode0,
                full1Resource, full4Resource);
        ReceiveBlockAndConsensus full0ReceiveBlock = new ReceiveBlockAndConsensus(full0Resource, fullNode0,
                full1Resource, full4Resource);

        /**
         * full3, full5
         */
        FullReceiveTransactionNoneUser full3ReceiveTransaction = new FullReceiveTransactionNoneUser(fullNode3, full3Resource,
                full1Resource, full4Resource);
        MineBlockAndSendIt full3MineBlockAndSendIt = new MineBlockAndSendIt(fullNode3 ,
                full1Resource, full4Resource);
        ReceiveBlockAndConsensus full3ReceiveBlock = new ReceiveBlockAndConsensus(full3Resource, fullNode3,
                full1Resource, full4Resource);

        FullReceiveTransactionNoneUser full5ReceiveTransaction = new FullReceiveTransactionNoneUser(fullNode5, full5Resource,
                full2Resource, full4Resource);
        MineBlockAndSendIt full5MineBlockAndSendIt = new MineBlockAndSendIt(fullNode5 ,
                full2Resource, full4Resource);
        ReceiveBlockAndConsensus full5ReceiveBlock = new ReceiveBlockAndConsensus(full5Resource, fullNode5,
                full2Resource, full4Resource);

        /**
         * 실행
         */
        new Thread(master).start();

        new Thread(user0MakeTransactionAndSendIt).start();
        new Thread(user0ReceiveTransaction).start();
        new Thread(full1ReceiveTransaction).start();
        new Thread(full1MineBlockAndSendIt).start();
        new Thread(full1ReceiveBlock).start();

        new Thread(user1MakeTransactionAndSendIt).start();
        new Thread(user1ReceiveTransaction).start();
        new Thread(full2ReceiveTransaction).start();
        new Thread(full2MineBlockAndSendIt).start();
        new Thread(full2ReceiveBlock).start();

        new Thread(user2MakeTransactionAndSendIt).start();
        new Thread(user2ReceiveTransaction).start();
        new Thread(full4ReceiveTransaction).start();
        new Thread(full4MineBlockAndSendIt).start();
        new Thread(full4ReceiveBlock).start();

        new Thread(user3MakeTransactionAndSendIt).start();
        new Thread(user3ReceiveTransaction).start();
        new Thread(full0ReceiveTransaction).start();
        new Thread(full0MineBlockAndSendIt).start();
        new Thread(full0ReceiveBlock).start();

        new Thread(full3ReceiveTransaction).start();
        new Thread(full3MineBlockAndSendIt).start();
        new Thread(full3ReceiveBlock).start();

        new Thread(full5ReceiveTransaction).start();
        new Thread(full5MineBlockAndSendIt).start();
        new Thread(full5ReceiveBlock).start();



    }
}
