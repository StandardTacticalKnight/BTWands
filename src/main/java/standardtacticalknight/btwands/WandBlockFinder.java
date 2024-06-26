package standardtacticalknight.btwands;

import net.minecraft.core.HitResult;
import net.minecraft.core.block.Block;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import standardtacticalknight.btwands.item.ItemWand;

import java.util.LinkedList;

public class WandBlockFinder {

	private final World world;
	private final EntityPlayer player;
	private Side side = Side.NONE; //what side of the block the player is looking at, decides the plane where blocks are searched
	public Block origin; //blocktype being looked at
	public int meta; //metadata of the block being looked at

	public WandBlockFinder(World world, EntityPlayer player) {
		this.world = world;
		this.player = player;
	}

	/**
	 * generate the list of similar blocks in the plane being looked at by the player
	 * @param hitresult what the player is looking at
	 * @param range square radius of block searching
	 * @return LinkedList containing blocks to be placed/replaced
	 */
	public LinkedList<BlockPos3D> getBlockPositionList(HitResult hitresult, int range, ItemWand.Mode mode){
		//add the block looked at (moved 1 in face direction) to the place list
		this.side = hitresult.side;
		this.origin = Block.blocksList[world.getBlockId(hitresult.x, hitresult.y, hitresult.z)];
		this.meta = world.getBlockMetadata(hitresult.x, hitresult.y, hitresult.z);
		LinkedList<BlockPos3D> blocksToPlace = new LinkedList<>();
		//direction masks for wand placement
		int xMask, yMask, zMask;
		switch(this.side) { //eww... this is gross... this is way too many switch statements... find a better way... some kinda truth table lookup maybe?
			case TOP:
			case BOTTOM:
				xMask = 1;
				yMask = 0;
				zMask = 1;
				Direction playerFacing; //change TOP/BOTTOM placement during V/H place modes based on player facing
				switch(mode) {
					case VERTICAL:
						playerFacing = this.player.getHorizontalPlacementDirection(this.side); // get facing direction for top/bottom placement
						switch (playerFacing){
							case EAST:
							case WEST:
								zMask = 0;
								break;
							case NORTH:
							case SOUTH:
								xMask = 0;
								break;
						}
						break;
					case HORIZONTAL:
						playerFacing = this.player.getHorizontalPlacementDirection(this.side); // get facing direction for top/bottom placement
						switch (playerFacing){
							case EAST:
							case WEST:
								xMask = 0;
								break;
							case NORTH:
							case SOUTH:
								zMask = 0;
								break;
						}
						break;
				}
				break;
			case NORTH:
			case SOUTH:
				xMask = 1;
				yMask = 1;
				zMask = 0;
				switch(mode) {
					case VERTICAL:
						xMask = 0;
						break;
					case HORIZONTAL:
						yMask = 0;
						break;
				}
				break;
			case EAST:
			case WEST:
				xMask = 0;
				yMask = 1;
				zMask = 1;
				switch(mode) {
					case VERTICAL:
						zMask = 0;
						break;
					case HORIZONTAL:
						yMask = 0;
						break;
				}
				break;
			default:
				BTWands.LOGGER.error("invalid hitresult.side");
				return null;
        }// thank god it's over

		boolean flag; //crude neighbor detector; if a new block hasn't been found in a layer then stop looking further
		for(int layer = 0; layer <= range; layer++){ //iterate over layers from inside to outside 3210123
			flag = true;
			for(int dx = -layer*xMask; dx <= layer*xMask; ++dx) { //iterate over the cube ignoring axes needed
				for (int dy = -layer*yMask; dy <= layer*yMask; ++dy) {
					for (int dz = -layer*zMask; dz <= layer*zMask; ++dz) {
						if (Math.abs(dx) == layer || Math.abs(dy) == layer || Math.abs(dz) == layer) { // Check if the Manhattan distance from (x,y,z) to (0,0,0) equals to the current layer
							BlockPos3D queryPos = new BlockPos3D(hitresult.x + dx, hitresult.y + dy, hitresult.z + dz);
							if (CheckValid(queryPos)) {
								blocksToPlace.add(queryPos);
								flag = false;
							}
						}
					}
				}
			}if(flag){return blocksToPlace;} //crude neighbor detection
		}
        return blocksToPlace;
    }

	/**
	 * check if a block should be added to the candidate list,
	 * only true if the block matches origin and has a replaceable block(air, tallgrass, water, etc.) adjacent to it
	 * @param candidate block to be checked
	 * @return if block is valid or no
	 */
	private Boolean CheckValid(BlockPos3D candidate){
		if (candidate.y >= 0 && candidate.y < world.getHeightBlocks()) {
			Block base = Block.blocksList[world.getBlockId(candidate.x, candidate.y, candidate.z)];
			if (base != null && base.blockMaterial.isSolid() && base.id == this.origin.id) { //is foundation there and also same block as origin
				BlockPos3D placePos = candidate.move(side);
				if (candidate.y >= 0 && candidate.y < world.getHeightBlocks()) {
					Block place = Block.blocksList[world.getBlockId(placePos.x, placePos.y, placePos.z)];
					if (place == null || place.blockMaterial.isReplaceable()) { //is place area air or replaceable block
						return true;
					}
				}
			}
		}
		return false;
    }
}
