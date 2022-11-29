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
import block.chain.thread.user.MakeTransactionAndSendIt;
import block.chain.thread.user.UserReceiveTransaction;

public class Main {
    public static void main(String[] args) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException {


        //user, full 생성
        FullNode fullNode0 = new FullNode();
        UserNode userNode0 = new UserNode();
        FullNode fullNode1 = new FullNode();
        UserNode userNode1 = new UserNode();

        Master master = new Master(List.of(fullNode0));


        //유저가 pubkey를 알아야해서
        List<String> pubKeyList = new ArrayList<>();
        //연결
        pubKeyList.add(userNode0.getPublicKey());
        userNode0.setUsersPublicKeyList(pubKeyList);
/*        pubKeyList.add(userNode1.getPublicKey());
        userNode1.setUsersPublicKeyList(pubKeyList);*/


        //데이터 전송을 위한 공유 메모리
        FullResource full0Resource = new FullResource();
        UserResource user0Resource = new UserResource();
        FullResource full1Resource = new FullResource();
        UserResource user1Resource = new UserResource();


        /**
         * user0
         */
        MakeTransactionAndSendIt user0MakeTransactionAndSendIt = new MakeTransactionAndSendIt(userNode0,full0Resource);
        FullReceiveTransaction full0ReceiveTransaction = new FullReceiveTransaction(fullNode0, full0Resource, user0Resource, full1Resource);
        MineBlockAndSendIt full0MineBlockAndSendIt = new MineBlockAndSendIt(fullNode0);
        UserReceiveTransaction user0ReceiveTransaction = new UserReceiveTransaction(userNode0, user0Resource);

        /**
         * user1
         */
        MakeTransactionAndSendIt user1MakeTransactionAndSendIt = new MakeTransactionAndSendIt(userNode1, full1Resource);
        FullReceiveTransaction full1ReceiveTransaction = new FullReceiveTransaction(fullNode1, full1Resource,user1Resource,full0Resource);
        MineBlockAndSendIt full1MineBlockAndSendIt = new MineBlockAndSendIt(fullNode1);
        UserReceiveTransaction user1ReceiveTransaction = new UserReceiveTransaction(userNode1,user1Resource);



        new Thread(user0MakeTransactionAndSendIt).start();
        new Thread(user0ReceiveTransaction).start();
        new Thread(full0ReceiveTransaction).start();
        new Thread(full0MineBlockAndSendIt).start();

        new Thread(master).start();

  /*      new Thread(user1MakeTransactionAndSendIt).start();
        new Thread(user1ReceiveTransaction).start();
        new Thread(full1ReceiveTransaction).start();
        new Thread(full1MineBlock).start();*/




    }
}
