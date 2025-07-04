package standardtacticalknight.btwands.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.RenderGlobal;
import net.minecraft.client.render.camera.ICamera;
import net.minecraft.client.world.WorldClient;
import net.minecraft.core.util.phys.HitResult;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.phys.AABB;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import standardtacticalknight.btwands.BlockPos3D;
import standardtacticalknight.btwands.WandBlockFinder;
import standardtacticalknight.btwands.item.ItemTrowel;
import standardtacticalknight.btwands.item.ItemWand;

import java.util.List;

@Mixin(value = RenderGlobal.class, remap = false)
public class RenderGlobalMixin {
	@Final
	@Shadow
	private Minecraft mc;

	@Shadow
	private WorldClient worldObj;

	@Inject(method = "drawSelectionBox", at =  @At(value = "INVOKE", target = "Lnet/minecraft/client/render/RenderGlobal;drawOutlinedBoundingBox(Lnet/minecraft/core/util/phys/AABB;)V"))
	private void BTWandOverlayRender(ICamera camera, HitResult hitResult, float delta, CallbackInfo ci) {
		RenderGlobal self = (RenderGlobal)(Object)this;
		ItemStack heldItem = this.mc.thePlayer.inventory.getCurrentItem(); // Get held item
		if (heldItem == null) return;
		if (!(heldItem.getItem() instanceof ItemWand)) return;

		ItemWand item = (ItemWand) heldItem.getItem();
		int wandRange = item.getRange();
		ItemWand.Mode wandMode = item.getMode();

		// If it's a wand then find placeable spots to draw

		WandBlockFinder blockFinder = new WandBlockFinder(worldObj, mc.thePlayer, heldItem.getItem() instanceof ItemTrowel);
		List<BlockPos3D> blocks = blockFinder
			.getBlockPositionList(hitResult, wandRange, wandMode); // Find em based on held item's range TODO: fix this mess...

		if (!blocks.isEmpty()) {
			double offsetX = camera.getX(delta), offsetY = camera.getY(delta), offsetZ = camera.getZ(delta); // Do necessary camera offset stuffs
			GL11.glColor4f(1.0f, 1.0f, 1.0f, 0.6f); // Make white selection boxes

			for (BlockPos3D block : blocks) { // Draw 'em
				double OUTLINE_OFFSET = 0.0002f; // Helps prevent z fighting

				double x = block.x - offsetX + OUTLINE_OFFSET;
				double y = block.y - offsetY + OUTLINE_OFFSET;
				double z = block.z - offsetZ + OUTLINE_OFFSET;

				AABB bb = AABB.getTemporaryBB(
					x, y, z,
					x + 1, y + 1, z + 1
				);

				self.drawOutlinedBoundingBox(bb);
			}
		}


		GL11.glColor4f(0.0f, 0.0f, 0.0f, 0.35f + this.mc.getOutlineWidth() * 0.3f); // Reset color TODO: remove need for this
	}
}
