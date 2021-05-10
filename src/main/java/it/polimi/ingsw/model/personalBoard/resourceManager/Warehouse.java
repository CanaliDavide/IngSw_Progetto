package it.polimi.ingsw.model.personalBoard.resourceManager;

import it.polimi.ingsw.exception.InvalidOrganizationWarehouseException;
import it.polimi.ingsw.exception.NegativeResourceException;
import it.polimi.ingsw.exception.TooMuchResourceDepotException;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceType;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class Warehouse implements Cloneable{
    private ArrayList<Depot> depots = new ArrayList<>();
    private ArrayList<Depot> depotsLeader = new ArrayList<>();

    public Warehouse() {
        for (int i = 0; i < 3; i++) {
            depots.add(new Depot(false, i+1));
        }
    }

    /**
     * Constructor to clone the warehouse when we enter the edit mode*/
    public Warehouse(ArrayList<Depot> depots, ArrayList<Depot> depotsLeader){
        this.depots=depots;
        this.depotsLeader=depotsLeader;
    }





    /**
     * Get a specific standard depot
     * @param index of the depot i want
     * @return the depot at index pos*/
    public Depot getDepot(int index) throws IndexOutOfBoundsException{
        return depots.get(index);
    }

    /**
     * Get a specific leader depot
     * @param index of the depot i want
     * @return the depot at index pos*/
    public Depot getDepotLeader(int index) throws IndexOutOfBoundsException{
        return depotsLeader.get(index);
    }

    /**
     * Method used to add a depot to the leader list when a WarehouseLeader is active
     * @param depot i want to add*/
    public void addDepotLeader(Depot depot){
        depotsLeader.add(depot);
    }



    /**
     * call the private modifyDepotValue with the leader depots list
     * @param indexDepot the index i want to modify in the normal depot list
     * @param resource the resource to sum*/
    public void addDepotResourceAt(int indexDepot, Resource resource, boolean isNormalDepot) throws TooMuchResourceDepotException, IndexOutOfBoundsException, InvalidOrganizationWarehouseException {
        Depot depot;
        if(isNormalDepot) depot = depots.get(indexDepot);
        else depot = depotsLeader.get(indexDepot);

        if(depot.getResourceType()!=resource.getType() && depot.getResourceType() != ResourceType.ANY){
            throw new InvalidOrganizationWarehouseException("You try to add a resource type different from his own");
        }
        if(isNormalDepot && (depot.getResourceType() == ResourceType.ANY) && doIHaveADepotWith(resource.getType())){
            throw new InvalidOrganizationWarehouseException("You can't add " + resource.getType() +" because you " +
                    "have those already stored in another depot");
        }
        depot.addResource(resource);
    }




    /**
     * call the private modifyDepotValue with the standard depots list
     *@param indexDepot the index i want to modify in the leader depot list
     *@param resource the resource to sub*/
    public void subDepotResourceAt(int indexDepot, Resource resource, boolean isNormalDepot) throws NegativeResourceException, IndexOutOfBoundsException, InvalidOrganizationWarehouseException {
        Depot depot;
        if(isNormalDepot) depot = depots.get(indexDepot);
        else depot = depotsLeader.get(indexDepot);

        depot.subResource(resource);
    }

    /**
     * used to know if there's already a depot with that resource type in the warehouse
     * @param type: the type i'm looking at
     * @return true if i already have one, false if i don't*/
    public boolean doIHaveADepotWith(ResourceType type){
        for(Depot dep: depots){
            if(dep.getResourceType()==type){
                return true;
            }
        }
        return false;
    }


    /**
     * Remove the resource from the standard depot
     * @param indexDepot of depot i want to empty
     * @return the resource i took from the index depot*/
    public Resource popResourceFromDepotAt(int indexDepot, boolean isNormalDepot) throws IndexOutOfBoundsException{
        Depot depot;
        if(isNormalDepot) depot = depots.get(indexDepot);
        else depot = depotsLeader.get(indexDepot);

        Resource resource = depot.getResource();
        depot.setEmptyResource();
        return resource;
    }

    /**
     * used to know how much resource of a single type i'm storing in the warehouse(Leader + Normal)
     * @param resourceType i want to find
     * @return the amount value of resourceType i own*/
    public int howManyDoIHave(ResourceType resourceType){
        int num=0;
        for(Depot dep:depots){
            if(dep.getResourceType()==resourceType){
                num+=dep.getResourceValue();
            }
        }
        for(Depot dep : depotsLeader){
            if(dep.getResourceType()==resourceType){
                num+=dep.getResourceValue();
            }
        }
        return num;
    }

    /**
     * Copy the standard array depot
     * @return a clone of the standard depots*/
    public ArrayList<Depot> copyDepots(){
        return depots.stream()
                .map(Dep -> new Depot(Dep.getResource(), Dep.isLockDepot(), Dep.getMaxStorable()))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Copy the leader array depot
     * @return a clone of the leader depots*/
    public ArrayList<Depot> copyDepotsLeader(){
        return depotsLeader.stream()
                .map(Dep -> new Depot(Dep.getResource(), Dep.isLockDepot(), Dep.getMaxStorable()))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Made for testing purposes!*/
    public void print(){
        System.out.println("===================WAREHOUSE======================");

        System.out.println("Normal depot:");
        for(Depot  dep: depots){
            System.out.println(dep);
        }
        System.out.println("Leader depot:");
        for(Depot  dep: depotsLeader){
            System.out.println(dep);
        }
        //System.out.println("Resource to manage:"+ resourceToManage.getType()+"("+resourceToManage.getValue()+")");
        System.out.println("==================================================");
    }
}
