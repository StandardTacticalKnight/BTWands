package standardtacticalknight.btwands.item;

import net.minecraft.client.entity.player.PlayerLocal;
import net.minecraft.client.entity.player.PlayerRemote;
import net.minecraft.client.world.WorldClientMP;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.data.tag.Tag;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.enums.EnumBlockSoundEffectType;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.material.ToolMaterial;
import net.minecraft.core.item.tool.ItemTool;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.util.phys.HitResult;
import net.minecraft.core.util.phys.Vec3;
import net.minecraft.core.world.World;
import net.minecraft.server.entity.player.PlayerServer;
import net.minecraft.server.world.WorldServer;
import standardtacticalknight.btwands.BTWands;
import standardtacticalknight.btwands.BlockPos3D;
import standardtacticalknight.btwands.WandBlockFinder;

import java.util.LinkedList;

public class ItemWand extends ItemTool {

	private static final Tag<Block<?>> tagEffectiveAgainst = BlockTags.MINEABLE_BY_SHOVEL;
	private final int range;
	public enum Mode {
		OPEN,
		HORIZONTAL,
		VERTICAL
	}
	Mode mode = Mode.OPEN;

	public ItemWand(String name,String namespace, int id, int damageDealt, ToolMaterial toolMaterial, int range) {
		super(name, namespace, id, damageDealt, toolMaterial, tagEffectiveAgainst);
		this.range = range;
		this.setMaxDamage(toolMaterial.getDurability()*4);
	}
	@Override
	public boolean onUseItemOnBlock(ItemStack itemstack, Player entityplayer, World world, int blockX, int blockY, int blockZ, Side side, double xPlaced, double yPlaced) {
	    if (world.isClientSide) return true;
		WandBlockFinder blockFinder = new WandBlockFinder(world, entityplayer);
		HitResult hitResult = new HitResult(blockX, blockY, blockZ, side, Vec3.getTempVec3(blockX, blockY, blockZ));
		LinkedList<BlockPos3D> blocks = blockFinder.getBlockPositionList(hitResult,this.range, this.mode);//generate the block list for placement

		ItemStack[] result = blockFinder.origin.getBreakResult(world, EnumDropCause.PICK_BLOCK, blockX, blockY, blockZ, blockFinder.originMeta, blockFinder.originTileEntity);
		if (!blocks.isEmpty() && getInventorySlot(entityplayer, result[0])!=-1) { //if there is at least one placeable block and at least one of that item in the player's inventory
			for (BlockPos3D block : blocks) {
				if(consumeItem(entityplayer, result[0])){ //try to take one item from the player's inv and place it in the world
					//world.editingBlocks = true;
					boolean placed = world.setBlockAndMetadataWithNotify(block.x, block.y, block.z, blockFinder.origin.id(), blockFinder.originMeta);
					//world.editingBlocks = false;
					world.notifyBlocksOfNeighborChange(block.x, block.y, block.z, blockFinder.origin.id());
					if(!placed){
						BTWands.LOGGER.warn("refunded item, this should not happen in normal conditions");
						refundItem(entityplayer,new ItemStack(blockFinder.origin,1,blockFinder.originMeta)); //if placing the block fails, refund the item
					}else itemstack.damageItem(1, entityplayer); //use up the durability for every block placed
				}else{
					break; //if we cant find an item to consume then stop
				}
			}
			if(world instanceof WorldClientMP && entityplayer instanceof PlayerRemote){//update clientside if on a server
				entityplayer.inventorySlots.broadcastChanges();//FIXME could be optimized
				//((WorldServer)world).triggerEvent(blockX, blockY, blockZ, 0, 0);
				//((WorldServer)world).mcServer.playerList.sendPacketToPlayersAroundPoint(blockX, blockY, blockZ, 64.0, world.dimension.id, new Packet54PlayNoteBlock(blockX, blockY, blockZ, 1, 1));
			}
			world.playBlockSoundEffect(entityplayer,(double)blockX + 0.5f, (double)blockY + 0.5f, (double)blockZ + 0.5f, blockFinder.origin, EnumBlockSoundEffectType.PLACE);
		}
		return true;
	}

	@Override
	public ItemStack onUseItem(ItemStack itemstack, World world, Player entityplayer) {
		if (entityplayer.isSneaking()) {
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
			entityplayer.sendMessage("Wand mode: "+this.mode);
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
	 *
	 * @return success or failure to find or consume item
	 */
	boolean consumeItem(Player player, ItemStack itemStack){
		int selectedSlot = getInventorySlot(player, itemStack);
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
	 *
	 * @return inventory slot where item is contained, or -1 otherwise
	 */
	int getInventorySlot(Player player, ItemStack itemStack){
		for (int j = 0; j < player.inventory.mainInventory.length; ++j) {
			if (player.inventory.mainInventory[j] == null || player.inventory.mainInventory[j].itemID != itemStack.itemID && player.inventory.mainInventory[j].getMetadata() != itemStack.getMetadata()) continue;
			//if (player.inventory.mainInventory[j].getMetadata() != itemMeta) continue;
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
	boolean refundItem(Player player, ItemStack item){
		if (player instanceof PlayerLocal) {
			player.inventory.insertItem(item, true);
			if (item.stackSize > 0) {
				player.dropPlayerItem(item);
			}
			return true;
		}
		if (player instanceof PlayerRemote) {
			PlayerRemote playerMP = (PlayerRemote)player;
			playerMP.dropPlayerItem(item);
			return true;
		}
		return false;
	}
}
