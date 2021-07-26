package me.ori.main.models;

import me.ori.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

public class GameScoreBoard {
    private Main main;
    private GameManager manager;

    public GameScoreBoard(GameManager manager) {

        this.manager = manager;
    }

    public void createScoreboard(Player p){

        String displayName = ChatColor.AQUA + "RocketWarsDuels" + ChatColor.RED + " !";

        ScoreboardManager sbm = Bukkit.getScoreboardManager();
        Scoreboard scoreboard = sbm.getNewScoreboard();

        Objective obj = scoreboard.registerNewObjective("scoreboard","dummy",displayName);
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        if(manager.getBlueTeam().hasBed()) {

            Score score1 = obj.getScore(ChatColor.BLUE + "Blue " + ChatColor.GRAY + "bed status: " + ChatColor.GOLD + "v");
            score1.setScore(1);
        }
        else {
            Score score1 = obj.getScore(ChatColor.BLUE + "Blue " + ChatColor.GRAY + "bed status: " + ChatColor.GOLD + "x");
            score1.setScore(1);

        }

        if(manager.getRedTeam().hasBed()) {
            Score score2 = obj.getScore(ChatColor.RED + "Red " + ChatColor.GRAY + "bed status: " + ChatColor.GOLD + "v");
            score2.setScore(2);
        }
        else {
            Score score2 = obj.getScore(ChatColor.RED + "Red " + ChatColor.GRAY + "bed status: " + ChatColor.GOLD + "x");
            score2.setScore(2);
        }





        p.setScoreboard(scoreboard);

    }


}
