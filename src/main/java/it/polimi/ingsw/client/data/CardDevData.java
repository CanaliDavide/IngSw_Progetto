package it.polimi.ingsw.client.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.client.PrintAssistant;
import it.polimi.ingsw.model.card.Color;

import java.util.ArrayList;

public class CardDevData {
    private int level;
    private int victoryPoints;
    private ColorData color;
    private ArrayList<ResourceData> resourceReq;

    private ArrayList<EffectData> effects;



    public CardDevData(@JsonProperty("level")int level,
                       @JsonProperty("victoryPoints")int victoryPoints,
                       @JsonProperty("color")ColorData color,
                       @JsonProperty("resourceReq")ArrayList<ResourceData> resourceReq) {
        this.level = level;
        this.victoryPoints = victoryPoints;
        this.color = color;
        this.resourceReq = resourceReq;
    }

    @JsonCreator(mode= JsonCreator.Mode.PROPERTIES)
    public CardDevData(@JsonProperty("level")int level,
                       @JsonProperty("victoryPoints")int victoryPoints,
                       @JsonProperty("color")ColorData color,
                       @JsonProperty("resourceReq")ArrayList<ResourceData> resourceReq,
                       @JsonProperty("effects")ArrayList<EffectData> effects){
        this.level = level;
        this.victoryPoints = victoryPoints;
        this.color = color;
        this.resourceReq = resourceReq;
        this.effects = effects;
    }

    public CardDevData(int level, ColorData color){
        this.level=level;
        this.color=color;
    }

    public int getLevel() {
        return level;
    }

    public int getVictoryPoints() {
        return victoryPoints;
    }

    public ColorData getColor() {
        return color;
    }

    public String colorToColor(){
        String s="";
        switch (color){
            case BLUE:
                s = PrintAssistant.ANSI_BLUE_BACKGROUND;
                break;
            case GREEN:
                s = PrintAssistant.ANSI_GREEN_BACKGROUND;
                break;
            case PURPLE:
                s = PrintAssistant.ANSI_PURPLE_BACKGROUND;
                break;
            case YELLOW:
                s = PrintAssistant.ANSI_YELLOW_BACKGROUND;
                break;
            case WHITE:
                s = PrintAssistant.ANSI_RESET;
                break;
        }
        return s;
    }

    public ArrayList<ResourceData> getResourceReq() {
        return resourceReq;
    }


    public ArrayList<EffectData> getEffects() {
        return effects;
    }

    public String cardHeader(int width){
        String header = "CARD LV" + level +" +" +victoryPoints+"VP ";
        header = PrintAssistant.instance.stringBetweenChar(header, ' ', width - 2, ' ', ' ');
        header = "|" + colorToColor() + header + PrintAssistant.ANSI_RESET + "|";
        return header;
    }

    public String cardCost(int width, int offSet){
        StringBuilder row = new StringBuilder();
        for(ResourceData resReq : resourceReq){
            row.append(resReq.toCli());
        }
        return PrintAssistant.instance.fitToWidth(row.toString(), width, ' ', '|', '|', offSet);
    }

    public String cardProductionCost(int width, int offSet){
        return PrintAssistant.instance.fitToWidth(effects.get(0).resourceBeforeToCli(), width, ' ', '|', '|', offSet);
    }

    public String cardProductionEarn(int width, int offSet){
        return PrintAssistant.instance.fitToWidth(effects.get(0).resourceAfterToCli(), width, ' ', '|', '|', offSet);
    }

    public String cardEnd(int width){
        String end = PrintAssistant.instance.stringBetweenChar("END CARD", ' ', width - 2, ' ', ' ');
        end = "|" + colorToColor() + end + PrintAssistant.ANSI_RESET + "|";
        return end;
    }
    public String toCLIForLeader(){
        String s;
        s=colorToColor()+PrintAssistant.ANSI_BLACK+" DEV LV"+getLevel()+" "+PrintAssistant.ANSI_RESET+" ";
        return s;
    }
}
