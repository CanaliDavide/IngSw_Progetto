package it.polimi.ingsw.server;

import it.polimi.ingsw.client.data.ColorData;
import it.polimi.ingsw.client.data.ResourceData;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.message.bothArchitectureMessage.*;
import it.polimi.ingsw.message.clientMessage.*;
import it.polimi.ingsw.message.serverMessage.StrongboxModify;
import it.polimi.ingsw.model.personalBoard.market.Marble;
import it.polimi.ingsw.model.personalBoard.market.Market;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.observer.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class VirtualClient implements ModelObserver, ResourceManagerObserver,
        FaithTrackObserver, CardManagerObserver, MarketObserver {

    private String username;
    private ClientConnectionHandler client;
    private Controller controller;
    private final Match match;
    private boolean ready;

    public VirtualClient(String username,
                         ClientConnectionHandler clientConnectionHandler,
                         Match match) {

        this.username = username;
        this.client= clientConnectionHandler;
        this.match = match;
        this.client.setVirtualClient(this);
        this.ready = false;
    }

    public ClientConnectionHandler getClient() { return client; }

    public void setClient(ClientConnectionHandler client) { this.client = client; }

    public int getClientID() {
        return client.getClientID();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        this.ready = true;
    }

    public boolean isReady() {
        return ready;
    }

    public Match getMatch() {
        return match;
    }

    @Override
    public String toString() {
        return "VirtualClient{" +
                ", username='" + username + '\'' +
                ", clientHandler=" + client +
                '}';
    }



    //OBSERVER IMPLEMENTATION



    //MODEL OBSERVER
    @Override
    public void currentPlayerChange() {
        match.sendAllPlayers(new NewTurn());
    }


    @Override
    public void removeDeckDevelopmentSinglePlayer(int row, int column) {
        client.writeToStream(new RemoveDeckDevelopmentCard(row, column));
    }

    //RESOURCE MANAGER OBSERVER


    @Override
    public void bufferUpdate(ArrayList<Resource> resources) {
        match.sendSinglePlayer(getUsername(),
                new BufferUpdate(resources.stream()
                        .map(Resource::toClient)
                        .collect(Collectors.toCollection(ArrayList::new))));

    }

    @Override
    public void anyConversionRequest(ArrayList<Resource> optionOfConversion,
                                     ArrayList<Resource> optionOfDiscount,
                                     int numOfAny, boolean isProduction) {

        client.writeToStream(new AnyConversionRequest(
                optionOfConversion.stream().map(Resource::toClient).collect(Collectors.toCollection(ArrayList::new)),
                optionOfDiscount.stream().map(Resource::toClient).collect(Collectors.toCollection(ArrayList<ResourceData>::new)),
                numOfAny)
        );

    }

    //Faith Track OBSERVER
    @Override
    public void positionIncrease() {
        match.sendAllPlayers(new FaithTrackIncrement(username));
    }


    @Override
    public void popeFavorReached(int idVaticanReport, boolean isDiscard) {
        match.sendAllPlayers(new PopeFavorActivated(idVaticanReport, isDiscard, username));
    }


    //Market

    @Override
    public void marketTrayChange(ArrayList<ArrayList<Marble>> marketTray, Marble lastMarble) {
        ArrayList<ArrayList<ColorData>> marketTrayToClient = new ArrayList<>();
        for (int i = 0; i < marketTray.size(); i++){
            ArrayList<ColorData> marketCol = new ArrayList<>();
            for (Marble marble: marketTray.get(i)){
                marketCol.add(marble.getColorData());
            }
            marketTrayToClient.add(marketCol);
        }

        match.sendAllPlayers(new MarketUpdate(marketTrayToClient, lastMarble.getColorData()));
    }


    //Card Manager

    @Override
    public void cardSlotUpdate(int indexCardSlot, int rowDeckDevelopment, int colDeckDevelopment) {
        match.sendAllPlayers(
                new CardSlotUpdate(rowDeckDevelopment, colDeckDevelopment, indexCardSlot, username));
    }

    @Override
    public void leaderStatusUpdate(int leaderIndex, boolean discard) {
        match.sendAllPlayers(
                new LeaderStatusUpdate(leaderIndex, discard, username));
    }


    //Warehouse updating

    @Override
    public void strongboxUpdate(ArrayList<Resource> strongboxUpdated) {
        match.sendAllPlayers(
                new StrongboxUpdate(strongboxUpdated.stream()
                        .map(Resource::toClient)
                        .collect(Collectors.toCollection(ArrayList::new)), username));
    }

    @Override
    public void depotUpdate(Resource depotUpdated, int index, boolean isLeaderDepot) {
        match.sendAllPlayers(
                new DepotUpdate(depotUpdated.toClient(), index, isLeaderDepot, username)
        );
    }
}
