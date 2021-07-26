package me.ori.main.models;

import me.ori.main.Main;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Bed;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.ScoreboardManager;

import java.util.ArrayList;

public class GameManager {
    private boolean canPlayersMove;
    private Location blueSpawn;
    private Location redSpawn;

    public GameStage getGameStage() {
        return gameStage;
    }

    public boolean isCanPlayersMove() {
        return canPlayersMove;
    }

    private GameStage gameStage;
    private Team redTeam;

    public GameScoreBoard getScoreBoard() {
        return scoreBoard;
    }

    private GameScoreBoard scoreBoard;

    public Team getRedTeam() {
        return redTeam;
    }

    public Team getBlueTeam() {
        return blueTeam;
    }

    public Main getMain() {
        return main;
    }

    private Main main;
    private Team blueTeam;

    public boolean isPlaying() {
        return isPlaying;
    }

    private boolean isPlaying;

    public boolean canPlayersMove() {
        return canPlayersMove;
    }

    public int getId() {
        return id;
    }

    private int id;

    public Location getBlueBedLoc() {
        return blueBedLoc;
    }

    public Location getRedBedLoc() {
        return redBedLoc;
    }

    private Location blueBedLoc;
    private Location redBedLoc;

    private Location lobbyLoc;

    public GameManager(int id,Main main) {
        gameStage = GameStage.BEFORE_START;
        isPlaying = false;

        redTeam = new Team();
        blueTeam = new Team();

        canPlayersMove = true;
        scoreBoard = new GameScoreBoard(this);
        this.main = main;
        this.id = id;

        blueSpawn = main.getSpawnById(id).clone().add(0,0,34);
        redSpawn = main.getSpawnById(id).clone().add(0,0,-27);
        lobbyLoc = new Location(Bukkit.getWorld("world"),-158,28,113);

        switch (id){
            case 1:
                blueBedLoc = new Location(Bukkit.getWorld("world"),9,22,53);
                redBedLoc = new Location(Bukkit.getWorld("world"),9,22,-2);
                break;
            case 2:
                blueBedLoc = new Location(Bukkit.getWorld("world"),-200,32,-70);
                redBedLoc = new Location(Bukkit.getWorld("world"),-200,32,-122);
                break;
        }

    }

    public void addPlayerToTeam(Player p, boolean toBlue) {

        if (toBlue) {
            if (redTeam.isPlayerInTeam(p))
                redTeam.removePlayer(p);
            blueTeam.addPlayer(p);
            return;
        }
        if (blueTeam.isPlayerInTeam(p))
            blueTeam.removePlayer(p);
        redTeam.addPlayer(p);
    }

    public int getAmountOfJoinedPlayers() {
        return redTeam.size() + blueTeam.size();
    }

    public ArrayList<Player> getAllInGamePlayers() {
        ArrayList<Player> listOne = new ArrayList<>(redTeam.getPlayers());
        ArrayList<Player> mergedList = new ArrayList<>(blueTeam.getPlayers());
        mergedList.addAll(listOne);
        return mergedList;

    }

    public void start() {
        main.getListener(id).getPlayerPlacedBlocks().clear();
        setBed(Bukkit.getWorld("world").getBlockAt(blueBedLoc),BlockFace.NORTH,Material.RED_BED);
        setBed(Bukkit.getWorld("world").getBlockAt(redBedLoc),BlockFace.SOUTH,Material.RED_BED);



        canPlayersMove = false;

        ItemStack rockets = new ItemStack(Material.FIREWORK_ROCKET);
        rockets.setAmount(64);
        FireworkMeta fwmeta = (FireworkMeta) rockets.getItemMeta();
        fwmeta.setPower(4);
        fwmeta.addEffect(FireworkEffect.builder().withFade(Color.LIME).withColor(Color.LIME).trail(true).build());
        fwmeta.setDisplayName("§aFirework");
        rockets.setItemMeta(fwmeta);


        ItemStack blocks;


        for (Player p : getAllInGamePlayers()) {
            p.getInventory().clear();
            scoreBoard.createScoreboard(p);

            p.teleport(main.getSpawnById(id).clone().add(0,5,0));


            if(getTeamColor(p).equalsIgnoreCase("Red")) {
                blocks = new ItemStack(Material.RED_WOOL);
                blocks.setAmount(64);
            }
            else {
                blocks = new ItemStack(Material.BLUE_WOOL);
                blocks.setAmount(64);
            }


            ItemStack finalBlocks = blocks;
            new BukkitRunnable() {

                int count = 5;

                @Override
                public void run() {
                    p.sendTitle(ChatColor.GOLD + "The game starts in:", ChatColor.AQUA + "" + count, 10, 20, 10);
                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 5, 5);
                    if (count == 0) {
                        p.sendTitle(ChatColor.GOLD + "The Game Started!", "", 10, 20, 10);
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 5, 5);
                        cancel();
                        p.getInventory().addItem(ItemManager.rocketsCrossbow);
                        p.getInventory().setItemInOffHand(rockets);
                        p.getInventory().setItem(30, finalBlocks);
                        p.setHealth(20);
                        isPlaying = true;
                        gameStage = GameStage.PLAYING;
                        canPlayersMove = true;
                        respawn(p);
                        return;
                    }
                    count--;

                }
            }.runTaskTimer(Main.getPlugin(Main.class),0,20L);

        }
    }


    public boolean isPlayerInGame(Player p) {
        return getAllInGamePlayers().contains(p);
    }

    public Team getTeam(Player p){
        if(redTeam.getPlayers().contains(p))
            return redTeam;
        else if(blueTeam.getPlayers().contains(p))
            return blueTeam;
        return null;

    }
    public String getTeamColor(Player p){
        if(redTeam.getPlayers().contains(p))
            return "Red";
        else if(blueTeam.getPlayers().contains(p))
            return "Blue";
        return null;

    }

    public void stop(Player died){
        gameStage = GameStage.ENDED;
        isPlaying = false;
        for (Player p : getAllInGamePlayers()){
            p.getInventory().clear();
            p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());

            p.teleport(main.getSpawnById(id).clone().add(0,5,0));

            p.teleport(lobbyLoc);
        }
        if (getTeamColor(died).equalsIgnoreCase("Red")) {
            for (Player pl : getBlueTeam().getPlayers()) {
                pl.sendTitle(ChatColor.GOLD + "Your team won!", ChatColor.AQUA + "Thanks for playing!", 20, 80, 20);
            }
            for (Player pl : getRedTeam().getPlayers()) {
                pl.sendTitle(ChatColor.DARK_RED + "Your team lost :(", ChatColor.AQUA + "Thanks for playing!", 20, 80, 20);
            }
        } else {
            for (Player pl : getBlueTeam().getPlayers()) {
                pl.sendTitle(ChatColor.DARK_RED + "Your team lost :(", ChatColor.AQUA + "Thanks for playing!", 20, 80, 20);
            }
            for (Player pl : getRedTeam().getPlayers()) {
                pl.sendTitle(ChatColor.GOLD + "Your team won!", ChatColor.AQUA + "Thanks for playing!", 20, 80, 20);
            }

        }
        for (Block b : main.getListener(id).getPlayerPlacedBlocks()){
            b.setType(Material.AIR);
        }

        main.getListener(id).getPlayerPlacedBlocks().clear();

    }



    public void respawn(Player p){
        if(!isPlayerInGame(p))
            return;
        if(!isPlaying)
            return;
        if(redTeam.getPlayers().contains(p)) {
            p.teleport(redSpawn);
            return;
        }
        p.teleport(blueSpawn);
    }

    public void setBed(Block start, BlockFace facing, Material material) {
        for (Bed.Part part : Bed.Part.values()) {
            start.setBlockData(Bukkit.createBlockData(material, (data) -> {
                ((Bed) data).setPart(part);
                ((Bed) data).setFacing(facing);
            }));
            start = start.getRelative(facing.getOppositeFace());
        }
    }




}
