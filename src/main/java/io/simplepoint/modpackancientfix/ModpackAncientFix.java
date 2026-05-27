package io.simplepoint.modpackancientfix;

import dev.shadowsoffire.apotheosis.adventure.affix.AffixHelper;
import dev.shadowsoffire.apotheosis.adventure.affix.AffixInstance;
import dev.shadowsoffire.apotheosis.adventure.affix.AffixRegistry;
import dev.shadowsoffire.apotheosis.adventure.affix.Affix;
import dev.shadowsoffire.apotheosis.adventure.loot.LootRarity;
import dev.shadowsoffire.placebo.reload.DynamicHolder;
import io.simplepoint.modpackancientfix.affix.AutoSmeltAffix;
import io.simplepoint.modpackancientfix.affix.ElytraBoostAffix;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;

@Mod(ModpackAncientFix.MOD_ID)
public class ModpackAncientFix {

    public static final String MOD_ID = "modpackancientfix";
    private static final ResourceLocation ANCIENT_MATERIAL_ID = ResourceLocation.fromNamespaceAndPath("apotheosis", "ancient_material");
    private static final ResourceLocation APOTHEOSIS_ADVENTURE_TAB_ID = ResourceLocation.fromNamespaceAndPath("apotheosis", "adventure");
    private static final ResourceLocation AUTO_SMELT_CODEC_ID = ResourceLocation.fromNamespaceAndPath(MOD_ID, "auto_smelt");
    private static final ResourceLocation ELYTRA_BOOST_CODEC_ID = ResourceLocation.fromNamespaceAndPath(MOD_ID, "elytra_boost");
    private static final ResourceLocation ELYTRA_BOOST_AFFIX_ID = ResourceLocation.fromNamespaceAndPath("apotheosis", "sword/special/elytra_boost");
    private static final String ANCIENT_RARITY_ID = "apotheosis:ancient";
    private static final String AFFIX_DATA_TAG = "affix_data";
    private static final String RARITY_TAG = "rarity";
    private static final String UNBREAKABLE_TAG = "Unbreakable";
    private static final String ANCIENT_UNBREAKABLE_MARKER = "modpackancientfix_ancient_unbreakable";
    private static final DynamicHolder<ElytraBoostAffix> ELYTRA_BOOST_AFFIX = AffixRegistry.INSTANCE.holder(ELYTRA_BOOST_AFFIX_ID);

    public ModpackAncientFix() {
        AffixRegistry.INSTANCE.registerCodec(AUTO_SMELT_CODEC_ID, AutoSmeltAffix.CODEC);
        AffixRegistry.INSTANCE.registerCodec(ELYTRA_BOOST_CODEC_ID, ElytraBoostAffix.CODEC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::addCreativeEntries);
        MinecraftForge.EVENT_BUS.register(this);
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

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.player.level().isClientSide) {
            return;
        }

        event.player.getInventory().items.forEach(this::syncAncientUnbreakable);
        event.player.getInventory().armor.forEach(this::syncAncientUnbreakable);
        event.player.getInventory().offhand.forEach(this::syncAncientUnbreakable);
    }

    @SubscribeEvent
    public void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        Player player = event.getEntity();
        if (!player.isFallFlying()) {
            return;
        }

        ItemStack stack = event.getItemStack();
        AffixInstance instance = AffixHelper.getAffixes(stack).get(ELYTRA_BOOST_AFFIX);
        if (instance == null || !instance.isValid()) {
            return;
        }

        event.setCancellationResult(InteractionResult.sidedSuccess(event.getLevel().isClientSide));
        event.setCanceled(true);

        if (event.getLevel().isClientSide || Affix.isOnCooldown(ELYTRA_BOOST_AFFIX_ID, 1, player)) {
            return;
        }

        LootRarity rarity = instance.rarity().get();
        ElytraBoostAffix affix = ELYTRA_BOOST_AFFIX.get();
        int flight = affix.getRocketFlight(rarity, instance.level());
        ItemStack rocket = new ItemStack(Items.FIREWORK_ROCKET);
        rocket.getOrCreateTagElement("Fireworks").putByte("Flight", (byte) flight);
        event.getLevel().addFreshEntity(new FireworkRocketEntity(event.getLevel(), rocket, player));
        player.swing(event.getHand(), true);
        Affix.startCooldown(ELYTRA_BOOST_AFFIX_ID, player);
    }

    private void syncAncientUnbreakable(ItemStack stack) {
        if (stack.isEmpty()) {
            return;
        }

        if (isAncientAffixItem(stack)) {
            CompoundTag tag = stack.getOrCreateTag();
            if (!tag.getBoolean(UNBREAKABLE_TAG) || !tag.getBoolean(ANCIENT_UNBREAKABLE_MARKER)) {
                tag.putBoolean(UNBREAKABLE_TAG, true);
                tag.putBoolean(ANCIENT_UNBREAKABLE_MARKER, true);
            }
            return;
        }

        CompoundTag tag = stack.getTag();
        if (tag == null || !tag.getBoolean(ANCIENT_UNBREAKABLE_MARKER)) {
            return;
        }

        tag.remove(UNBREAKABLE_TAG);
        tag.remove(ANCIENT_UNBREAKABLE_MARKER);
        if (tag.isEmpty()) {
            stack.setTag(null);
        }
    }

    private boolean isAncientAffixItem(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag == null || !tag.contains(AFFIX_DATA_TAG, Tag.TAG_COMPOUND)) {
            return false;
        }

        CompoundTag affixData = tag.getCompound(AFFIX_DATA_TAG);
        return affixData.contains(RARITY_TAG, Tag.TAG_STRING) && ANCIENT_RARITY_ID.equals(affixData.getString(RARITY_TAG));
    }
}
