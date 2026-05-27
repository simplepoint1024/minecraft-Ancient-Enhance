package io.simplepoint.modpackancientfix;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;

@Mod(ModpackAncientFix.MOD_ID)
public class ModpackAncientFix {

    public static final String MOD_ID = "modpackancientfix";
    private static final ResourceLocation ANCIENT_MATERIAL_ID = ResourceLocation.fromNamespaceAndPath("apotheosis", "ancient_material");
    private static final ResourceLocation APOTHEOSIS_ADVENTURE_TAB_ID = ResourceLocation.fromNamespaceAndPath("apotheosis", "adventure");

    public ModpackAncientFix() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::addCreativeEntries);
    }

    private void addCreativeEntries(BuildCreativeModeTabContentsEvent event) {
        ResourceLocation tabId = event.getTabKey().location();
        if (!ResourceLocation.fromNamespaceAndPath("minecraft", "ingredients").equals(tabId) && !APOTHEOSIS_ADVENTURE_TAB_ID.equals(tabId)) {
            return;
        }

        Item ancientMaterial = ForgeRegistries.ITEMS.getValue(ANCIENT_MATERIAL_ID);
        if (ancientMaterial == null) {
            return;
        }

        event.accept(() -> ancientMaterial);
    }
}
