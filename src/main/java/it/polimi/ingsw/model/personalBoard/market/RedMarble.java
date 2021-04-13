package it.polimi.ingsw.model.personalBoard.market;

import it.polimi.ingsw.model.resource.ResourceFactory;
import it.polimi.ingsw.model.resource.ResourceType;

public class RedMarble implements Marble{

    /**
     * Method that will add a faith in market's resource to send array
     * @param market is the reference to the market of the game
     */
    @Override
    public void doMarbleAction(Market market){
        market.addInResourcesToSend(ResourceFactory.createResource(ResourceType.FAITH,1));
    }
}
