package block.chain.thread.full;

import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.*;

class FullUserReceiveTransactionTest {

    @Test
    public void test(){
        Queue<Integer> abc = new LinkedList<>();
        new Thread(new Test1(abc)).start();
        new Thread(new Test2(abc)).start();
    }

}
class Test1 implements Runnable{
    private Queue<Integer> abc;

    public Test1(Queue<Integer> abc) {
        this.abc = abc;
    }

    @Override
    public void run() {
        for (int i=0; i<1000; i++){
            abc.add(i);
            System.out.println("추가함");
        }
    }
}
class Test2 implements Runnable{
    private Queue<Integer> abc;

    public Test2(Queue<Integer> abc) {
        this.abc = abc;
    }

    @Override
    public void run() {
        for(int i=0; i< 1000; i++){
            System.out.println("abc.size() = " + abc.size());
        }
    }
}
