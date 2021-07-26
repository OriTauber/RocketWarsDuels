package me.ori.main.models;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Team {
    public ArrayList<Player> getPlayers() {
        return players;
    }

    private ArrayList<Player> players;

    public boolean hasBed() {
        return hasBed;
    }

    public void setBed(boolean hasBed) {
        this.hasBed = hasBed;
    }

    public boolean HasBed() {
        return hasBed;
    }

    private boolean hasBed;

    public Location getBedLoc() {
        return bedLoc;
    }

    public void setBedLoc(Location bedLoc) {
        this.bedLoc = bedLoc;
    }

    private Location bedLoc;






    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public Team() {
        players = new ArrayList<>();
        bedLoc = new Location(Bukkit.getWorld("world"),0,0,0);
        hasBed = true;
    }


    public void addPlayer(Player p){
        players.add(p);
    }
    public void removePlayer(Player p){
        players.remove(p);
    }
    public int size(){
        return players.size();
    }
    public boolean isPlayerInTeam(Player p){
        return players.contains(p);
    }


}
