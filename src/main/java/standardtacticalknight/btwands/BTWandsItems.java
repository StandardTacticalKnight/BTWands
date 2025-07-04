package standardtacticalknight.btwands;

import net.minecraft.core.item.Item;
import net.minecraft.core.item.material.ToolMaterial;
import standardtacticalknight.btwands.item.ItemTrowel;
import standardtacticalknight.btwands.item.ItemWand;
import turniplabs.halplibe.helper.ItemBuilder;
import turniplabs.halplibe.util.ItemInitEntrypoint;

import java.util.HashMap;
import java.util.Map;

import static standardtacticalknight.btwands.BTWands.MOD_ID;

public class BTWandsItems implements ItemInitEntrypoint {
	public static Map<Item, String> textures = new HashMap<>();
	private int itemID = 18755;

	public static Item flintWand;
	public static Item quartzWand;
	public static Item olivineWand;
	public static Item diamondWand;
	public static Item flintTrowel;
	public static Item quartzTrowel;
	public static Item olivineTrowel;
	public static Item diamondTrowel;

	@Override
	public void afterItemInit() {

		flintWand = createWand("flintwand", ToolMaterial.wood, 1);
		quartzWand = createWand("quartzwand", ToolMaterial.iron, 3);
		olivineWand = createWand("olivinewand", ToolMaterial.stone, 4);
		diamondWand = createWand("diamondwand", ToolMaterial.diamond, 5);

		flintTrowel = createTrowel("flinttrowel", ToolMaterial.wood, 0);
		quartzTrowel = createTrowel("quartztrowel", ToolMaterial.iron, 1);
		olivineTrowel = createTrowel("olivinetrowel", ToolMaterial.stone, 3);
		diamondTrowel = createTrowel("diamondtrowel", ToolMaterial.diamond, 4);
	}

	private Item createWand(String name, ToolMaterial material, int range) {
		return createItem(new ItemWand(name, "btwands:item/" + name, itemID++, 2, material, range), name);
	}

	private Item createTrowel(String name, ToolMaterial material, int range) {
		return createItem(new ItemTrowel(name, "btwands:item/" + name, itemID++, 2, material, range), name);
	}

	private Item createItem(Item item, String texture) {
		textures.put(item, texture);

		return new ItemBuilder(MOD_ID)
			.build(item);
	}
}
