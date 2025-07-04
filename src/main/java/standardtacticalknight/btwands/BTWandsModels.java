package standardtacticalknight.btwands;

import net.minecraft.client.render.EntityRenderDispatcher;
import net.minecraft.client.render.TileEntityRenderDispatcher;
import net.minecraft.client.render.block.color.BlockColorDispatcher;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.item.model.ItemModelDispatcher;
import net.minecraft.client.render.item.model.ItemModelStandard;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.util.collection.NamespaceID;
import turniplabs.halplibe.helper.ModelHelper;
import turniplabs.halplibe.util.ModelEntrypoint;

import static standardtacticalknight.btwands.BTWands.MOD_ID;
import static standardtacticalknight.btwands.BTWandsItems.textures;

public class BTWandsModels implements ModelEntrypoint {
	@Override
	public void initBlockModels(BlockModelDispatcher dispatcher) {

	}

	@Override
	public void initItemModels(ItemModelDispatcher dispatcher) {
		BTWands.LOGGER.info("Initializing items models...");

		textures.forEach((item, texture) -> ModelHelper.setItemModel(item, () -> {
			ItemModelStandard model = new ItemModelStandard(item, MOD_ID).setFull3D();
			model.icon = TextureRegistry.getTexture(NamespaceID.getTemp(MOD_ID,"item/"+texture));
			//model.setPointInfrontOfPlayer();
			//model.setRotateWhenRendering();
			return model;
		}));
	}

	@Override
	public void initEntityModels(EntityRenderDispatcher dispatcher) {

	}

	@Override
	public void initTileEntityModels(TileEntityRenderDispatcher dispatcher) {

	}

	@Override
	public void initBlockColors(BlockColorDispatcher dispatcher) {

	}
}
