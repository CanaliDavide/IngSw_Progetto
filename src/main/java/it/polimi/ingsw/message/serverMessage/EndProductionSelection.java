package it.polimi.ingsw.message.serverMessage;

import it.polimi.ingsw.server.ServerMessageHandler;

public class EndProductionSelection implements ServerMessage{
    @Override
    public void process(ServerMessageHandler handler) {
        handler.handleEndCardSelection();
    }
}
