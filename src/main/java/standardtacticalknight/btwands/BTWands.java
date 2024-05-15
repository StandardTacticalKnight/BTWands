package standardtacticalknight.btwands;

import net.fabricmc.api.ModInitializer;
import net.minecraft.core.block.Block;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.material.ToolMaterial;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import standardtacticalknight.btwands.item.ItemWand;
import turniplabs.halplibe.helper.ItemHelper;
import turniplabs.halplibe.helper.RecipeBuilder;
import turniplabs.halplibe.util.GameStartEntrypoint;
import turniplabs.halplibe.util.RecipeEntrypoint;


public class BTWands implements ModInitializer, GameStartEntrypoint, RecipeEntrypoint {
    public static final String MOD_ID = "btwands";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	//public static WandBlockFinder finder;
	public static Item flintWand;
	public static Item quartzWand;
	public static Item olivineWand;
	public static Item diamondWand;
    @Override
    public void onInitialize() {
        LOGGER.info("BTWands initialized.");
    }

	@Override
	public void beforeGameStart() {
		int startingBlockId = 2700;
		int itemID = 18755;
		flintWand = ItemHelper.createItem(MOD_ID,  new ItemWand("flintwand", itemID++,2, ToolMaterial.wood, 1),"flintwand.png");
		quartzWand = ItemHelper.createItem(MOD_ID, new ItemWand("quartzwand", itemID++,2, ToolMaterial.iron, 3),"quartzwand.png");
		olivineWand = ItemHelper.createItem(MOD_ID,new ItemWand("olivinewand", itemID++,2, ToolMaterial.stone, 4),"olivinewand.png");
		diamondWand = ItemHelper.createItem(MOD_ID,new ItemWand("diamondwand", itemID++,2, ToolMaterial.diamond, 5),"diamondwand.png");
	}

	@Override
	public void afterGameStart() {

	}

	@Override
	public void onRecipesReady() {
		RecipeBuilder.Shaped(MOD_ID)
			.setShape("  B", " S ", "S  ")
			.addInput('B', Item.flint)
			.addInput('S', Item.stick)
			.create("toFlintWand", flintWand.getDefaultStack());
		RecipeBuilder.Shaped(MOD_ID)
			.setShape("  B", " S ", "S  ")
			.addInput('B', Block.blockQuartz)
			.addInput('S', Item.stick)
			.create("toQuartzWand", quartzWand.getDefaultStack());
		RecipeBuilder.Shaped(MOD_ID)
			.setShape("  B", " S ", "S  ")
			.addInput('B', Block.blockOlivine)
			.addInput('S', Item.stick)
			.create("toOlivineWand", olivineWand.getDefaultStack());
		RecipeBuilder.Shaped(MOD_ID)
			.setShape("  B", " S ", "S  ")
			.addInput('B', Block.blockDiamond)
			.addInput('S', Item.stick)
			.create("toDiamondWand", diamondWand.getDefaultStack());
	}

	@Override
	public void initNamespaces() {

	}
}
