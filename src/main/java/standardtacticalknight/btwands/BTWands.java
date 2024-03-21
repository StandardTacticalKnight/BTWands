package standardtacticalknight.btwands;

import net.fabricmc.api.ModInitializer;
import net.minecraft.core.block.Block;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.material.ToolMaterial;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import standardtacticalknight.btwands.item.ItemWand;
import turniplabs.halplibe.HalpLibe;
import turniplabs.halplibe.helper.ItemHelper;
import turniplabs.halplibe.util.GameStartEntrypoint;
import turniplabs.halplibe.util.RecipeEntrypoint;


public class BTWands implements ModInitializer, GameStartEntrypoint, RecipeEntrypoint {
    public static final String MOD_ID = "btwands";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	//public static WandBlockFinder finder;
	public static Item wand;
    @Override
    public void onInitialize() {
        LOGGER.info("BTWands initialized.");
    }

	@Override
	public void beforeGameStart() {
		int startingBlockId = 2700;
		int itemID = 18755;
		wand = ItemHelper.createItem(MOD_ID,new ItemWand("wand", itemID++,2, ToolMaterial.iron),"wand","wand.png");
	}

	@Override
	public void afterGameStart() {

	}

	@Override
	public void onRecipesReady() {

	}
}
