package it.polimi.ingsw.message.clientMessage;

import it.polimi.ingsw.client.ClientMessageHandler;

public class NewTurn implements ClientMessage{



    @Override
    public void process(ClientMessageHandler handler) {
        System.out.println("ciaooooooo");
    }
}
