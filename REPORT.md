# 보고서 
## 블록체인 프로그램
### 학번 : b711206 이름 : 한주덕

(보고서에서 getter, setter의 기능(get...(), set...() 함수)은 단순하게 변수에 값을 집어넣고, 가져오는 기능이므로 설명을 생략하겠습니다. )

이 프로그램은 Main클래스에서 시작한다.

```java
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

Master master = new Master(List.of(fullNode0, fullNode1, fullNode2, fullNode3, fullNode4, fullNode5));
```
우선 fullnode를 생성하고, 유저 노드를 생성한다.  
과제의 조건은 node U0, U1, U2, U3 와  node F0, F1, F2, F3, F4, F5 이므로 유저노드 4개, 풀노드 6개를 새성한다.
유저 노드는 모델명을 위해 유저 이름을 설정해 주었다.
과제의 출력을 위해 master 노드를 생성한다. 

```java
public class UserNode {
    private static final String SPEC = "secp256k1";
    private static final String ALGO = "SHA256withECDSA";
    private String publicKey;
    private PrivateKey privateKey;
    private List<String> usersPublicKeyList;

    private String name;

    private Queue<Transaction> transactionHaving = new LinkedList<>();

    public UserNode() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        ECGenParameterSpec ecSpec = new ECGenParameterSpec(SPEC);
        KeyPairGenerator g = KeyPairGenerator.getInstance("EC");
        g.initialize(ecSpec, new SecureRandom());
        KeyPair keypair = g.generateKeyPair();
        publicKey = Base64.getEncoder().encodeToString(keypair.getPublic().getEncoded());
        privateKey = keypair.getPrivate();
    }
```
유저 노드 클래스를 살펴보면 SPEC과 ALGO는 ECDSA의 공유키를 생성하기 위해 필요한 상수이다.  
usersPublicKeyList는 유저가 다른 유저의 공유키를 갖고 있는 리스트이고, name은 유저의 이름을 저장하기 위해서 있다.  
transactionHaving은 유저가 갖고 있는 트랜잭션 풀이다.  
생성자를 살펴보면 ECDSA를 이용하여 Public Key와 Private Key를 생성한다.

```java
public class FullNode {

    private List<Block> blockPool = new ArrayList<>();

    private Queue<TransactionDto> transactionPool = new LinkedList<>();
    
    private Chain myLongestChain = new Chain();

    private Block lastMiningBlock;
```
풀노드 클래스를 살펴보면 blockPool은 풀노드가 갖고 있는 블록풀이다.  
transactionPool은 풀노드가 갖고 있는 트랜잭션풀이다.  
myLongestChain은 풀노드가 갖고 있는 longest chain이다.  
lastMiningBlock은 가장 최근에 성공 여부 없이 채굴한 블록이다.

```java
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
```
모든 유저가 다른 유저의 public key를 알기 위해서 모든 유저의 public key를 pubKeyList에 넣어 주고, 모든 유저가 갖고있는 User public key list에  
pubKeyList를 연결한다.  
```java
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
```
프로세스간 데이터를 이동하기 위해 공유메모리로 사용할 클래스 FullResource 와 UserResource 를 수 만큼 생성한다.  

```java
public class FullResource {
    private Queue<TransactionDto> transferTransactionToFull = new LinkedList<>();

    private Queue<Block> blocks = new LinkedList<>();



    public void addTransactionToFull(TransactionDto transactionDto){
        transferTransactionToFull.add(transactionDto);
    }

    public void addBlock(Block block){
        blocks.add(block);
    }
}
```
FullResource클래스는 해당 풀노드가 받을 transaction과 block의 Queue가 있고 Queue는 다른 노드들이 보내는 데이터를 쌓아 놓는 역할을 한다.  
addTransactionToFull는 들어오는 트랜잭션을 해당 큐에 넣는다.   
addBlock은 들어오는 블록을 해당 큐에 넣는다.

```java
public class UserResource {

    private Queue<TransactionDto> transferTransactionToUser = new LinkedList<>();

    public void addTransactionToUser(TransactionDto transactionDto){
        transferTransactionToUser.add(transactionDto);
    }
}
```
UserResource클래스는 FullResource와 비슷하게 트랜잭션을 받을 Queue가 있다.  
addTransactionToUser는 트랜잭션을 받아서 해당 큐에 넣는다.

```java
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
```
MakeTransactionAndSendIt클래스는 유저가 갖고 있는 트랜잭션으로 트랜잭션을 만들고, 풀노드에게 보내는 일을 한다.  
UserReceiveTransaction쿨래스는 유저가 트랜잭션을 받는 일을 한다.  
FullReceiveTransaction클래스는 플노드가 트랜잭션을 받고 연결된 노드에게 보내는 일을 한다.  
MineBlockAndSendIt클래스는 풀노드가 블록을 채굴하고 연결된 다른 풀노드에게 보내는 역할을 한다.  
ReceiveBlockAndConsensus클래스는 풀노드가 블록을 받고 받은 블록을 다른 풀노드에게 전송하고 자신의 체인에 연결해보는 일을 한다.  

위 5가지의 일을 하는 클래스를 유저 노드, 풀노드의 개수만큼 생성한다.  
#### 5가지 클래스에 대해 설명하고자 한다.

```java
public class MakeTransactionAndSendIt implements Runnable{

    private UserNode userNode;

    private FullResource fullResource;

    public MakeTransactionAndSendIt(UserNode userNode,  FullResource fullResource) {
        this.userNode = userNode;
        this.fullResource =fullResource;
    }

    @Override
    public void run() {
        for(int i=0; i<3; i++){
            try {
                fullResource.addTransactionToFull(userNode.makeFirstTransaction(i));
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (SignatureException e) {
                throw new RuntimeException(e);
            } catch (InvalidKeyException e) {
                throw new RuntimeException(e);
            }
        }
        while(true){
            try {
                fullResource.addTransactionToFull(userNode.makeTransaction());
                Thread.sleep(5000);
            } catch (InvalidKeyException e) {
                throw new RuntimeException(e);
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (SignatureException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
```
MakeTransactionAndSendIt 클래스는 생성자를 통해 유저노드와 보낼 풀노드의 공유메모리를 넣어준다.  
처음 루프문을 살펴보면 fullResource.addTransactionToFull(userNode.makeFirstTransaction(i))는 3번을 도는데 이는 첫 트랜잭션을  
3개 생성하고 풀노드에게 보내는 일을 한다.  
다음 루프문을 살펴보면 ullResource.addTransactionToFull(userNode.makeTransaction())는 첫 트랜잭션 생성 후 받은 트랜잭션을 가지고  
재판매를 하는 트랜잭션을 생성한다.  

```java
public TransactionDto makeFirstTransaction(int number) throws InvalidKeyException, NoSuchAlgorithmException, IOException, SignatureException {
        Random random = new Random();
        random.setSeed(System.currentTimeMillis());

        Transaction transaction = new Transaction(publicKey, getRandomPubKey(),  random.nextLong(), name+"'s item_"+number,
                LocalDate.now(), LocalDate.now(), "first");

        Signature ecdsaSign = Signature.getInstance(ALGO);
        ecdsaSign.initSign(privateKey);

        byte[] bytes = ChangeByteWIthObject.convertObjectToBytes(transaction);
        ecdsaSign.update(bytes);
        byte[] signature = ecdsaSign.sign();

        String sig = Base64.getEncoder().encodeToString(signature);
        String trId = SHA256.encrypt(transaction);


        return new TransactionDto(transaction, trId, sig);
    }
```
makeFirstTransaction함수는 트랜잭션을 생성하는데 현재 자신의 퍼블릭키, 자신을 포함한 임의의 퍼블릭키, 랜덤으로 가져온 identifier, modelNo, 만든 날짜, 거래 날짜, others를 넣어준다.
그리고 갖고 있는 private key로 서명을 하고 SHA256으로 해쉬를 한다. 트랜잭션과 서명, 해쉬한 값을 리턴한다.  
TransactionDto는 트랜잭션과 서명, 해쉬한 값를 한번에 보내기 위해서 만든 클래스이다.
```java
private String getRandomPubKey(){
        int size = usersPublicKeyList.size();
        Random random = new Random();
        random.setSeed(System.currentTimeMillis());
        return usersPublicKeyList.get(random.nextInt(size));
    }
```
getRandomPubKey 함수는 자신이 갖고 있는 퍼블릭 키 리스트에서 랜덤으로 하나의 키를 리턴한다.
```java
public void addTransactionToFull(TransactionDto transactionDto){
        transferTransactionToFull.add(transactionDto);
    }
```
addTransactionToFull는 받은 TransactionDto를 리소스 큐에 넣어준다.

```java
public TransactionDto makeTransaction() throws InvalidKeyException, NoSuchAlgorithmException, IOException, SignatureException, InterruptedException {
        Random random = new Random();
        random.setSeed(System.currentTimeMillis());

        Transaction transactionFound;
        while(true){
            transactionFound = transactionHaving.poll();
            if(transactionFound==null){
                Thread.sleep(1000);
                continue;
            }
            break;
        }
        Transaction transaction = new Transaction(publicKey, getRandomPubKey(),transactionFound.getIdentifier(), transactionFound.getModelNo(),
                transactionFound.getManufacturedDate(), LocalDate.now(), "used");

        Signature ecdsaSign = Signature.getInstance(ALGO);
        ecdsaSign.initSign(privateKey);

        byte[] bytes = ChangeByteWIthObject.convertObjectToBytes(transaction);
        ecdsaSign.update(bytes);
        byte[] signature = ecdsaSign.sign();

        String sig = Base64.getEncoder().encodeToString(signature);
        String trId = SHA256.encrypt(transaction);

        return new TransactionDto(transaction, trId, sig);
    }
```
makeTransaction함수는 우선 자신이 받은 트랜잭션에서 값을 가져오고 여기에 immutable field 는 변화없이 넣어주고, 퍼블릭 키 부분들과, 거래일, others만 다르게 해서 트랜잭션을 생성한다.  
만약 받은 트랜잭션이 없다면 1초를 쉰다.  

```java
package block.chain.thread.user;

import block.chain.node.UserNode;
import block.chain.sharedResource.UserResource;
import block.chain.transaction.TransactionDto;

import java.util.Queue;

public class UserReceiveTransaction implements Runnable{

    private UserNode userNode;
    private UserResource userResource;

    public UserReceiveTransaction(UserNode userNode, UserResource userResource) {
        this.userNode = userNode;
        this.userResource = userResource;
    }


    @Override
    public void run() {
        Queue<TransactionDto> transferTransaction = userResource.getTransferTransactionToUser();

        while(true){
            TransactionDto transactionDto = transferTransaction.poll();
            if(transactionDto==null){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }else{
                userNode.readTransaction(transactionDto);
            }
        }
    }
}
```
UserReceiveTransaction는 유저노드와 해당 공유 메모리를 넣어서 생성한다.  
자신이 받은 트랜잭션이 없으면 1초 쉰다. 있으면 해당 트랜잭션을 자신의 트랜잭션 풀에 넣는다.

```java
public void readTransaction(TransactionDto transactionDto){
        if(publicKey.equals(transactionDto.getTransaction().getOutput())){
            transactionHaving.add(transactionDto.getTransaction());
        }
    }
```
readTransaction 함수는 자신의 퍼블릭 키와 트랜잭션의 구매자의 퍼블릭 키가 같으면 해당 트랜잭션을 자신이 갖고 있는 트랜잭션 풀에 넣는다.  

```java
package block.chain.thread.full;

import block.chain.node.FullNode;
import block.chain.sharedResource.FullResource;
import block.chain.sharedResource.UserResource;
import block.chain.transaction.TransactionDto;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class FullReceiveTransaction implements Runnable{

    private FullNode fullNode;

    private FullResource myFullResource;

    private UserResource userResource;

    private List<FullResource> otherFullResource = new ArrayList<>();

    public FullReceiveTransaction(FullNode fullNode, FullResource myFullResource,
                                  UserResource userResource, FullResource ...otherResource) {
        this.fullNode = fullNode;
        this.myFullResource = myFullResource;
        this.userResource = userResource;
        for (FullResource fullResource : otherResource) {
            otherFullResource.add(fullResource);
        }
    }

    @Override
    public void run() {
        Queue<TransactionDto> transferTransaction = myFullResource.getTransferTransactionToFull();
        while(true){
            TransactionDto transactionDto = transferTransaction.poll();
            if(transactionDto==null){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }else{
                try {
                    if(fullNode.receiveTransactionAndTransfer(transactionDto)){
                        userResource.addTransactionToUser(transactionDto);
                        for (FullResource fullResource : otherFullResource) {
                            fullResource.addTransactionToFull(transactionDto);
                        }
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
                }
            }
        }
    }
}
```
FullReceiveTransaction 클래스는 자신의 풀노드, 자신의 공유메모리, 자신과 연결된 노드들의 공유메모리를 넣어서 생성한다.  
풀노드가 받은 트랜잭션이 없다면 1초 쉰다.  
받은 트랜잭션이 있다면 자신의 트랜잭션 풀에 해당 트랜잭션이 있으면 해당 트랜잭션은 버리고, 없으면 해당 트랜잭션을 자신의 트랜잭션 풀에 넣고, 연결된 노드들에게 보낸다.  
addTransactionToUser 와 addTransactionToFull 는 공유메모리에 넣어주는 함수이다.
```java
public boolean receiveTransactionAndTransfer(TransactionDto transactionDto) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException, SignatureException, InvalidKeyException {
        if(!transactionPool.contains(transactionDto)){
            transactionPool.add(transactionDto);

            return true;
        }
        return false;
    }
```
receiveTransactionAndTransfer 함수는 해당 트랜잭션이 자신의 트랜잭션풀에 있으면 버리고, 없으면 추가하는 기능이다.  
```java
package block.chain.thread.full;

import block.chain.Block;
import block.chain.node.FullNode;
import block.chain.sharedResource.FullResource;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;

public class MineBlockAndSendIt implements Runnable {

    private FullNode fullNode;

    private List<FullResource> otherFullResource = new ArrayList<>();

    public MineBlockAndSendIt(FullNode fullNode , FullResource ...otherResources) {
        this.fullNode = fullNode;
        for (FullResource otherResource : otherResources) {
            otherFullResource.add(otherResource);
        }
    }
    @Override
    public void run() {
        try {
            while(true){
                Block mining = fullNode.mining();
                System.out.println("채굴");
                fullNode.consensusLongestChain(mining);
                for (FullResource fullResource : otherFullResource) {
                    fullResource.addBlock(mining);
                }
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
```
MineBlockAndSendIt 클래스는 해당 풀노드와 연결된 다른 풀노드를 넣어서 생성한다.  
블록을 채굴한 후 해당 블록을 자신의 longest Chain에 연결 시키고, 해당 블록을 연결된 풀노드에게 보낸다.
```java
public Block mining() throws NoSuchAlgorithmException, InvalidKeySpecException, IOException, SignatureException, InvalidKeyException, InterruptedException {
        List<TransactionDto> transactionDtoValidated = new ArrayList<>();
        while(true){
            if(transactionDtoValidated.size()==8){
                break;
            }
            TransactionDto poll = transactionPool.poll();
            if(poll==null){
                Thread.sleep(1000);
                continue;
            }
            if(validateTransaction(poll)){
                transactionDtoValidated.add(poll);
            }
        }
        MerkleTree merkleTree = new MerkleTree(transactionDtoValidated);
        Block lastBlock = myLongestChain.getLastBlock();
        int blockNo = lastBlock.getBlockNo() + 1;
        String prevBlockHash = SHA256.encrypt(lastBlock);
        Random random = new Random();
        random.setSeed(System.currentTimeMillis());
        Block blockMined = difficulty(random, merkleTree, prevBlockHash, blockNo);
        lastMiningBlock = blockMined;
        return blockMined;
    }
```
mining 함수는 자신의 트랜잭션 풀에서 8개의 트랜잭션을 가져와서 머클트리로 만든다. 이전 블록의 Hash값 블록 넘버를 만들고, 이를 difficulty함수에 넘겨준다.  
difficulty 함수에서 target number보다 작은 값을 찾으면 해당 블록을 넘겨주고, 최근 채굴한 블록 변수에 넣어준다. 이후 채굴된 블록을 리턴한다.




