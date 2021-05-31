package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ClientMessageHandler;
import it.polimi.ingsw.client.ClientState;
import it.polimi.ingsw.client.GUI.controller.*;
import it.polimi.ingsw.client.data.CardLeaderData;
import it.polimi.ingsw.client.data.ResourceData;
import it.polimi.ingsw.message.bothArchitectureMessage.ConnectionMessage;
import it.polimi.ingsw.message.clientMessage.*;
import it.polimi.ingsw.model.resource.ResourceType;
import javafx.application.Platform;
import java.util.ArrayList;
import java.util.stream.Collectors;


public class GUIMessageHandler extends ClientMessageHandler {
    private final Client client = Client.getInstance();
    private final ControllerHandler controllerHandler = ControllerHandler.getInstance();



    @Override
    public void handleError(ErrorMessage message) {
        String error = (message.getErrorType() == null)?message.getCustomError():message.getErrorType().getMessage();
        Platform.runLater(()->{
            controllerHandler.getCurrentController().showCustomMessage(error);
        });
    }

    @Override
    public void connectNewUser(ConnectionMessage message) {

    }

    @Override
    public void waitingPeople(ConnectionMessage message) {
        WaitingController controller = (WaitingController) controllerHandler.getController(Views.WAITING);
        Platform.runLater(()->{
            controller.setLobbyMessage();
            controllerHandler.changeView(Views.WAITING);
        });
    }

    @Override
    public void username(ConnectionMessage message) {
        SetupController setupController = (SetupController) controllerHandler.getController(Views.SETUP);
        Platform.runLater(()->{
            setupController.showInsertUsername();
            if (controllerHandler.getCurrentView() == Views.MAIN_MENU ||
            controllerHandler.getCurrentView() == Views.WAITING){
                controllerHandler.changeView(Views.SETUP);
            }
        });

    }

    @Override
    public void newTurn(StarTurn message) {
        super.newTurn(message);
        String msg = "Is "+ message.getUsername() + " turn";
        Platform.runLater(()->{
            controllerHandler.getCurrentController().showCustomMessage(msg);
        });


    }

    @Override
    public void numberOfPlayer(ConnectionMessage message) {
        Platform.runLater(()->controllerHandler.changeView(Views.SETUP));
    }

    @Override
    public void connectInfo(ConnectionMessage message) {

    }



    @Override
    public void leaderSetUp(LeaderSetUpMessage message) {
        super.leaderSetUp(message);
        PreGameSelectionController controller = (PreGameSelectionController) controllerHandler.getController(Views.PRE_MATCH);
        ArrayList<String> paths = message.getLeaders().stream().map(CardLeaderData::toResourcePath)
                .collect(Collectors.toCollection(ArrayList::new));

        Platform.runLater(()->{
            controllerHandler.changeView(Views.PRE_MATCH);
            controller.setLeaderImages(paths);
        });
    }

    @Override
    public void mainMenu() {
        Platform.runLater(()->controllerHandler.changeView(Views.MAIN_MENU));
    }

    @Override
    public void startGame() {
        Platform.runLater(()->controllerHandler.changeView(Views.PERSONAL_BOARD));
    }

    @Override
    public void gameSetUp(GameSetup message) {
        super.gameSetUp(message);

        ArrayList<ResourceData> resources = new ArrayList<>();
        resources.add(new ResourceData(ResourceType.COIN, 3));
        resources.add(new ResourceData(ResourceType.SERVANT, 3));
        resources.add(new ResourceData(ResourceType.STONE, 4));
        resources.add(new ResourceData(ResourceType.SHIELD, 5));

        Platform.runLater(()->{
            PersonalBoardController pbController = (PersonalBoardController) controllerHandler.getController(Views.PERSONAL_BOARD);
            pbController.setUpResourceFromMarket(resources);
        });



        Platform.runLater(()->{
            DeckDevelopmentController deckController = (DeckDevelopmentController) controllerHandler.getController(Views.DECK_DEV);
            deckController.setUpDeckImages(message.getDeckDev());
        });
    }

    @Override
    public void anyConversionRequest(AnyConversionRequest message) {
        if(client.getState() == ClientState.ENTERING_LOBBY){
            Platform.runLater(()->{
                if (message.getNumOfAny() == 0){
                    WaitingController controller = (WaitingController) controllerHandler.getController(Views.WAITING);
                    controller.setPreMatchMessage();
                    controllerHandler.changeView(Views.WAITING);
                }else{
                    PreGameSelectionController controller = (PreGameSelectionController) controllerHandler.getController(Views.PRE_MATCH);
                    controller.setHowManyRes(message.getNumOfAny());
                    controller.showChooseResourcesBox();
                }
            });
        }
    }

    @Override
    public void leaderDiscardUpdate(LeaderDiscard message) {
        super.leaderDiscardUpdate(message);
        Platform.runLater(()->{
            PersonalBoardController personalBoardController = (PersonalBoardController) ControllerHandler.getInstance().getController(Views.PERSONAL_BOARD);
            if(message.getUsername().equals(personalBoardController.getCurrentShowed())) {
                personalBoardController.resetLeader();
                personalBoardController.setDisableLeaderBtn(true);
                personalBoardController.loadLeader(Client.getInstance().getModelOf(message.getUsername()).toModelData());
            }
        });
    }

    @Override
    public void bufferUpdate(BufferUpdate message) {

    }

    @Override
    public void manageResourceRequest(ManageResourcesRequest message) {

    }

    @Override
    public void whiteMarbleConversion(WhiteMarbleConversionRequest message) {

    }

    @Override
    public void faithTrackPositionIncreased(FaithTrackIncrement message) {
        super.faithTrackPositionIncreased(message);
        Platform.runLater(()->{
            PersonalBoardController personalBoardController = (PersonalBoardController) ControllerHandler.getInstance().getController(Views.PERSONAL_BOARD);
            if(message.getUsername().equals(personalBoardController.getCurrentShowed())) {
                personalBoardController.resetFaithTrack();
                personalBoardController.loadFaithTrack(Client.getInstance().getModelOf(message.getUsername()).toModelData());
            }
        });
    }

    @Override
    public void popeFavorActivation(PopeFavorActivated message) {
        super.popeFavorActivation(message);
        Platform.runLater(()->{
            PersonalBoardController personalBoardController = (PersonalBoardController) ControllerHandler.getInstance().getController(Views.PERSONAL_BOARD);
            if(message.getUsername().equals(personalBoardController.getCurrentShowed())) {
                personalBoardController.resetFaithTrack();
                personalBoardController.loadFaithTrack(Client.getInstance().getModelOf(message.getUsername()).toModelData());
            }
        });
    }

    @Override
    public void winningCondition() {

    }


}
