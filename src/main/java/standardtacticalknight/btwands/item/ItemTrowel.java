package standardtacticalknight.btwands.item;

import net.minecraft.client.entity.player.PlayerRemote;
import net.minecraft.client.world.WorldClientMP;
import net.minecraft.core.util.phys.HitResult;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.enums.EnumBlockSoundEffectType;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.block.ItemBlock;
import net.minecraft.core.item.material.ToolMaterial;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.util.phys.Vec3;
import net.minecraft.core.world.World;
import net.minecraft.server.entity.player.PlayerServer;
import net.minecraft.server.world.WorldServer;
import standardtacticalknight.btwands.BTWands;
import standardtacticalknight.btwands.BlockPos3D;
import standardtacticalknight.btwands.WandBlockFinder;

import java.util.LinkedList;
import java.util.Random;

public class ItemTrowel extends ItemWand {
	public ItemTrowel(String name, String namespace, int id, int damageDealt, ToolMaterial toolMaterial, int range) {
		super(name, namespace, id, damageDealt, toolMaterial, range);
	}
	private final LinkedList<ItemStack> hotbarBlocks = new LinkedList<>();
	private final Random rand = new Random();

	@Override
	public boolean onUseItemOnBlock(ItemStack itemstack, Player entityplayer, World world, int blockX, int blockY, int blockZ, Side side, double xPlaced, double yPlaced) {
		if (world.isClientSide) return true;
		WandBlockFinder blockFinder = new WandBlockFinder(world, entityplayer,true);
		HitResult hitResult = new HitResult(blockX, blockY, blockZ, side, Vec3.getTempVec3(blockX, blockY, blockZ));
		LinkedList<BlockPos3D> blocks = blockFinder.getBlockPositionList(hitResult, this.getRange(), this.getMode());//generate the block list for placement
		if (!blocks.isEmpty() && getHotbarBlocks(entityplayer)) { //if there is at least one placeable block and at least one of that item in the player's inventory
			for (BlockPos3D block : blocks) {
				ItemStack chosenItem = hotbarBlocks.get(rand.nextInt(hotbarBlocks.size()));// grab a random block from the player's hotbar
				int chosenId = chosenItem.itemID;
				int chosenMeta = chosenItem.getMetadata();


				if(consumeItem(entityplayer, chosenItem)){ //try to take one item from the player's inv and place it in the world
					//world.editingBlocks = true;
					boolean placed = world.setBlockAndMetadataWithNotify(block.x, block.y, block.z, chosenId, chosenMeta);
					//world.editingBlocks = false;
					world.notifyBlocksOfNeighborChange(block.x, block.y, block.z, chosenId);
					if(!placed){
						BTWands.LOGGER.warn("refunded item, this should not happen in normal conditions");
						refundItem(entityplayer,new ItemStack(chosenItem.getItem(),1,chosenMeta)); //if placing the block fails, refund the item
					}else itemstack.damageItem(1, entityplayer); //use up the durability for every block placed
				}else{
					break; //if we cant find an item to consume then stop
				}
			}
			if(entityplayer instanceof PlayerRemote && world instanceof WorldClientMP){//update clientside if on a server
				(entityplayer).inventorySlots.broadcastChanges();//could be optimized
			}
			world.playBlockSoundEffect(entityplayer,(double)blockX + 0.5f, (double)blockY + 0.5f, (double)blockZ + 0.5f, blockFinder.origin, EnumBlockSoundEffectType.PLACE);
		}
		return true;
	}
	private boolean getHotbarBlocks(Player player){
		int hotbarSlot;
		hotbarBlocks.clear();
		for(hotbarSlot = 0; hotbarSlot < 9; ++hotbarSlot) {// go through all items in hotbar to find suitable blocks
			ItemStack stack = player.inventory.getItem(hotbarSlot + player.inventory.getHotbarOffset());
			if (stack != null && stack.getItem() instanceof ItemBlock && ((ItemBlock) stack.getItem()).getBlock().isSolidRender()) {
				hotbarBlocks.add(stack);
			}
		}
        return !hotbarBlocks.isEmpty();
	}
}
