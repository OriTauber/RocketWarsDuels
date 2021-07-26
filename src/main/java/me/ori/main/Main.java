package me.ori.main;

import me.ori.main.models.GameCommand;
import me.ori.main.models.GameListeners;
import me.ori.main.models.GameManager;
import me.ori.main.models.ItemManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {
    private GameManager gameManager;
    private GameManager gameManager2;

    private GameListeners listener1;
    private GameListeners listener2;

    private final Location game1spawn = new Location(Bukkit.getWorld("world"),9,22,24);
    private final Location game2spawn = new Location(Bukkit.getWorld("world"),-201,32,-100);

    @Override
    public void onEnable() {
        PluginManager pm = Bukkit.getPluginManager();
        gameManager = new GameManager(1,this);
        gameManager2 = new GameManager(2,this);
        listener1 = new GameListeners(gameManager);
        listener2 = new GameListeners(gameManager2);
        ItemManager.init();
        pm.registerEvents(listener1,this);
        pm.registerEvents(listener2,this);
        this.getCommand("rocketwarsduels").setExecutor(new GameCommand(gameManager,gameManager2));
        // Plugin startup logic

    }

    @Override
    public void onDisable() {


        // Plugin shutdown logic
    }

    public GameListeners getListener(int id){
        if (id == 1){
            return listener1;
        }
        else if(id == 2){
            return listener2;
        }
        return null;
    }

    public Location getSpawnById(int id){
        if (id == 1){
            return game1spawn;
        }
        else if(id == 2){
            return game2spawn;
        }
        return null;
    }



}
