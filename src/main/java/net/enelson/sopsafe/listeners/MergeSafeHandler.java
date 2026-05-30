package net.enelson.sopsafe.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.data.Directional;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import net.enelson.sopsafe.SopSafe;

public class MergeSafeHandler implements Listener {
	@EventHandler
	public void blockPlace(BlockPlaceEvent e) {
		Block block = e.getBlock();
		if (block.getType() == Material.CHEST) {
			Chest chest = (Chest) block.getState();
			Directional faceless = (Directional) chest.getBlockData();
			BlockFace[] faces = null;

			if (faceless.getFacing().equals(BlockFace.EAST) || faceless.getFacing().equals(BlockFace.WEST)) {
				faces = new BlockFace[] { BlockFace.NORTH, BlockFace.SOUTH };
			} else if (faceless.getFacing().equals(BlockFace.NORTH) || faceless.getFacing().equals(BlockFace.SOUTH)) {
				faces = new BlockFace[] { BlockFace.EAST, BlockFace.WEST };
			}

			for (BlockFace face : faces) {
				Block block1 = block.getRelative(face);
				if (block1.getType().equals(Material.CHEST) && SopSafe.manager.getSafe(block1) != null) {
					e.setCancelled(true);
				}
			}
		}
	}
}


