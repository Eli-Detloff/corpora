package corpora.modid.init;

import corpora.modid.Corpora;
import corpora.modid.item.ShellItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {

    public static final Item SHELL_ITEM = registerItem("shell_item", new ShellItem(new Item.Settings()));
    public static final Item SHELL_ITEM_BROKEN = registerItem("shell_item_broken", new Item(new Item.Settings()));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(Corpora.MOD_ID, name), item);
    }

    public static void registerModItems() {
        Corpora.LOGGER.info("Registering Mod Items for " + Corpora.MOD_ID);
    }
}
