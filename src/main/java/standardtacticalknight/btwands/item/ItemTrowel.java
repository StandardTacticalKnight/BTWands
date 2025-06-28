package standardtacticalknight.btwands.item;

import net.minecraft.client.entity.player.PlayerRemote;
import net.minecraft.client.world.WorldClientMP;
import net.minecraft.core.block.Block;
import net.minecraft.core.util.phys.HitResult;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.enums.EnumBlockSoundEffectType;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.block.ItemBlock;
import net.minecraft.core.item.material.ToolMaterial;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.util.phys.Vec3;
import net.minecraft.core.world.World;
import standardtacticalknight.btwands.BTWands;
import standardtacticalknight.btwands.BlockPos3D;
import standardtacticalknight.btwands.WandBlockFinder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ItemTrowel extends ItemWand {
	public ItemTrowel(String name, String namespace, int id, int damageDealt, ToolMaterial toolMaterial, int range) {
		super(name, namespace, id, damageDealt, toolMaterial, range);
	}
	private final List<ItemStack> hotbarBlocks = new ArrayList<>();
	private final Random rand = new Random();

	@Override
	public boolean onUseItemOnBlock(ItemStack itemstack, Player entityplayer, World world, int blockX, int blockY, int blockZ, Side side, double xPlaced, double yPlaced) {
		if (world.isClientSide) return true;
		WandBlockFinder blockFinder = new WandBlockFinder(world, entityplayer, true);

		HitResult hitResult = new HitResult(blockX, blockY, blockZ, side, Vec3.getTempVec3(blockX, blockY, blockZ));
		List<BlockPos3D> blocks = blockFinder.getBlockPositionList(hitResult, this.getRange(), this.getMode());//generate the block list for placement
		if (blocks.isEmpty() || !getHotbarBlocks(entityplayer)) return true;

		// If there is at least one placeable block and at least one of that item in the player's inventory

		for (BlockPos3D block : blocks) {
			ItemStack chosenItem = hotbarBlocks.get(rand.nextInt(hotbarBlocks.size()));// grab a random block from the player's hotbar
			int chosenId = chosenItem.itemID;
			int chosenMeta = chosenItem.getMetadata();

			if (!consumeItem(entityplayer, chosenItem)) break; // If we cant find an item to consume then stop

			// Try to take one item from the player's inv and place it in the world
			boolean placed = world.setBlockAndMetadataWithNotify(block.x, block.y, block.z, chosenId, chosenMeta);
			world.notifyBlocksOfNeighborChange(block.x, block.y, block.z, chosenId);

			if (!placed) {
				BTWands.LOGGER.warn("refunded item, this should not happen in normal conditions");
				refundItem(entityplayer,new ItemStack(chosenItem.getItem(),1,chosenMeta)); //if placing the block fails, refund the item
				continue;
			}

			itemstack.damageItem(1, entityplayer); // Use up the durability for every block placed
		}
		if(entityplayer instanceof PlayerRemote && world instanceof WorldClientMP){ // Update clientside if on a server
			entityplayer.inventorySlots.broadcastChanges(); // Could be optimized
		}

		world.playBlockSoundEffect(entityplayer,(double)blockX + 0.5f, (double)blockY + 0.5f, (double)blockZ + 0.5f, blockFinder.origin, EnumBlockSoundEffectType.PLACE);
		return true;
	}

	private boolean getHotbarBlocks(Player player){
		hotbarBlocks.clear();
		for (int slot = 0; slot < 9; ++slot) { // Go through all items in hotbar to find suitable blocks
			ItemStack stack = player.inventory.getItem(slot + player.inventory.getHotbarOffset());
			if (stack == null) continue;
			if (!(stack.getItem() instanceof ItemBlock)) continue;
			ItemBlock<?> item = ((ItemBlock<?>) stack.getItem());
			Block<?> block = item.getBlock();

			if (!block.isSolidRender()) continue;


			hotbarBlocks.add(stack);
		}

        return !hotbarBlocks.isEmpty();
	}
}
