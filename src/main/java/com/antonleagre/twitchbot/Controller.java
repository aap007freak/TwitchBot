package com.antonleagre.twitchbot;

import com.gikk.twirk.Twirk;
import com.gikk.twirk.TwirkBuilder;
import com.gikk.twirk.events.TwirkListenerBaseImpl;
import com.gikk.twirk.types.twitchMessage.TwitchMessage;
import com.gikk.twirk.types.users.TwitchUser;
import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import javax.annotation.PostConstruct;
import java.io.IOException;

public class Controller{


    //both of veetf2
    private final String clientID = "6px0arpv3gju31t8u6demwjm3u4l60";
    private final String oAth = "oauth:y30zgzvjc4i7a36ctv55e1rdbfkwj6";

    private Twirk twirk;
    private CommandHandler commandHandler;

    @FXML
    private TabPane tabs;
    @FXML
    private Tab botTab, statsTab, chatTab, previewTab;
    //menu items
    @FXML
    private RadioMenuItem tabMenuBot, tabMenuStats, tabMenuChat, tabMenuPreview;
    @FXML
    private MenuItem connectMenuConnect, connectMenuDisconnect;
    //tab items sorted by tab;
    //chat tab
    @FXML
    private TextArea chatTA;
    //bottab
    @FXML
    private Label botConnectedLabel;
    @FXML
    private JFXTreeTableView<CommandHandler.Command> commandTable; //test
    @FXML
    private JFXTextField newComTrigger, newComCommand;
    @FXML
    private JFXButton newComAddButton;
    @FXML
    private TextArea logTA;


    private Browser streamPreview;

    //helper booleans
    private boolean bBotTab = true;
    private boolean bstatsTab = true;
    private boolean bChatTab = true;
    private boolean bPreviewTab = true;

    @FXML @PostConstruct
    public void initialize() throws IOException, InterruptedException {
        commandHandler = new CommandHandler(commandTable);


        setupBot();
        logTA.appendText("PROGRAM CORRECTLY INITALIZED\n");
        /*
                streamPreview = new Browser("http:/player.twitch.tv/?veetf2"); // TODO: 29/08/2017 make this program argument or connect based
                previewTab.setContent(streamPreview); todo make this work somehow
         */

    }
    @FXML
    protected void tabMenuEnableAllClicked(){
        if(!bBotTab){
            tabs.getTabs().add(botTab);
            bBotTab = true;
            tabMenuBot.setSelected(true);
        }
        if(!bstatsTab){
            tabs.getTabs().add(statsTab);
            bstatsTab = true;
            tabMenuStats.setSelected(true);
        }
        if(!bChatTab){
            tabs.getTabs().add(chatTab);
            bChatTab = true;
            tabMenuChat.setSelected(true);
        }
        if(!bPreviewTab){
            tabs.getTabs().add(previewTab);
            bPreviewTab = true;
            tabMenuPreview.setSelected(true);
        }
    }
    @FXML
    protected void tabMenuBotClicked(){
        if(bBotTab){
           //close the tab
            tabs.getTabs().remove(botTab);
            bBotTab = false;
        }else{
            //reopen the tab
            tabs.getTabs().add(botTab);
            bBotTab = true;
        }
    }
    @FXML
    protected void tabMenuStatsClicked(){
            if(bstatsTab){
                //close the tab
                tabs.getTabs().remove(statsTab);
                bstatsTab = false;
            }else{
                //reopen the tab
                tabs.getTabs().add(statsTab);
                bstatsTab = true;
            }
        }
    @FXML
    protected void tabMenuPreviewClicked(){
            if(bPreviewTab){
                //close the tab
                tabs.getTabs().remove(previewTab);
                bPreviewTab = false;
            }else{
                //reopen the tab
                tabs.getTabs().add(previewTab);
                bPreviewTab = true;
            }
        }
    @FXML
    protected void tabMenuChatClicked(){
            if(bChatTab){
                //close the tab
                tabs.getTabs().remove(chatTab);
                bChatTab = false;
            }else{
                //reopen the tab
                tabs.getTabs().add(chatTab);
                bChatTab = true;
            }
        }
    @FXML
    protected void connectMenuConnectClicked() throws IOException, InterruptedException {
            logTA.appendText("TRYING TO CONNECT TO TWITCH\n");
            new Thread(() -> {
                // TODO: 29/08/2017 add some sort of feedback
                if(twirk != null && !twirk.isConnected()){
                    try {
                        twirk.connect();
                        Platform.runLater(() -> {
                            connectMenuConnect.setDisable(true);
                            connectMenuDisconnect.setDisable(false);
                            botConnectedLabel.setTextFill(Color.GREEN);
                            botConnectedLabel.setText("Connected!");
                            logTA.appendText("CONNECTED TO TWITCH");
                        });

                    } catch (IOException | InterruptedException e) {
                        Platform.runLater(() -> logTA.appendText("ERROR! CONNECTING FAILED... " + e.getCause().toString()));

                        e.printStackTrace();
                    }

                }
            }).start();

        }
    @FXML
    protected void connectMenuDisconnectClicked(){
            logTA.appendText("DISCONNECTING...\n");
            new Thread(() -> {
                if(twirk != null && twirk.isConnected()){
                    twirk.disconnect();
                    Platform.runLater(() -> {
                        connectMenuConnect.setDisable(false);
                        connectMenuDisconnect.setDisable(true);
                        botConnectedLabel.setTextFill(Color.RED);
                        botConnectedLabel.setText("Not connected!");
                    });

                }
            }).start();

        }
    @FXML
    protected void botAddComClicked(ActionEvent actionEvent) {
        // TODO: 31/08/2017 check for illegal characters
        String triggerTex = newComTrigger.getText();
        String commandTex = newComCommand.getText();
        commandHandler.addCommand(triggerTex, commandTex);
    }


    private void setupBot(){
        twirk = new TwirkBuilder("#veetf2", "veetf2", oAth).build(); //channel name (with num thing #, then username, then oath:)

        twirk.addIrcListener(new TwirkListenerBaseImpl() {
            @Override
            public void onPrivMsg(TwitchUser sender, TwitchMessage message) {

                chatTA.appendText(sender.getDisplayName() + ": " + message.getContent() +"\n");

                //check if the message is a command, and the updating the ui based of it.
                String command = commandHandler.checkCommand(message.getContent());
                if(command != null){
                    System.out.println("command detected, we need to do stuff");
                    twirk.channelMessage("@" + sender.getDisplayName() + ", " + command);
                }

            }
        });
    }

}

 class Browser extends Region {

    final WebView browser = new WebView();
    final WebEngine webEngine = browser.getEngine();
    String url;
    public Browser(String urlto) {
        this.url = urlto;
        //apply the styles
        getStyleClass().add("browser");
        // load the web page
        webEngine.load(url);
        //add the web view to the scene
        getChildren().add(browser);

    }

    private Node createSpacer() {
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        return spacer;
    }

    @Override protected void layoutChildren() {
        double w = getWidth();
        double h = getHeight();
        layoutInArea(browser,0,0,w,h,0, HPos.CENTER, VPos.CENTER);
    }

    @Override protected double computePrefWidth(double height) {
        return 750;
    }

    @Override protected double computePrefHeight(double width) {
        return 500;
    }
    public void reload(){
        webEngine.load(url);
    }
}

class User extends RecursiveTreeObject<User> {
    StringProperty userName;
    StringProperty age;
    StringProperty department;

    public User(String department, String age, String userName) {
        this.department = new SimpleStringProperty(department) ;
        this.userName = new SimpleStringProperty(userName);
        this.age = new SimpleStringProperty(age);
    }
}

