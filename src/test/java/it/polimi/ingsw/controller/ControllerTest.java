package it.polimi.ingsw.controller;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.exception.JsonFileModificationError;
import it.polimi.ingsw.model.GameMaster;
import it.polimi.ingsw.model.GameSetting;
import it.polimi.ingsw.model.card.Leader;
import it.polimi.ingsw.model.personalBoard.PersonalBoard;
import it.polimi.ingsw.model.personalBoard.market.Market;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ControllerTest {

    GameSetting gameSetting;
    GameMaster gameMaster;
    PersonalBoard personalBoard;
    Controller controller;
    ObjectMapper mapper = new ObjectMapper();
    ArrayList<Leader> leaders;

    @BeforeEach
    @Test
    void init() throws IOException, JsonFileModificationError {
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        leaders =
                mapper.readValue(new File("src/main/resources/json/leader.json"), new TypeReference<>() {});

        gameSetting = new GameSetting(3);
        ArrayList<String> players = new ArrayList<>();
        players.add("Lorenzo");
        players.add("Matteo");
        players.add("Davide");

        gameMaster = new GameMaster(gameSetting, players);
        gameMaster.nextPlayer();
        personalBoard = gameMaster.getPlayerPersonalBoard(gameMaster.getCurrentPlayer());


        controller = new Controller(gameMaster);

    }

    private void printState(){
        System.out.println("State: " + controller.getTurnState());
    }

    private Leader getLeader(int index){
        Leader leader = leaders.get(index);
        leader.attachCardToUser(personalBoard, gameMaster.getMarket());
        personalBoard.getCardManager().addLeader(leader);

        return leader;
    }

    @Test
    void marketActionWithNoLeaders() {
        printState();
        controller.marketAction(2, true);
        System.out.println(gameMaster.getMarket().getWhiteMarbleDrew());
        printState();
    }

    @Test
    void marketActionWithLeaders() {
        //1 stone
        Leader marbleLeader1 = getLeader(10);
        //1 coin
        Leader marbleLeader2 = getLeader(11);


        printState();
        controller.marketAction(2, true);

        System.out.println(gameMaster.getMarket().getResourceToSend());
        int whiteMarble = gameMaster.getMarket().getWhiteMarbleDrew();
        System.out.println("WhiteMarble: " + whiteMarble);
        printState();

        assertDoesNotThrow(()->
                controller.leaderWhiteMarbleConversion(0, 1));
        printState();

        assertDoesNotThrow(()->
                controller.leaderWhiteMarbleConversion(1, 1));
        printState();



    }


}