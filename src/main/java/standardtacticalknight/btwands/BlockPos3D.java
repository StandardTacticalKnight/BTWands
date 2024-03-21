package standardtacticalknight.btwands;

import net.minecraft.core.util.helper.Side;

public class BlockPos3D {
	public int x;
	public int y;
	public int z;

	public BlockPos3D(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public BlockPos3D move(Side direction) {
		switch(direction) {
			case TOP:
				y += 1;
				break;
			case BOTTOM:
				y -= 1;
				break;
			case NORTH:
				z -= 1;
				break;
			case SOUTH:
				z += 1;
				break;
			case EAST:
				x += 1;
				break;
			case WEST:
				x -= 1;
				break;
		}
		return this;
	}
	public BlockPos3D move(int dx, int dy, int dz) {
		x += dx;
		y += dy;
		x += dz;
		return this;
	}
}
