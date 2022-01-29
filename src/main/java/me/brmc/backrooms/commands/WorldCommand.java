package me.brmc.backrooms.commands;

import org.bukkit.World;
import org.bukkit.entity.Player;

import me.brmc.backrooms.Backrooms;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Default;
import revxrsal.commands.annotation.Subcommand;

@Command("world")
public class WorldCommand{

	@Subcommand({ "teleport", "tp" })
	public void worldTeleport(Player sender, String worldName, @Default("me") Player player) {
		World world = Backrooms.get().getServer().getWorld(worldName);
		if (world == null)
			return;
		player.teleport(world.getSpawnLocation());
	}

}
