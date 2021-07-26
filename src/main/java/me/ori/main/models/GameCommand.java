package me.ori.main.models;

import org.bukkit.ChatColor;
import org.bukkit.GameRule;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GameCommand implements CommandExecutor {
    private GameManager manager;
    private GameManager manager2;

    public GameCommand(GameManager manager, GameManager manager2) {
        this.manager = manager;
        this.manager2 = manager2;

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        switch (args.length) {
            case 2:
                if (args[0].equalsIgnoreCase("1")) {


                    if (args[1].equalsIgnoreCase("select")) {
                        if (!player.hasPermission("rwd.select")) {
                            player.sendMessage(ChatColor.DARK_RED + "You don't have permission to do so!");
                            break;
                        }
                        player.openInventory(SelectInventory.createInventory());
                        break;
                    } else if (args[1].equalsIgnoreCase("start")) {


                        if (!player.hasPermission("rwd.start")) {
                            player.sendMessage(ChatColor.DARK_RED + "You don't have permission to do so!");
                            break;
                        }

                        if (manager.isPlaying()) {
                            player.sendMessage(ChatColor.DARK_RED + "Game is already playing!");
                            break;
                        }
                        if (manager.getBlueTeam().getPlayers().isEmpty() || manager.getRedTeam().getPlayers().isEmpty()) {
                            player.sendMessage(ChatColor.DARK_RED + "One or two teams are empty!");
                            break;
                        }

                        player.sendMessage(ChatColor.AQUA + "Started The Game!");
                        manager.start();
                        player.getWorld().setGameRule(GameRule.KEEP_INVENTORY, true);
                        player.getWorld().setGameRule(GameRule.DO_MOB_SPAWNING, false);

                        break;


                    }
                } else if (args[0].equalsIgnoreCase("2")) {


                    if (args[1].equalsIgnoreCase("select")) {
                        if (!player.hasPermission("rwd.select")) {
                            player.sendMessage(ChatColor.DARK_RED + "You don't have permission to do so!");
                            break;
                        }
                        player.openInventory(SelectInventory.createInventory());
                        break;


                    } else if (args[1].equalsIgnoreCase("start")) {

                        if (!player.hasPermission("rwd.start")) {
                            player.sendMessage(ChatColor.DARK_RED + "You don't have permission to do so!");
                            break;
                        }


                        if (manager2.isPlaying()) {
                            player.sendMessage(ChatColor.DARK_RED + "Game is already playing!");
                            break;
                        }

                        if (manager2.getBlueTeam().getPlayers().isEmpty() || manager2.getRedTeam().getPlayers().isEmpty()) {
                            player.sendMessage(ChatColor.DARK_RED + "One or two teams are empty!");
                            break;
                        }

                        player.sendMessage(ChatColor.AQUA + "Started The Game!");
                        manager2.start();
                        player.getWorld().setGameRule(GameRule.KEEP_INVENTORY, true);
                        player.getWorld().setGameRule(GameRule.DO_MOB_SPAWNING, false);

                        break;


                    }
                }

            default:
                sender.sendMessage(ChatColor.YELLOW + "Welcome to the rocketwars duels plugin! to start Type" +
                        " /rocketwarsduel(rwd) select after 2 players select their teams the game starts.");
                break;


        }

        return false;
    }
}
