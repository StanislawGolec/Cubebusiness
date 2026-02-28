package me.scpl.java.games.mcplugins.cubebusiness.logic;

import me.scpl.java.games.mcplugins.cubebusiness.CubeBusiness;
import me.scpl.java.games.mcplugins.cubebusiness.gameobjects.CBPlayer;

import java.util.ArrayList;

public class PlayerManager {

    private ArrayList<CBPlayer> cbPlayers;

    public PlayerManager(CubeBusiness plugin) {
        cbPlayers = new ArrayList<CBPlayer>();
    }

    public ArrayList<CBPlayer> getCBPlayers() {
        return cbPlayers;
    }

    public void setCBPlayers(ArrayList<CBPlayer> cbPlayers) {
        this.cbPlayers = cbPlayers;
    }

}
