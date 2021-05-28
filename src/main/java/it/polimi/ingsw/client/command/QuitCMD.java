package it.polimi.ingsw.client.command;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ClientInput;
import it.polimi.ingsw.client.ClientState;
import it.polimi.ingsw.client.PrintAssistant;
import it.polimi.ingsw.message.serverMessage.QuitGame;

import java.util.ArrayList;

public class QuitCMD implements Command{
    private final String cmd="QUIT";
    private final String param;
    private final Client client;

    public QuitCMD(String param, Client client) {
        this.param = param;
        this.client = client;
    }

    @Override
    public void doCommand() {
        client.writeToStream(new QuitGame());
        System.exit(0);
    }

    @Override
    public void help() {
        ArrayList<String> rowHelp= new ArrayList<>();
        rowHelp.add("HELP: "+cmd);
        rowHelp.add("Quit from where you are, if you are in game you will quit the game and come back to main menu, quitting from main menu will close the client!");
        rowHelp.add("ex: "+cmd.toLowerCase()+ "");
        PrintAssistant.instance.printfMultipleString(rowHelp);
    }

    @Override
    public void description() {
        PrintAssistant.instance.printf(PrintAssistant.instance.fitToWidth(cmd, ClientInput.MAX_CHAR_COMMAND)+"to quit the place you are in");
    }
}
