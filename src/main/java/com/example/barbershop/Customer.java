package com.example.barbershop;

import java.util.concurrent.ThreadLocalRandom;

public class Customer extends Thread {

    private static final int COME_BACK_UPPER_BOUND = 3;
    private static final int HAIRCUT_LOWER_BOUND = 1;
    private static final int HAIRCUT_UPPER_BOUND = 4;

    private String name;
    private boolean verbose;
    private boolean needHaircut;

    public Customer(String name, boolean verbose) {
        this.name = name;
        this.verbose = verbose;
        this.needHaircut = true;
    }

    @Override
    public void run() {
        System.out.println(name + " needs a haircut.");

        while(needHaircut) {
            if(takesSeat()) {
                System.out.println(name + " has taken a seat.");
                getHaircut();
            } else {
                comeBack();
            }
        }
        System.out.println(name + " has gotten a haircut.");
    }

    private boolean takesSeat() {
        return Barbershop.seats.tryAcquire();
    }

    private void comeBack() {
        try {
            int period = ThreadLocalRandom.current().nextInt(COME_BACK_UPPER_BOUND);
            if(verbose) {
                System.out.println(name + " is coming back in " + period + " seconds.");
            }
            Thread.sleep(1000 * period);
        } catch(InterruptedException e) {
            return;
        }
    }

    private void getHaircut() {

        try {
            Barbershop.barber.acquire();
        } catch(InterruptedException e) {
            return;
        }

        System.out.println(name + " is getting a haircut.");

        try {
            Thread.sleep(1000 * (ThreadLocalRandom.current().nextInt(HAIRCUT_LOWER_BOUND, HAIRCUT_UPPER_BOUND+1)));
        } catch(InterruptedException e) {
            // noop
        }
        needHaircut = false;
        Barbershop.barber.release();
        Barbershop.seats.release();
    }

}
