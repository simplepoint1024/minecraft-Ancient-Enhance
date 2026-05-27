package io.simplepoint.modpackancientfix.affix;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.shadowsoffire.apotheosis.adventure.affix.Affix;
import dev.shadowsoffire.apotheosis.adventure.affix.AffixType;
import dev.shadowsoffire.apotheosis.adventure.loot.LootCategory;
import dev.shadowsoffire.apotheosis.adventure.loot.LootRarity;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Optional;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

public class AutoSmeltAffix extends Affix {

    public static final Codec<AutoSmeltAffix> CODEC = RecordCodecBuilder.create(inst -> inst.group(
        LootRarity.CODEC.fieldOf("min_rarity").forGetter(affix -> affix.minRarity)
    ).apply(inst, AutoSmeltAffix::new));

    private final LootRarity minRarity;

    public AutoSmeltAffix(LootRarity minRarity) {
        super(AffixType.ABILITY);
        this.minRarity = minRarity;
    }

    @Override
    public boolean canApplyTo(ItemStack stack, LootCategory category, LootRarity rarity) {
        return (stack.getItem() instanceof PickaxeItem || stack.getItem() instanceof ShovelItem) && rarity.isAtLeast(this.minRarity);
    }

    @Override
    public void modifyLoot(ItemStack stack, LootRarity rarity, float level, ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        ServerLevel serverLevel = context.getLevel();
        float experience = 0.0F;

        for (int i = 0; i < generatedLoot.size(); i++) {
            ItemStack original = generatedLoot.get(i);
            if (original.isEmpty()) {
                continue;
            }

            Optional<SmeltingRecipe> recipe = serverLevel.getRecipeManager().getRecipeFor(RecipeType.SMELTING, new SimpleContainer(singleItem(original)), serverLevel);
            if (recipe.isEmpty()) {
                continue;
            }

            ItemStack result = recipe.get().getResultItem(serverLevel.registryAccess()).copy();
            if (result.isEmpty()) {
                continue;
            }

            result.setCount(result.getCount() * original.getCount());
            generatedLoot.set(i, result);
            experience += recipe.get().getExperience() * original.getCount();
        }

        if (experience <= 0.0F) {
            return;
        }

        Vec3 origin = context.getParamOrNull(LootContextParams.ORIGIN);
        if (origin == null) {
            return;
        }

        int totalExperience = Mth.floor(experience);
        float fractional = experience - totalExperience;
        if (fractional > 0.0F && context.getRandom().nextFloat() < fractional) {
            totalExperience++;
        }

        if (totalExperience > 0) {
            ExperienceOrb.award(serverLevel, origin, totalExperience);
        }
    }

    @Override
    public Codec<? extends Affix> getCodec() {
        return CODEC;
    }

    private static ItemStack singleItem(ItemStack stack) {
        ItemStack single = stack.copy();
        single.setCount(1);
        return single;
    }
}
