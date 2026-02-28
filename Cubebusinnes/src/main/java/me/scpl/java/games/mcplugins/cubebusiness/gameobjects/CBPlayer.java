package me.scpl.java.games.mcplugins.cubebusiness.gameobjects;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CBPlayer {

    // --- Core Identifiers ---
    private UUID playerUUID;
    private UUID pieceUUID;
    private int space;

    // --- Economy & Assets ---
    private int cash;
    private List<Integer> ownedProperties;
    private boolean isBankrupt;

    // --- Jail & Turn Logic ---
    private boolean isImprisoned;
    private int jailTurns;
    private int doublesRolled;
    private int getOutOfJailFreeCards;

    public CBPlayer(UUID playerUUID) {
        this.playerUUID = playerUUID;
        this.pieceUUID = null;
        this.space = 1; // Everyone starts on Go (Space 1)

        // Setup initial balance and lists
        this.cash = 1500;
        this.ownedProperties = new ArrayList<>();
        this.isBankrupt = false;

        // Setup default turn states
        this.isImprisoned = false;
        this.jailTurns = 0;
        this.doublesRolled = 0;
        this.getOutOfJailFreeCards = 0;
    }

    // --- Core Identifiers Getters/Setters ---

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public void setPlayerUUID(UUID playerUUID) {
        this.playerUUID = playerUUID;
    }

    public UUID getPieceUUID() {
        return pieceUUID;
    }

    public void setPieceUUID(UUID pieceUUID) {
        this.pieceUUID = pieceUUID;
    }

    public int getSpace() {
        return space;
    }

    public void setSpace(int space) {
        this.space = space;
    }

    // --- Economy & Assets Methods ---

    public int getCash() {
        return cash;
    }

    public void setCash(int cash) {
        this.cash = cash;
    }

    public void addCash(int amount) {
        this.cash += amount;
    }

    public void removeCash(int amount) {
        this.cash -= amount;
    }

    public List<Integer> getOwnedProperties() {
        return ownedProperties;
    }

    public void addProperty(int spaceId) {
        if (!ownedProperties.contains(spaceId)) {
            ownedProperties.add(spaceId);
        }
    }

    public void removeProperty(int spaceId) {
        ownedProperties.remove(Integer.valueOf(spaceId));
    }

    public boolean isBankrupt() {
        return isBankrupt;
    }

    public void setBankrupt(boolean bankrupt) {
        this.isBankrupt = bankrupt;
    }

    // --- Jail & Turn Logic Methods ---

    public boolean isImprisoned() {
        return isImprisoned;
    }

    public void setImprisoned(boolean imprisoned) {
        this.isImprisoned = imprisoned;
    }

    public int getJailTurns() {
        return jailTurns;
    }

    public void setJailTurns(int jailTurns) {
        this.jailTurns = jailTurns;
    }

    public void addJailTurn() {
        this.jailTurns++;
    }

    public int getDoublesRolled() {
        return doublesRolled;
    }

    public void setDoublesRolled(int doublesRolled) {
        this.doublesRolled = doublesRolled;
    }

    public void addDoubleRoll() {
        this.doublesRolled++;
    }

    public void resetDoubleRolls() {
        this.doublesRolled = 0;
    }

    public int getGetOutOfJailFreeCards() {
        return getOutOfJailFreeCards;
    }

    public void addGetOutOfJailFreeCard() {
        this.getOutOfJailFreeCards++;
    }

    public void useGetOutOfJailFreeCard() {
        if (this.getOutOfJailFreeCards > 0) {
            this.getOutOfJailFreeCards--;
        }
    }
}