package standardtacticalknight.btwands.item;

import net.minecraft.core.HitResult;
import net.minecraft.core.block.Block;
import net.minecraft.core.data.tag.Tag;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.enums.EnumBlockSoundEffectType;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.material.ToolMaterial;
import net.minecraft.core.item.tool.ItemTool;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.util.phys.AABB;
import net.minecraft.core.util.phys.Vec3d;
import net.minecraft.core.world.World;
import org.lwjgl.opengl.GL11;
import standardtacticalknight.btwands.BTWands;
import standardtacticalknight.btwands.BlockPos3D;
import standardtacticalknight.btwands.WandBlockFinder;

import java.util.LinkedList;

public class ItemWand extends ItemTool {

	private static final Tag<Block> tagEffectiveAgainst = null;

	public ItemWand(String name, int id, int damageDealt, ToolMaterial toolMaterial) {
		super(name, id, damageDealt, toolMaterial, tagEffectiveAgainst);
	}
	@Override
	public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int blockX, int blockY, int blockZ, Side side, double xPlaced, double yPlaced) {
		WandBlockFinder blockFinder = new WandBlockFinder(world);
		HitResult hitResult = new HitResult(blockX, blockY, blockZ, side, Vec3d.createVector(blockX, blockY, blockZ));
		//BTWands.finder = blockFinder;
		LinkedList<BlockPos3D> blocks = blockFinder.getBlockPositionList(hitResult,3);
		if (!blocks.isEmpty()) {
			for (BlockPos3D block : blocks) {
				world.editingBlocks = true;
				world.setBlockAndMetadataWithNotify(block.x, block.y, block.z, blockFinder.origin.id, blockFinder.meta);
				world.editingBlocks = false;
				world.notifyBlocksOfNeighborChange(block.x, block.y, block.z, blockFinder.origin.id);
			}
			world.playBlockSoundEffect((float)blockX + 0.5f, (float)blockY + 0.5f, (float)blockZ + 0.5f, blockFinder.origin, EnumBlockSoundEffectType.PLACE);
		}
		return true;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
		BTWands.LOGGER.info("rightclick");

		return itemstack;
	}
}
