package it.polimi.ingsw.message.clientMessage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.client.ClientMessageHandler;
import it.polimi.ingsw.client.data.CardLeaderData;

public class LeaderActivate implements ClientMessage{
    private final CardLeaderData leader;
    private final int leaderIndex;
    private final String username;

    @JsonCreator
    public LeaderActivate(@JsonProperty("leader") CardLeaderData leader,
                          @JsonProperty("leaderIndex") int leaderIndex,
                          @JsonProperty("username") String username) {
        this.leader = leader;
        this.leaderIndex = leaderIndex;
        this.username = username;
    }


    public CardLeaderData getLeader() {
        return leader;
    }

    public int getLeaderIndex() {
        return leaderIndex;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public void process(ClientMessageHandler handler) {

    }
}
