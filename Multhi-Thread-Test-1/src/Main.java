
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static class ComplexCalculation {
        public BigInteger calculateResult(BigInteger base1, BigInteger power1, BigInteger base2, BigInteger power2) throws InterruptedException {
            BigInteger result = BigInteger.ZERO;
        /*
            Calculate result = ( base1 ^ power1 ) + (base2 ^ power2).
            Where each calculation in (..) is calculated on a different thread
        */
            System.out.println("HII");
            List<PowerCalculatingThread> threads = Arrays.asList(new PowerCalculatingThread(base1, power1), new PowerCalculatingThread(base2, power2));
            for(Thread thread: threads){
                thread.start();
            }
            for(Thread thread: threads){
                thread.join();
            }
            for(int i = 0 ; i < threads.size(); i++){
                PowerCalculatingThread pThread = threads.get(i);
                result = result.add(pThread.getResult());
            }

            return result;
        }

        private static class PowerCalculatingThread extends Thread {
            private BigInteger result = BigInteger.ONE;
            private BigInteger base;
            private BigInteger power;

            public PowerCalculatingThread(BigInteger base, BigInteger power) {
                this.base = base;
                this.power = power;
            }

            @Override
            public void run() {
                BigInteger currResult = BigInteger.ONE;
                while (power.compareTo(BigInteger.ZERO) > 0) {
                    currResult = currResult.multiply(base);
                    power = power.subtract(BigInteger.ONE);
                    // Decrease power by 1
                }
                this.result = currResult;
           /*
           Implement the calculation of result = base ^ power
           */
            }

            public BigInteger getResult() { return result; }
        }
    }
    public static void main(String[] args) throws InterruptedException {
        ComplexCalculation cr = new ComplexCalculation();
        cr.calculateResult(new BigInteger("2"), new BigInteger("5"), new BigInteger("5"), new BigInteger("5"));
    }
}