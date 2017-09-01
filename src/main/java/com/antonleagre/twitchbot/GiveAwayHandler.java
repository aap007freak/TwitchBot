package com.antonleagre.twitchbot;

public class GiveAwayHandler {

    public GiveAwayHandler(){
        System.out.println("clicked");
        new Countdown(10, true).start();
    }
}
