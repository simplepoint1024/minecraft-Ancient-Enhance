package io.simplepoint.modpackancientfix.affix;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.shadowsoffire.apotheosis.adventure.affix.Affix;
import dev.shadowsoffire.apotheosis.adventure.affix.AffixType;
import dev.shadowsoffire.apotheosis.adventure.loot.LootCategory;
import dev.shadowsoffire.apotheosis.adventure.loot.LootRarity;
import dev.shadowsoffire.apotheosis.adventure.socket.gem.bonus.GemBonus;
import dev.shadowsoffire.placebo.util.StepFunction;
import java.util.Map;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;

public class ElytraBoostAffix extends Affix {

    public static final Codec<ElytraBoostAffix> CODEC = RecordCodecBuilder.create(inst -> inst.group(
        GemBonus.VALUES_CODEC.fieldOf("values").forGetter(affix -> affix.values)
    ).apply(inst, ElytraBoostAffix::new));

    private final Map<LootRarity, StepFunction> values;

    public ElytraBoostAffix(Map<LootRarity, StepFunction> values) {
        super(AffixType.ABILITY);
        this.values = values;
    }

    @Override
    public boolean canApplyTo(ItemStack stack, LootCategory category, LootRarity rarity) {
        return category.isLightWeapon() && this.values.containsKey(rarity);
    }

    @Override
    public MutableComponent getDescription(ItemStack stack, LootRarity rarity, float level) {
        return Component.translatable("affix.%s.desc".formatted(this.getId()), this.getRocketFlight(rarity, level));
    }

    @Override
    public Component getAugmentingText(ItemStack stack, LootRarity rarity, float level) {
        MutableComponent desc = this.getDescription(stack, rarity, level);
        Component min = Component.literal(fmt(this.getRocketFlight(rarity, 0.0F)));
        Component max = Component.literal(fmt(this.getRocketFlight(rarity, 1.0F)));
        return desc.append(valueBounds(min, max));
    }

    public int getRocketFlight(LootRarity rarity, float level) {
        return this.values.get(rarity).getInt(level);
    }

    @Override
    public Codec<? extends Affix> getCodec() {
        return CODEC;
    }
}
