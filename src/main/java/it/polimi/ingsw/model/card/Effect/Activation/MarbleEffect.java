package it.polimi.ingsw.model.card.Effect.Activation;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.model.card.Effect.Effect;
import it.polimi.ingsw.controller.TurnState;
import it.polimi.ingsw.model.personalBoard.market.Market;
import it.polimi.ingsw.model.personalBoard.resourceManager.ResourceManager;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceFactory;
import java.util.ArrayList;


/**
 * MarbleEffect class defines the effect that concern marbles
 */
public class MarbleEffect implements Effect {
    private final ArrayList<Resource> transformIn;
    private Market market = null;

    /**
     * Constructor MarbleEffect creates a new MarbleEffect instance
     * @param transformIn of type ArrayList - the resources which we will transform each white marble drew
     */
    @JsonCreator
    public MarbleEffect(@JsonProperty("transformIn") ArrayList<Resource> transformIn) {
        this.transformIn = transformIn;
    }

    /**
     * Method doEffect is in charge of pass all the resources to
     * the market based on how many white marble the user haw drawn
     * @param turnState of type State - defines the state of the turn, in this case must be MARKET_STATE
     */
    @Override
    public void doEffect(TurnState turnState) {
        if (turnState == TurnState.WHITE_MARBLE_CONVERSION){
            int whiteMarble = market.getWhiteMarbleToTransform();
            //add resource in market
            ArrayList<Resource> conversion = new ArrayList<>();
            for (Resource res: transformIn){
                conversion.add(ResourceFactory.createResource(res.getType(), res.getValue()*whiteMarble));
            }
            market.insertLeaderResources(conversion);
        }
    }

    /**
     * Method attachMarket attach the market
     * @param market of type Market is the instance of the market of the game
     */
    @Override
    public void attachMarket(Market market) {
        this.market = market;
    }

    /**
     * Method attachResourceManager does nothing because MarbleEffect doesn't need
     * any reference to it
     * @param resourceManager of type ResourceManager is an instance of the resource manager of the player
     */
    @Override
    public void attachResourceManager(ResourceManager resourceManager) {}

    public ArrayList<Resource> getTransformIn() {
        return transformIn;
    }

    @Override
    public String toString() {
        String x = "\ntrasformIn= ";
        for(Resource res: transformIn){
            x += "{"+ res.getType().getDisplayName()+", "+res.getValue()+"}  ";
        }
        return x;
    }

}
