package com.antonleagre.twitchbot;


import com.gikk.twirk.Twirk;

public class GiveAwayHandler {
    // TODO: 2/09/2017 maybe addd support for a progressbar to see the progress of the giveaway, or custom commands on certain events.
    private final Twirk twirk;
    public GiveAwayHandler(Twirk twirk){
        this.twirk = twirk;
    }


    public void startGiveaway(GiveAway giveAway){
        Countdown cd = new Countdown(giveAway.getTotalSeconds(),false);
        cd.onStep(() -> {
            if(giveAway.chatAlers()){
                handleChat(giveAway, cd.getCountdownTick());
            }
        });
        cd.onFinish(() -> {

        });
        cd.start();

    }
    private void handleChat(GiveAway giveAway,int currentSecond){
        if(giveAway.getTotalSeconds() > 61){ //assuming its a long giveaway.
            if(giveAway.getTotalSeconds() == currentSecond){ //as soon as the giveaway starts
                System.out.println("a giveaway has begun");
            }
            if(currentSecond == 60){ //one minute left
                System.out.println("theres only one minute lef!");
            }
            if(currentSecond == 15){ //15 secs left
                System.out.println("15 secs left!");
            }
            if(currentSecond == 5){
                System.out.println("last chance to enter, the winner will be picked shortly");
            }
        }else{
            if(giveAway.getTotalSeconds() == currentSecond){ //as soon as the giveaway starts
                System.out.println("a short giveaway has begun");
            }

            if(currentSecond == 15){ //15 secs left
                System.out.println("15 secs left!");
            }
            if(currentSecond == 5){
                System.out.println("last chance to enter, the winner will be picked shortly");
            }
        }
    }




}
class GiveAway{

    private int totalSeconds;
    private boolean chatAlers, enteringMoreThanOnce;
    private String prize;

    public GiveAway(int totalSecondsforGiveAway, boolean enteringMoreThanOnce, boolean chatAlers, String prize){
        this.totalSeconds = totalSecondsforGiveAway;
        this.enteringMoreThanOnce = enteringMoreThanOnce;
        this.chatAlers = chatAlers;
        this.prize = prize;
    }

    public int getTotalSeconds() {
        return totalSeconds;
    }

    public String getPrize() {
        return prize;
    }

    public boolean enteringMoreThanOnce() {
        return enteringMoreThanOnce;
    }

    public boolean chatAlers() {
        return chatAlers;
    }
}