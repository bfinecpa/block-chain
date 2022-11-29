package block.chain.transaction;

import java.io.Serializable;
import java.security.PublicKey;
import java.time.LocalDate;

public class Transaction implements Serializable {

    //input 은 특정 물품의 판매자의 public key
    private String input;

    //output 은 이 물품의 구매자의 public key
    private String output;


    // 아래 3개는 immutable
    //특정 물품의 id
    private Long identifier;

    //물품의 모델명
    private String modelNo;

    //물품의 제조일
    private LocalDate manufacturedDate;

    //거래일 -> 변할 수 있음
    private LocalDate tradingDate;

    //상세내용 -> 변할 수 있음
    private String others;



    public Transaction(String input, String output, Long identifier, String modelNo, LocalDate manufacturedDate, LocalDate tradingDate, String others) {
        this.input = input;
        this.output = output;
        this.identifier = identifier;
        this.modelNo = modelNo;
        this.manufacturedDate = manufacturedDate;
        this.tradingDate = tradingDate;
        this.others = others;
    }



    public void setTradingDate(LocalDate tradingDate) {
        this.tradingDate = tradingDate;
    }

    public void setOthers(String others) {
        this.others = others;
    }

    public String getInput() {
        return input;
    }

    public String getOutput() {
        return output;
    }

    public Long getIdentifier() {
        return identifier;
    }

    public String getModelNo() {
        return modelNo;
    }

    public LocalDate getManufacturedDate() {
        return manufacturedDate;
    }

    public LocalDate getTradingDate() {
        return tradingDate;
    }

    public String getOthers() {
        return others;
    }

    public boolean compareImmutable(Transaction curTransaction) {
        if(this.identifier.equals(curTransaction.getIdentifier()) &&
                this.modelNo.equals(curTransaction.getModelNo()) &&
                this.manufacturedDate.equals(curTransaction.getManufacturedDate())
        ){
            return true;
        }
        return false;
    }
}
