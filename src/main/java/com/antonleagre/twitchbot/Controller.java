package com.antonleagre.twitchbot;

import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class Controller{
    private final String clientID = "6px0arpv3gju31t8u6demwjm3u4l60";

    @FXML
    private TabPane tabs;
    @FXML
    private Tab botTab, statsTab, chatTab, previewTab;
    @FXML
    private RadioMenuItem tabMenuBot, tabMenuStats, tabMenuChat, tabMenuPreview;
    private Browser streamPreview;

    //helper booleans
    private boolean bBotTab = true;
    private boolean bstatsTab = true;
    private boolean bChatTab = true;
    private boolean bPreviewTab = true;

    @FXML
    public void initialize(){
        streamPreview = new Browser("http:/player.twitch.tv/?veetf2"); // TODO: 29/08/2017 make this program argument or connect based
        previewTab.setContent(streamPreview);

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

