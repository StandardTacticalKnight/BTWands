package standardtacticalknight.btwands.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.RenderGlobal;
import net.minecraft.client.render.camera.ICamera;
import net.minecraft.core.HitResult;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.phys.AABB;
import net.minecraft.core.world.World;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import standardtacticalknight.btwands.BTWands;
import standardtacticalknight.btwands.BlockPos3D;
import standardtacticalknight.btwands.WandBlockFinder;
import standardtacticalknight.btwands.item.ItemWand;

import java.util.LinkedList;

import static org.lwjgl.input.Keyboard.getEventKey;

@Mixin(value = RenderGlobal.class, remap = false)
public class BTWandsBlockEventMixin {
	@Shadow
	private Minecraft mc;
	@Shadow
	private World worldObj;
	@Inject(method = "drawSelectionBox", at =  @At(value = "INVOKE", target = "Lnet/minecraft/core/block/Block;setBlockBoundsBasedOnState(Lnet/minecraft/core/world/World;III)V"))
	private void BTWandOverlayRender(ICamera camera, HitResult hitResult, float partialTick, CallbackInfo ci) {

		ItemStack heldItem = this.mc.thePlayer.inventory.getCurrentItem(); //get held item
		if (heldItem != null && heldItem.getItem() instanceof ItemWand) { //if it's a wand then find placeable spots to draw
			WandBlockFinder blockFinder = new WandBlockFinder(worldObj, mc.thePlayer);
			LinkedList<BlockPos3D> blocks = blockFinder.getBlockPositionList(hitResult, ((ItemWand) heldItem.getItem()).getRange(),((ItemWand) heldItem.getItem()).getMode()); //find em based on held item's range TODO: fix this mess...
			if (!blocks.isEmpty()) {
				AABB aabb;
				RenderGlobal thisObject = (RenderGlobal) (Object) this; //grab the inject's instance of 'this' TODO: is this required or is there a more elegant way to do this?
				double offsetX = camera.getX(partialTick), offsetY = camera.getY(partialTick), offsetZ = camera.getZ(partialTick); //do necessary camera offset stuffs
				GL11.glColor4f(1.0f, 1.0f, 1.0f, 0.6f); //make white selection boxes
				for (BlockPos3D block : blocks) { //draw 'em
					aabb = new AABB(block.x, block.y, block.z, block.x + 1, block.y + 1, block.z + 1);
					thisObject.drawOutlinedBoundingBox(aabb.getOffsetBoundingBox(-offsetX, -offsetY, -offsetZ).expand(0.0002f, 0.0002f, 0.0002f)); //tiny .expand to stop z fighting
				}
			}
			GL11.glColor4f(0.0f, 0.0f, 0.0f, 0.35f + this.mc.getOutlineWidth() * 0.3f);//reset color TODO: remove need for this
		}
	}
}
