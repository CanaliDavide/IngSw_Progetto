package it.polimi.ingsw.message.clientMessage;

public enum ErrorType {
    INVALID_MESSAGE ("Message is not valid"),
    INVALID_USERNAME("Username is not valid"),
    NOT_YOUR_TURN("It is not your turn, please wait!"),
    NOT_SINGLE_PLAYER_MODE("You are not playing in single player mode"),
    INVALID_ACTION("Invalid Action"),
    FAIL_RECONNECTION("Not able to reconnect"),
    FAIL_GAME_LOADING("Error in match creation");
    //GAME ERROR


    private final String message;

    ErrorType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
