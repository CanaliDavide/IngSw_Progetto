package it.polimi.ingsw.controller;

import it.polimi.ingsw.exception.*;
import it.polimi.ingsw.message.clientMessage.ErrorType;
import it.polimi.ingsw.model.GameMaster;
import it.polimi.ingsw.model.card.Development;
import it.polimi.ingsw.model.personalBoard.PersonalBoard;
import it.polimi.ingsw.model.personalBoard.cardManager.CardManager;
import it.polimi.ingsw.model.personalBoard.market.Market;
import it.polimi.ingsw.model.personalBoard.resourceManager.ResourceManager;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.server.Match;
import it.polimi.ingsw.server.VirtualClient;

import java.util.ArrayList;

public class Controller {
    private GameMaster gameMaster;
    private Match match;

    public Controller(GameMaster gameMaster, Match match) {
        this.gameMaster = gameMaster;
        this.match = match;
        registerAllVirtualClientObserver();
    }

    public Controller(GameMaster gameMaster){
        this.gameMaster = gameMaster;
    }

    private void sendError(ErrorType errorType){
        System.out.println("SingleErrorMessage: " + errorType);
        //match.sendSinglePlayer(gameMaster.getCurrentPlayer(), new ErrorMessage(errorType));
    }
    private void sendError(String customMessage){
        System.out.println("SingleErrorMessage: " + customMessage);
        //match.sendSinglePlayer(gameMaster.getCurrentPlayer(), new ErrorMessage(customMessage));
    }


    public TurnState getTurnState(){
        return gameMaster.getTurnState();
    };

    public boolean isCorrectTurnState(String username, TurnState turnState){
        return isYourTurn(username) && getTurnState() == turnState;
    }

    public boolean isYourTurn(String username){
        return getCurrentPlayer().equals(username);
    }


    private String getCurrentPlayer(){
        return gameMaster.getCurrentPlayer();
    };


    private void registerAllVirtualClientObserver(){
        for (VirtualClient virtualClient: match.getAllPlayers()){
            String username = virtualClient.getUsername();
            //model observer
            gameMaster.attachObserver(virtualClient);

            //resource manager observer
            gameMaster.getPlayerPersonalBoard(username)
                    .getResourceManager().attachObserver(virtualClient);

            //faith track observer
            gameMaster.getPlayerPersonalBoard(username)
                    .getFaithTrack().attachObserver(virtualClient);

            //card manager
            gameMaster.getPlayerPersonalBoard(username)
                    .getCardManager().attachObserver(virtualClient);
        }

    }


    public void nextTurn() {
        //TODO check if the next player is active (maybe)
        gameMaster.nextPlayer();
    }



    //LEADER MANAGING
    public void manageLeaderCard(int leaderIndex, boolean discard){
        CardManager cardManager = gameMaster.getPlayerPersonalBoard(getCurrentPlayer()).getCardManager();
        try {
            if (discard){
                cardManager.discardLeader(leaderIndex);
            }else{
                cardManager.activateLeader(leaderIndex);
            }
        } catch (Exception e) {
            sendError(e.getMessage());
        }
    }


    //MARKET ACTION
    public void marketAction(int selection, boolean isRow){
        PersonalBoard personalBoard = gameMaster.getPlayerPersonalBoard(getCurrentPlayer());
        Market market = gameMaster.getMarket();
        CardManager cardManager = personalBoard.getCardManager();
        try{
            if (isRow)
                market.insertMarbleInRow(selection);
            else
                market.insertMarbleInCol(selection);
        }catch (Exception e){
            sendError(e.getMessage());
            return;
        }


        if (market.getWhiteMarbleDrew() == 0 || !cardManager.doIHaveMarbleEffects()){
            ResourceManager resourceManager = personalBoard.getResourceManager();
            //bufferInsertion
            resourceManager.resourceFromMarket(market.getResourceToSend());
            market.reset();
        }
    }

    public void leaderWhiteMarbleConversion(int leaderIndex, int numOfWhiteMarble){
        CardManager cardManager = gameMaster.getPlayerPersonalBoard(getCurrentPlayer()).getCardManager();
        if(!cardManager.doIHaveMarbleEffects()){
            sendError("You don't have leader with marble effects");
            return;
        }
        Market market = gameMaster.getMarket();
        market.setWhiteMarbleToTransform(numOfWhiteMarble);

        try{
            cardManager.activateLeaderEffect(leaderIndex, getTurnState());
        }catch (Exception e){
            sendError(e.getMessage());
            return;
        }

        if(market.getWhiteMarbleDrew() == 0){
            ResourceManager resourceManager = gameMaster.getPlayerPersonalBoard(getCurrentPlayer()).getResourceManager();
            resourceManager.resourceFromMarket(market.getResourceToSend());
            market.reset();
        }
    }


    //BUY DEVELOPMENT CARD ACTION
    public void developmentAction(int row, int col, int locateSlot){
        Development card;
        try{
            card = gameMaster.getDeckDevelopmentCard(row, col);
        }catch (Exception e){
            sendError(e.getMessage());
            return;
        }
        //the card is already attached to the current player
        if (!card.checkRequirements()){
            sendError(ErrorType.NOT_ENOUGH_REQUIREMENT);
            return;
        }
        PersonalBoard personalBoard = gameMaster.getPlayerPersonalBoard(getCurrentPlayer());
        CardManager cardManager = personalBoard.getCardManager();
        try {
            cardManager.addDevelopmentCardTo(card, locateSlot);
            cardManager.setDeckDevelopmentCardBufferInformation(row, col);
        } catch (Exception e) {
            sendError(e.getMessage());
        }

    }

    //PRODUCTION ACTION

    public void leaderProductionAction(int leaderIndex){
        CardManager cardManager = gameMaster.getPlayerPersonalBoard(getCurrentPlayer()).getCardManager();
        if (!cardManager.doIHaveProductionEffects()){
            sendError("You don't have leader with production effect");
            return;
        }
        try {
            cardManager.activateLeaderEffect(leaderIndex, getTurnState());
        } catch (Exception e) {
            sendError(e.getMessage());
        }

    }

    public void normalProductionAction(int cardSlot, boolean isBaseProduction){
        CardManager cardManager = gameMaster.getPlayerPersonalBoard(getCurrentPlayer()).getCardManager();
        try {
            if(isBaseProduction)
                cardManager.baseProductionProduce();
            else
                cardManager.developmentProduce(cardSlot);

        } catch (Exception e) {
            sendError(e.getMessage());
        }
    }

    //ANY MANAGING

    public void anyRequiredResponse(ArrayList<Resource> resources, boolean isFromBuyDevelopment){
        PersonalBoard personalBoard = gameMaster.getPlayerPersonalBoard(getCurrentPlayer());
        try {
            personalBoard.getResourceManager().convertAnyRequirement(resources, isFromBuyDevelopment);
        } catch (Exception e) {
            sendError(e.getMessage());
        }

    }

    public void anyProductionProfitResponse(ArrayList<Resource> resources){
        PersonalBoard personalBoard = gameMaster.getPlayerPersonalBoard(getCurrentPlayer());
        try {
            personalBoard.getResourceManager().convertAnyProductionProfit(resources);
        } catch (Exception e) {
            sendError(e.getMessage());
        }
    }


    //WAREHOUSE MANAGING


    public void addToStrongbox(ArrayList<Resource> resources){
        ResourceManager resourceManager = gameMaster.getPlayerPersonalBoard(getCurrentPlayer()).getResourceManager();

    }

    public void addToWarehouse(ArrayList<Resource> resources, int index) {

    }




}
