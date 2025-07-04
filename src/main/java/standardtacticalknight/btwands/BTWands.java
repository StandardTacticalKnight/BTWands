package standardtacticalknight.btwands;

import net.fabricmc.api.ModInitializer;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.item.Items;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.halplibe.helper.RecipeBuilder;
import turniplabs.halplibe.util.GameStartEntrypoint;
import turniplabs.halplibe.util.RecipeEntrypoint;

import static standardtacticalknight.btwands.BTWandsItems.*;


public class BTWands implements ModInitializer, GameStartEntrypoint, RecipeEntrypoint {
    public static final String MOD_ID = "btwands";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("BTWands initialized.");
    }

	@Override
	public void beforeGameStart() {
	}

	@Override
	public void afterGameStart() {
	}

	@Override
	public void onRecipesReady() {
		RecipeBuilder.Shaped(MOD_ID)
			.setShape("  B", " S ", "S  ")
			.addInput('B', Items.FLINT)
			.addInput('S', Items.STICK)
			.create("toFlintWand", flintWand.getDefaultStack());
		RecipeBuilder.Shaped(MOD_ID)
			.setShape("  B", " S ", "S  ")
			.addInput('B', Blocks.BLOCK_QUARTZ)
			.addInput('S', Items.STICK)
			.create("toQuartzWand", quartzWand.getDefaultStack());
		RecipeBuilder.Shaped(MOD_ID)
			.setShape("  B", " S ", "S  ")
			.addInput('B', Blocks.BLOCK_OLIVINE)
			.addInput('S', Items.STICK)
			.create("toOlivineWand", olivineWand.getDefaultStack());
		RecipeBuilder.Shaped(MOD_ID)
			.setShape("  B", " S ", "S  ")
			.addInput('B', Blocks.BLOCK_DIAMOND)
			.addInput('S', Items.STICK)
			.create("toDiamondWand", diamondWand.getDefaultStack());
		RecipeBuilder.Shaped(MOD_ID)
			.setShape("S  ", " S ", "  B")
			.addInput('B', Items.FLINT)
			.addInput('S', Items.STICK)
			.create("toFlintTrowel", flintTrowel.getDefaultStack());
		RecipeBuilder.Shaped(MOD_ID)
			.setShape("S  ", " S ", "  B")
			.addInput('B', Blocks.BLOCK_QUARTZ)
			.addInput('S', Items.STICK)
			.create("toQuartzTrowel", quartzTrowel.getDefaultStack());
		RecipeBuilder.Shaped(MOD_ID)
			.setShape("S  ", " S ", "  B")
			.addInput('B', Blocks.BLOCK_OLIVINE)
			.addInput('S', Items.STICK)
			.create("toOlivineTrowel", olivineTrowel.getDefaultStack());
		RecipeBuilder.Shaped(MOD_ID)
			.setShape("S  ", " S ", "  B")
			.addInput('B', Blocks.BLOCK_DIAMOND)
			.addInput('S', Items.STICK)
			.create("toDiamondTrowel", diamondTrowel.getDefaultStack());
	}

	@Override
	public void initNamespaces() {
		RecipeBuilder.initNameSpace(MOD_ID);
	}
}
