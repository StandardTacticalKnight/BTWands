package standardtacticalknight.btwands.item;

import net.minecraft.client.entity.player.EntityPlayerSP;
import net.minecraft.core.HitResult;
import net.minecraft.core.block.Block;
import net.minecraft.core.data.tag.Tag;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.enums.EnumBlockSoundEffectType;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.material.ToolMaterial;
import net.minecraft.core.item.tool.ItemTool;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.util.phys.Vec3d;
import net.minecraft.core.world.World;
import net.minecraft.server.entity.player.EntityPlayerMP;
import standardtacticalknight.btwands.BTWands;
import standardtacticalknight.btwands.BlockPos3D;
import standardtacticalknight.btwands.WandBlockFinder;

import java.util.LinkedList;

public class ItemWand extends ItemTool {

	private static final Tag<Block> tagEffectiveAgainst = null;
	private final int range;
	public enum Mode {
		OPEN,
		HORIZONTAL,
		VERTICAL
	}
	private Mode mode = Mode.OPEN;

	public ItemWand(String name, int id, int damageDealt, ToolMaterial toolMaterial, int range) {
		super(name, id, damageDealt, toolMaterial, tagEffectiveAgainst);
		this.range = range;
		this.setMaxDamage(toolMaterial.getDurability()*4);
	}
	@Override
	public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int blockX, int blockY, int blockZ, Side side, double xPlaced, double yPlaced) {
		WandBlockFinder blockFinder = new WandBlockFinder(world, entityplayer);
		HitResult hitResult = new HitResult(blockX, blockY, blockZ, side, Vec3d.createVector(blockX, blockY, blockZ));
		LinkedList<BlockPos3D> blocks = blockFinder.getBlockPositionList(hitResult,this.range, this.mode);//generate the block list for placement
		if (!blocks.isEmpty() && getInventorySlot(entityplayer,blockFinder.origin.id,blockFinder.meta)!=-1) { //if there is at least one placeable block and at least one of that item in the player's inventory
			for (BlockPos3D block : blocks) {
				if(consumeItem(entityplayer,blockFinder.origin.id,blockFinder.meta)){ //try to take one item from the player's inv and place it in the world
					world.editingBlocks = true;
					boolean placed = world.setBlockAndMetadataWithNotify(block.x, block.y, block.z, blockFinder.origin.id, blockFinder.meta);
					world.editingBlocks = false;
					world.notifyBlocksOfNeighborChange(block.x, block.y, block.z, blockFinder.origin.id);
					if(!placed){
						BTWands.LOGGER.warn("refunded item, this should not happen in normal conditions");
						refundItem(entityplayer,new ItemStack(blockFinder.origin,1,blockFinder.meta)); //if placing the block fails, refund the item
					}else itemstack.damageItem(1, entityplayer); //use up the durability for every block placed
				}else{
					break; //if we cant find an item to consume then stop
				}
			}
			world.playBlockSoundEffect(entityplayer,(double)blockX + 0.5f, (double)blockY + 0.5f, (double)blockZ + 0.5f, blockFinder.origin, EnumBlockSoundEffectType.PLACE);
		}
		return true;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
		if(entityplayer.isSneaking()){
			switch(this.mode) {
				case OPEN:
					this.mode = Mode.VERTICAL;
					break;
				case VERTICAL:
					this.mode = Mode.HORIZONTAL;
					break;
				case HORIZONTAL:
					this.mode = Mode.OPEN;
					break;
			}

			entityplayer.addChatMessage("Wand mode: "+this.mode);
		}
		return itemstack;
	}

	public int getRange(){
		return this.range;
	}
	public Mode getMode(){
		return this.mode;
	}

	/**
	 * Consumes a single item from player inventory
	 * @param player
	 * @param itemID
	 * @param itemMeta
	 * @return success or failure to find or consume item
	 */
	private boolean consumeItem(EntityPlayer player, int itemID, int itemMeta){
		int selectedSlot = getInventorySlot(player, itemID, itemMeta);
		if (selectedSlot < 0) {
			return false;
		}
		if (player.getGamemode().consumeBlocks() && --player.inventory.mainInventory[selectedSlot].stackSize <= 0) {
			player.inventory.mainInventory[selectedSlot] = null;
		}
		return true;
    }

	/**
	 * Searches player inventory for specified item + meta
	 * @param player
	 * @param itemID
	 * @param itemMeta item metadata
	 * @return inventory slot where item is contained, or -1 otherwise
	 */
	private int getInventorySlot(EntityPlayer player,  int itemID, int itemMeta){
		for (int j = 0; j < player.inventory.mainInventory.length; ++j) {
			if (player.inventory.mainInventory[j] == null || player.inventory.mainInventory[j].itemID != itemID || player.inventory.mainInventory[j].getMetadata() != itemMeta) continue;
			return j;
		}
		return -1;
	}

	/**
	 * Gives the player the item when required, either by direct input to inventory or dropping if full
	 * @param player player to be refunded
	 * @param item itemstack to be refunded to the player
	 * @return success or failure
	 */
	private boolean refundItem(EntityPlayer player, ItemStack item){
		if (player instanceof EntityPlayerSP) {
			player.inventory.insertItem(item, true);
			if (item.stackSize > 0) {
				player.dropPlayerItem(item);
			}
			return true;
		}
		if (player instanceof EntityPlayerMP) {
			EntityPlayerMP playerMP = (EntityPlayerMP)player;
			playerMP.dropPlayerItem(item);
			return true;
		}
		return false;
	}
}
