package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.message.ConnectionMessage;
import it.polimi.ingsw.message.PingPongMessage;
import it.polimi.ingsw.message.serverMessage.ReconnectionMessage;
import java.util.Optional;

public class ServerMessageHandler {
    private Controller controller;
    private Server server;
    private ClientConnectionHandler client;
    private Optional<VirtualClient> virtualClient = Optional.empty();
    private HandlerState state;

    public ServerMessageHandler(Server server, ClientConnectionHandler client) {
        this.server = server;
        this.client = client;
        this.state = HandlerState.FIRST_CONTACT;
    }

    public void setVirtualClient(VirtualClient virtualClient) {
        this.virtualClient = Optional.of(virtualClient);
    }

    public void setState(HandlerState state){
        this.state = state;
    }

    public HandlerState getState() { return state; }

    public void handleConnectionMessage(ConnectionMessage message){
        System.out.println(message.getType() + ": " + message.getMessage());
    }

    public void handleFirstContact(ConnectionMessage message){
        if(state != HandlerState.FIRST_CONTACT) return;
        server.putInLobby(client);
    }

    public void handleMatchCreation(ConnectionMessage message){
        if (state != HandlerState.NUM_OF_PLAYER) return;
        server.createMatch(message.getNum(),client);
    }

    public void handleUsernameInput(ConnectionMessage message){
        if (state != HandlerState.USERNAME) return;
        virtualClient.ifPresent(value -> value.getMatch().setPlayerUsername(value, message.getMessage()));
    }

    public void handleDisconnection(){
        virtualClient.ifPresentOrElse(value -> value.getMatch().playerDisconnection(value),()->server.clientDisconnect(client));

    }

    public void handleReconnection(ReconnectionMessage message){
        server.clientReconnection(message.getMatchID(), message.getClientID(), client);
    }

    public void handlePingPong(PingPongMessage message){
        //TODO
    }

}
