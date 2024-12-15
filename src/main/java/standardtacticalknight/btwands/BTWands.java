package standardtacticalknight.btwands;

import net.fabricmc.api.ModInitializer;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.Items;
import net.minecraft.core.item.material.ToolMaterial;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import standardtacticalknight.btwands.item.ItemTrowel;
import standardtacticalknight.btwands.item.ItemWand;
import turniplabs.halplibe.helper.ItemBuilder;
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
	public static Item flintTrowel;
	public static Item quartzTrowel;
	public static Item olivineTrowel;
	public static Item diamondTrowel;
    @Override
    public void onInitialize() {
        LOGGER.info("BTWands initialized.");
    }

	@Override
	public void beforeGameStart() {
		int startingBlockId = 2700;
		int itemID = 18755;
		flintWand = new ItemBuilder(MOD_ID)
		    .setIcon("btwands:item/flintwand")
		    .build(new ItemWand("flintwand", MOD_ID+":flintwand", itemID++, 2, ToolMaterial.wood, 1));
		quartzWand = new ItemBuilder(MOD_ID)
            .setIcon("btwands:item/quartzwand")
            .build(new ItemWand("quartzwand", MOD_ID+":quartzwand",itemID++, 2, ToolMaterial.iron, 3));
		olivineWand = new ItemBuilder(MOD_ID)
            .setIcon("btwands:item/olivinewand")
            .build(new ItemWand("olivinewand",MOD_ID+":olivinewand", itemID++, 2, ToolMaterial.stone, 4));
		diamondWand = new ItemBuilder(MOD_ID)
            .setIcon("btwands:item/diamondwand")
            .build(new ItemWand("diamondwand",MOD_ID+":diamondwand", itemID++, 2, ToolMaterial.diamond, 5));
		flintTrowel = new ItemBuilder(MOD_ID)
			.setIcon("btwands:item/flinttrowel")
			.build(new ItemTrowel("flinttrowel",MOD_ID+":flinttrowel", itemID++, 2, ToolMaterial.wood, 0));
		quartzTrowel = new ItemBuilder(MOD_ID)
			.setIcon("btwands:item/quartztrowel")
			.build(new ItemTrowel("quartztrowel",MOD_ID+":quartztrowel", itemID++, 2, ToolMaterial.iron, 1));
		olivineTrowel = new ItemBuilder(MOD_ID)
			.setIcon("btwands:item/olivinetrowel")
			.build(new ItemTrowel("olivinetrowel",MOD_ID+":olivinetrowel", itemID++, 2, ToolMaterial.stone, 3));
		diamondTrowel = new ItemBuilder(MOD_ID)
			.setIcon("btwands:item/diamondtrowel")
			.build(new ItemTrowel("diamondtrowel",MOD_ID+":diamondtrowel", itemID++, 2, ToolMaterial.diamond, 4));
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
