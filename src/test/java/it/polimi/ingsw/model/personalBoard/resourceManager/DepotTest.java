package it.polimi.ingsw.model.personalBoard.resourceManager;

import it.polimi.ingsw.exception.TooMuchResourceDepotException;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceFactory;
import it.polimi.ingsw.model.resource.ResourceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

class DepotTest {
    Resource r1, r2;
    Depot d1, d2, d3;
    @BeforeEach
    void init(){
        r1 = ResourceFactory.createResource(ResourceType.COIN, 2);
        r2 = ResourceFactory.createResource(ResourceType.COIN, 4);
        d1 = new Depot(ResourceFactory.createResource(ResourceType.COIN, 2), true, 2);
        d2 = new Depot(false, 2);
        d3 = new Depot(r1, false, 3);

    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2})
    void setResource(int index) {
        switch (index){
            case 0:
                assertThrows(TooMuchResourceDepotException.class , ()->d2.setResource(r2));
                break;
            case 1:
                //TODO: caso positivo non so come testarlo
                break;
            default:
        }
    }

    @Test
    void getResourceValue() {
        assertEquals(2, d3.getResourceValue());
    }

    @Test
    void getResourceType(){
        assertEquals(ResourceType.COIN, d3.getResourceType());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1})
    void howMuchResCanIStillStoreIn(int index) {
        switch (index){
            case 0:
                assertEquals(1, d3.howMuchResCanIStillStoreIn());
                break;
            case 1:
                Depot d4 = new Depot(r1, false, 2);
                assertEquals(0, d4.howMuchResCanIStillStoreIn());
                break;
            default:
        }
    }


    @Test
    void getResource(){
        assertDoesNotThrow(()->d2.setResource(r1));
        assertEquals(r1, d2.getResource());
    }

    @Test
    void isLockDepot(){
        assertTrue(d1.isLockDepot());
    }

    @Test
    void getMaxStorable(){
        assertEquals(2, d1.getMaxStorable());
    }

    @Test
    void setEmptyResource(){
        d1.setEmptyResource();
        assertEquals(ResourceType.COIN, d1.getResourceType());
        assertEquals(d1.getResourceValue(), 0);
    }
}