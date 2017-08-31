package com.antonleagre.twitchbot;

import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

public class CommandHandler {

    // TODO: 31/08/2017 make commands USER_TYPE based (like mod or subsriber or smthing) 
    JFXTreeTableView<Command> treeTableView;
    ObservableList<Command> commands;

    public CommandHandler(JFXTreeTableView treeTableView){

        this.treeTableView = treeTableView;


        JFXTreeTableColumn<Command, String> triggerCol = new JFXTreeTableColumn<>("Trigger for the command");
        triggerCol.setPrefWidth(150);
        triggerCol.setCellValueFactory(param -> param.getValue().getValue().trigger);

        JFXTreeTableColumn<Command, String> commandCol = new JFXTreeTableColumn<>("Command");
        commandCol.setPrefWidth(150);
        commandCol.setCellValueFactory(param -> param.getValue().getValue().command);

        commands = FXCollections.observableArrayList();
        // TODO: 31/08/2017 add default commands and program args commands here; (using commands.add..)


        final TreeItem<Command> root = new RecursiveTreeItem<Command>(commands, RecursiveTreeObject::getChildren);

        treeTableView.setRoot(root);
        treeTableView.setShowRoot(false);
        treeTableView.getColumns().setAll(triggerCol, commandCol);

    }

    public void addCommand(String trigger, String command){
        if(commands != null){
            Command newCom = new Command(trigger, command);
            commands.add(newCom);
        }
    }

    public String checkCommand(String supposedTrigger){
        for(Command command : commands){
            
            if(supposedTrigger.startsWith(String.format("!%s", command.trigger.get()))){ // TODO: 31/08/2017 we should be using REGEX matching with matcher but i can't be bothered
                return command.command.get();
            }else{
                return null;
            }
        }
        return null;
    }

    class Command extends RecursiveTreeObject<Command>{
        StringProperty trigger;
        StringProperty command;

        public Command(String trigger, String command){
            this.trigger = new SimpleStringProperty(trigger);
            this.command = new SimpleStringProperty(command);
        }
    }
}
