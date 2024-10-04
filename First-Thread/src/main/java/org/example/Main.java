package org.example;

import java.math.BigInteger;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
//        Thread thread = new Thread(new BlockingTask());
//        thread.start();
//        thread.interrupt();

         Thread thread = new Thread(new LongComputationTask(new BigInteger("20000") , new BigInteger("100000000")));
         thread.setDaemon(true);
         thread.start();
//         thread.interrupt(); if setdaemon is true the main will not focus because its not his job

    }
    private static class BlockingTask implements Runnable{
        @Override
        public void run() {
            try {
                Thread.sleep(500000);
                System.out.println("eXeCUTING...");
            } catch (InterruptedException e) {
                System.out.println("exiting block thread");
            }
        }
    }
    private static class LongComputationTask implements Runnable{

        private BigInteger base;
        private BigInteger power;

        public LongComputationTask(BigInteger base , BigInteger power){
            this.base = base;
            this.power = power;
        }
        @Override
        public void run() {
            System.out.println(base+"^"+power+" = "+ pow(base,power));
        }
        private BigInteger pow(BigInteger base, BigInteger power){
            BigInteger result = BigInteger.ONE;
            for(BigInteger i = BigInteger.ZERO; i.compareTo(power) != 0; i = i.add(BigInteger.ONE)){
                if(Thread.currentThread().isInterrupted()){
                    System.out.println("Prematurely Interrupted");
                    return BigInteger.ZERO;
                }
                result = result.multiply(base);
            }
            return  result;
        }
    }
}