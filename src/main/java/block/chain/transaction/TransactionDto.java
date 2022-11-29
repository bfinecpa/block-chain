package block.chain.transaction;

import java.io.Serializable;

public class TransactionDto  implements Serializable {

    private Transaction transaction;

    //트랜잭션 해쉬 값
    private String trId;

    private String sig;

    public TransactionDto(Transaction transaction, String trId, String sig) {
        this.transaction = transaction;
        this.trId = trId;
        this.sig = sig;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public String getSig() {
        return sig;
    }

    public String getTrId() {
        return trId;
    }
}


