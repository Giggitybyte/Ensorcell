package me.thegiggitybyte.ensorcell.enchantment;

import me.thegiggitybyte.ensorcell.Ensorcell;
import net.minecraft.enchantment.EnchantmentTarget;

public class AbsorptionEnchantment extends EnsorcellEnchantment {
    public AbsorptionEnchantment() {
        super(
                "absorption",
                Rarity.VERY_RARE,
                EnchantmentTarget.ARMOR_CHEST,
                Ensorcell.Slots.CHESTPLATE
        );
    }
}
