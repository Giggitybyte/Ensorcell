package me.thegiggitybyte.ensorcell.enchantment;

import me.thegiggitybyte.ensorcell.Ensorcell;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;

public class DesperationEnchantment extends EnsorcellEnchantment {
    public DesperationEnchantment() {
        super(
                "desperation",
                Rarity.RARE,
                EnchantmentTarget.DIGGER,
                Ensorcell.Slots.HANDS,
                new Enchantment[]{}
        );
    }
    
    @Override
    public int getMaxLevel() {
        return 3;
    }
}
