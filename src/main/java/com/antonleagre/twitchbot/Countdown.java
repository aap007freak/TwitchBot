package com.antonleagre.twitchbot;

import java.util.Timer;
import java.util.TimerTask;

public class Countdown {

    private final boolean verbose;

    private Runnable stepRunnable;
    private Runnable finishRunnable;

    private Timer timer;

    private int totalSeconds;
    private int countdownSeconds; //counter
    private int actualSeconds; //nice seconds for printing to the other parts of the app

    private boolean stopRequested = false;
    private boolean running = false;

    public Countdown(int amountOfSeconds, Runnable onStep, Runnable onFinish, boolean verbose){

        this.totalSeconds = amountOfSeconds;
        countdownSeconds = totalSeconds + 1;
        this.stepRunnable = onStep;
        this.finishRunnable = onFinish;

        //setting up timer
        timer = new Timer();
        this.verbose = verbose;
    }

    public Countdown(int amountOfSeconds, boolean verbose){

        this.totalSeconds = amountOfSeconds;
        countdownSeconds = totalSeconds + 1;
        this.stepRunnable = () -> {};
        this.finishRunnable = () -> {};

        //setting up timer
        timer = new Timer();
        this.verbose = verbose;
    }

    public Countdown(int amountOfSeconds){

        this.totalSeconds = amountOfSeconds;
        countdownSeconds = totalSeconds + 1;
        this.stepRunnable = () -> {};
        this.finishRunnable = () -> {};

        //setting up timer
        timer = new Timer();
        this.verbose = false;
    }

    public void onStep(Runnable runner){
        this.stepRunnable = runner;
    }
    public void onFinish(Runnable runner){
        this.finishRunnable = runner;
    }



    public void start(){
        timer.scheduleAtFixedRate(
                new TimerTask() {
                    @Override
                    public void run() {
                        running = true;
                        countdownSeconds--;
                        if(countdownSeconds < 0){
                            finishRunnable.run();
                            timer.cancel();

                        }else if(stopRequested){
                            timer.cancel();
                            actualSeconds = 0;
                            running = false;
                            stopRequested = false;
                        }
                        else{ //smooth cycle
                            actualSeconds = countdownSeconds;
                            stepRunnable.run();

                            if(verbose) System.out.println("Running a countdown with: " + getCountdownTick() + " seconds remaining...");
                        }
                    }
                }, 0,
                1000);
    }

    public void stop(){
        stopRequested = true;
    }
    public void reset(int amountOfSeconds){
        if(running){
            stop();
        }
        this.totalSeconds = amountOfSeconds;
        countdownSeconds = totalSeconds + 1;
    }

    public int getCountdownTick(){
        if(running){
            return actualSeconds;
        }else{
            return 0;
        }
    }
    public boolean getRunning(){
        return running;
    }

}
