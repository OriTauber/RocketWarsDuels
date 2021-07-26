package me.ori.main.models;

import me.ori.main.Main;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GameListeners implements Listener {
    private GameManager manager;
    private HashMap<String, Long> crossbowCooldown = new HashMap<>();
    public Inventory selectInv = SelectInventory.createInventory();
    private int placesNumber = 0;

    public ArrayList<Block> getPlayerPlacedBlocks() {
        return playerPlacedBlocks;
    }

    private ArrayList<Block> playerPlacedBlocks;

    public GameListeners(GameManager manager) {
        this.manager = manager;
        playerPlacedBlocks = new ArrayList<>();

    }

    @EventHandler
    public void onCrossbowHit(EntityDamageByEntityEvent e) {
        if (!manager.isPlaying())
            return;
        if (!(manager.getGameStage() == GameStage.PLAYING))
            return;
        if (!(e.getEntity() instanceof Player))
            return;
        if (!(e.getDamager() instanceof Firework))
            return;
        Firework firework = (Firework) e.getDamager();
        if (!(firework.getShooter() instanceof Player))
            return;

        Player damager = (Player) firework.getShooter();
        Player damaged = (Player) e.getEntity();

        if (!manager.isPlayerInGame(damaged) || manager.isPlayerInGame(damager))
            return;

        if (!(damager.getInventory().getItemInMainHand() == ItemManager.rocketsCrossbow))
            return;
        e.setDamage(10.0D);
        damager.sendMessage(ChatColor.AQUA + "You Just hit " + damaged.getName() + "!");
    }


    @EventHandler
    public void onCrossBowShoot(EntityShootBowEvent e) {
        if (!manager.isPlaying())
            return;
        if (!(manager.getGameStage() == GameStage.PLAYING))
            return;

        if (!(e.getBow().getType() == Material.CROSSBOW))
            return;
        if (!(e.getProjectile() instanceof Firework))
            return;
        if(!(e.getEntity() instanceof Player))
            return;
        Firework firework = (Firework) e.getProjectile();

        if (!(firework.getShooter() instanceof Player))
            return;

        Player damager = (Player) firework.getShooter();
        Player damaged = (Player) e.getEntity();

        if (!manager.isPlayerInGame(damager))
            return;
        if(!manager.isPlayerInGame(damaged)){
            return;
        }
        if(damaged.getName() == damager.getName())
            return;

        if (crossbowCooldown.containsKey(damager.getName())) {

            if (crossbowCooldown.get(damager.getName()) > System.currentTimeMillis() / 1000) {

                long timeleft = (crossbowCooldown.get(damager.getName()) - System.currentTimeMillis() / 1000);
                damager.sendMessage(ChatColor.BLUE + "Your bow is on cooldown for the next " + timeleft + " seconds!");
                e.setCancelled(true);
                return;
            }
        }
        crossbowCooldown.put(damager.getName(), System.currentTimeMillis() / 1000 + 3);
        if(damaged == damager)
            return;


        damager.sendMessage(ChatColor.AQUA + "You shot " + damaged.getName() + "! He's now on " + damaged.getHealth());
        damaged.sendMessage(ChatColor.AQUA + "" + damager.getName() + "Shot you!");

    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (manager.getId() == 1) {

            Player p = (Player) e.getWhoClicked();

            if (!e.getView().getTitle().equalsIgnoreCase(ChatColor.GOLD + "Select Team"))
                return;
            if (!(manager.getGameStage() == GameStage.BEFORE_START)) {
                p.sendMessage(ChatColor.DARK_RED + "You can only choose your team before the game starts!");
                p.closeInventory();
                e.setCancelled(true);
                return;
            }


            if (e.getCurrentItem() != null) {
                //boolean someEmpty = manager.getBlueTeam().
                boolean sizeEquals = manager.getBlueTeam().size() == manager.getRedTeam().size();
                boolean blueLarger = manager.getBlueTeam().size() > manager.getRedTeam().size();
                boolean redLarger = manager.getRedTeam().size() > manager.getBlueTeam().size();


                if (e.getCurrentItem().getType() == Material.RED_WOOL) {


                    if (sizeEquals || blueLarger) {

                        p.sendMessage(ChatColor.AQUA + "You Choosed The " + ChatColor.RED + "Red" + ChatColor.AQUA + " Team!");

                        manager.addPlayerToTeam(p, false);
                        e.setCancelled(true);
                    } else {
                        e.setCancelled(true);
                        p.sendMessage(ChatColor.DARK_RED + "This team has too many players!");
                    }

                } else if (e.getCurrentItem().getType() == Material.BLUE_WOOL) {
                    if (sizeEquals || redLarger) {

                        p.sendMessage(ChatColor.AQUA + "You Choosed The " + ChatColor.BLUE + "Blue" + ChatColor.AQUA + " Team!");
                        manager.addPlayerToTeam(p, true);
                        e.setCancelled(true);
                    } else {
                        e.setCancelled(true);
                        p.sendMessage(ChatColor.DARK_RED + "This team has too many players!");
                    }

                } else {
                    e.setCancelled(true);
                }

            }

        }
        else if(manager.getId() == 2){

            Player p = (Player) e.getWhoClicked();

            if (!e.getView().getTitle().equalsIgnoreCase(ChatColor.GOLD + "Select Team"))
                return;
            if (!(manager.getGameStage() == GameStage.BEFORE_START)) {
                p.sendMessage(ChatColor.DARK_RED + "You can only choose your team before the game starts!");
                p.closeInventory();
                e.setCancelled(true);
                return;
            }


            if (e.getCurrentItem() != null) {
                //boolean someEmpty = manager.getBlueTeam().
                boolean sizeEquals = manager.getBlueTeam().size() == manager.getRedTeam().size();
                boolean blueLarger = manager.getBlueTeam().size() > manager.getRedTeam().size();
                boolean redLarger = manager.getRedTeam().size() > manager.getBlueTeam().size();


                if (e.getCurrentItem().getType() == Material.RED_WOOL) {


                    if (sizeEquals || blueLarger) {

                        p.sendMessage(ChatColor.AQUA + "You Choosed The " + ChatColor.RED + "Red" + ChatColor.AQUA + " Team!");

                        manager.addPlayerToTeam(p, false);
                        e.setCancelled(true);
                    } else {
                        e.setCancelled(true);
                        p.sendMessage(ChatColor.DARK_RED + "This team has too many players!");
                    }

                } else if (e.getCurrentItem().getType() == Material.BLUE_WOOL) {
                    if (sizeEquals || redLarger) {

                        p.sendMessage(ChatColor.AQUA + "You Choosed The " + ChatColor.BLUE + "Blue" + ChatColor.AQUA + " Team!");
                        manager.addPlayerToTeam(p, true);
                        e.setCancelled(true);
                    } else {
                        e.setCancelled(true);
                        p.sendMessage(ChatColor.DARK_RED + "This team has too many players!");
                    }

                } else {
                    e.setCancelled(true);
                }

            }

        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        if (!manager.isPlaying())
            return;
        if (!(manager.getGameStage() == GameStage.PLAYING))
            return;
        if (!manager.isPlayerInGame(e.getPlayer()))
            return;
        e.setDropItems(false);
        if (e.getBlock().getType() == Material.RED_BED) {
            Block b = e.getBlock();

            if (b.getLocation().equals(manager.getBlueBedLoc())) {
                if (manager.getRedTeam().isPlayerInTeam(e.getPlayer())) {
                    manager.getBlueTeam().setBed(false);

                    for (Player p : manager.getRedTeam().getPlayers()) {
                        p.sendTitle(ChatColor.RED + e.getPlayer().getName() + ChatColor.AQUA + "Broke The " + ChatColor.BLUE + "Blue " + ChatColor.AQUA + "Bed!", "", 10, 40, 10);

                    }
                    for (Player p : manager.getBlueTeam().getPlayers()) {
                        p.sendTitle(ChatColor.RED + e.getPlayer().getName() + ChatColor.AQUA + "Broke The " + ChatColor.BLUE + "Blue " + ChatColor.AQUA + "Bed!", "", 10, 40, 10);
                    }
                    for (Player p : manager.getAllInGamePlayers()) {
                        manager.getScoreBoard().createScoreboard(p);
                    }

                    return;
                }
                else {
                    e.getPlayer().sendMessage(ChatColor.AQUA + "You can't break your own bed!");
                }

            }

            if(b.getLocation().equals(manager.getRedBedLoc())) {
                if (manager.getBlueTeam().isPlayerInTeam(e.getPlayer())) {
                    manager.getRedTeam().setBed(false);
                    for (Player p : manager.getBlueTeam().getPlayers()) {
                        p.sendTitle(ChatColor.BLUE + e.getPlayer().getName() + ChatColor.AQUA + "Broke The " + ChatColor.RED + "Red " + ChatColor.AQUA + "Bed!", "", 10, 40, 10);

                    }
                    for (Player p : manager.getBlueTeam().getPlayers()) {
                        p.sendTitle(ChatColor.BLUE + e.getPlayer().getName() + ChatColor.AQUA + "Broke The " + ChatColor.RED + "Red " + ChatColor.AQUA + "Bed!", "", 10, 40, 10);

                    }
                    for (Player p : manager.getAllInGamePlayers()) {
                        manager.getScoreBoard().createScoreboard(p);

                    }
                    return;
                }
                else {
                    e.getPlayer().sendMessage(ChatColor.AQUA + "You can't break your own bed!");
                }
            }

            e.setCancelled(true);

        } else {
            if (!playerPlacedBlocks.contains(e.getBlock())) {
                e.getPlayer().sendMessage(ChatColor.DARK_RED + "You can only break blocks placed by a player!");
                e.setCancelled(true);
            }
            Block b = e.getBlock();
            World world = b.getWorld();
            Location firstBlockLoc = b.getLocation().add(0,1,0);
            if(playerPlacedBlocks.contains(world.getBlockAt(firstBlockLoc))){
                ArrayList<Block> blocksAbove = new ArrayList<>();
                while (!firstBlockLoc.getBlock().isEmpty()){
                    blocksAbove.add(world.getBlockAt(firstBlockLoc));
                    firstBlockLoc.add(0,1,0);
                }


                ArrayList<FallingBlock> fallingBlocks = new ArrayList<>();
                for(Block block : blocksAbove){
                    FallingBlock fblock = world.spawnFallingBlock(block.getLocation(),block.getBlockData());
                    fblock.setDropItem(false);
                    fblock.setHurtEntities(true);
                    block.setType(Material.AIR);
                    fallingBlocks.add(fblock);
                }
                new BukkitRunnable(){

                    @Override
                    public void run() {
                        for(FallingBlock fallingBlock : fallingBlocks){
                            world.getBlockAt(fallingBlock.getLocation()).setType(fallingBlock.getBlockData().getMaterial());

                        }


                    }
                }.runTaskLater(Main.getPlugin(Main.class),10L);

            }

        }


    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (!manager.isPlaying())
            return;
        if (!(manager.getGameStage() == GameStage.PLAYING))
            return;
        if (!manager.isPlayerInGame(e.getPlayer()))
            return;
        if (manager.canPlayersMove())
            return;
        e.setCancelled(true);


    }

    @EventHandler
    public void onInvMove(InventoryMoveItemEvent e) {
        if (!manager.isPlaying())
            return;
        if (!(manager.getGameStage() == GameStage.PLAYING))
            return;

        if (e.getItem().getType() == Material.FIREWORK_ROCKET)
            e.setCancelled(true);
    }

    @EventHandler
    public void obBlockPlace(BlockPlaceEvent e) {
        if (!manager.isPlaying())
            return;
        if (!(manager.getGameStage() == GameStage.PLAYING))
            return;
        if(e.getBlock().getType() == Material.RED_WOOL || e.getBlock().getType() == Material.BLUE_WOOL) {
            e.getPlayer().getInventory().addItem(new ItemStack(e.getBlock().getType()));
            playerPlacedBlocks.add(e.getBlock());
        }


    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        if (!manager.isPlaying())
            return;
        if (!(manager.getGameStage() == GameStage.PLAYING))
            return;
        if (!manager.isPlayerInGame(e.getEntity()))
            return;
        Player p = e.getEntity();



        p.setGameMode(GameMode.SPECTATOR);
        if (manager.getTeam(p).hasBed()) {
            p.sendMessage(ChatColor.DARK_RED + "You died! respawning in 5 seconds!");

            p.teleport(manager.getMain().getSpawnById(manager.getId()).clone().add(0, 5, 0));

            new BukkitRunnable() {
                int count = 5;


                @Override
                public void run() {
                    p.sendTitle(ChatColor.GOLD + "You will respawn in: ", ChatColor.AQUA + "" + count + " seconds", 10, 20, 10);
                    count--;
                    if (count == 0) {
                        cancel();
                        manager.respawn(p);
                        p.sendMessage(ChatColor.AQUA + "You Respawned!");
                        p.setGameMode(GameMode.SURVIVAL);
                    }


                }
            }.runTaskTimer(Main.getPlugin(Main.class), 0, 20L);
        } else {
            manager.stop(e.getEntity());



        }

    }


}
