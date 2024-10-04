import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        List<Long> inputNumbers = Arrays.asList(0L ,34L, 33L, 23L, 34L, 53L,56L);
        List<FactorialThread> threads = new ArrayList<>();
        for(long inputNumber: inputNumbers){
            threads.add(new FactorialThread(inputNumber));
        }
        for(Thread thread: threads){
            thread.setDaemon(true);
            thread.start();
        }
        //the join method will force the main thread to wait the other thread to finished until.
        for(Thread thread: threads){
            try {

                thread.join(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        for(int i = 0;i < inputNumbers.size(); i++){
            FactorialThread factorialThread = threads.get(i);
            if(factorialThread.isFinished()){
                System.out.println("Factorial of " + inputNumbers.get(i) + " is " + factorialThread.getResult());

            }else{
                System.out.println("Currently Still in Progress " + inputNumbers.get(i));
            }
        }
    }
    public  static  class FactorialThread extends Thread{
        private  long inputNumber;
        private BigInteger result = BigInteger.ZERO;
        private boolean isFinished = false;

        public FactorialThread(long inputNumber) {
        this.inputNumber = inputNumber;
        }

        @Override
        public  void run(){
            this.result = factorial(inputNumber);
            this.isFinished=true;
        }
        public BigInteger factorial(long n){
            BigInteger tempResult = BigInteger.ONE;
            for(long i = n; i > 0; i--) {
                tempResult = tempResult.multiply(new BigInteger(Long.toString(i)));
            }
            return  tempResult;
        }
        public boolean isFinished(){
            return  isFinished;
        }
        public BigInteger getResult(){
            return result;
        }
    }

}