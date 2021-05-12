package it.polimi.ingsw.client.command;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ClientInput;
import it.polimi.ingsw.client.ClientState;
import it.polimi.ingsw.client.PrintAssistant;
import it.polimi.ingsw.message.bothArchitectureMessage.ConnectionMessage;
import it.polimi.ingsw.message.bothArchitectureMessage.ConnectionType;

import java.util.ArrayList;

public class UsernameCMD implements Command{
    private final String cmd="USERNAME";
    private final String param;
    private final Client client;

    public UsernameCMD(String param, Client client) {
        this.param = param;
        this.client = client;
    }

    @Override
    public void doCommand() {
        if(param.isEmpty() || param.isBlank()){
            PrintAssistant.instance.invalidParamCommand(cmd);
            return;
        }
        if(client.getState()!= ClientState.ENTERING_LOBBY){
            PrintAssistant.instance.invalidStateCommand(cmd);
            return;
        }
        //System.out.println("--"+param+"---");
        String[] split= param.split(" ", 2);
        if(split.length>1){
            PrintAssistant.instance.invalidParamCommand(cmd);
            return;
        }
        //System.out.println("--"+split[0]+"---");
        client.setMyName(split[0]);
        client.writeToStream(new ConnectionMessage(ConnectionType.USERNAME, split[0]));
    }

    @Override
    public void help() {
        ArrayList<String> rowHelp= new ArrayList<>();
        rowHelp.add("HELP: "+cmd);
        rowHelp.add("write the command followed by the string you want to use as username");
        rowHelp.add("ex: username foo");
        PrintAssistant.instance.printfMultipleString(rowHelp);
    }

    @Override
    public void description() {
        PrintAssistant.instance.printf(PrintAssistant.instance.fitToWidth(cmd, ClientInput.MAX_CHAR_COMMAND)+"to insert your username when the client ask for it");
    }
}
