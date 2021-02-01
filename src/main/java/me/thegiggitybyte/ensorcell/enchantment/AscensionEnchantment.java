package me.thegiggitybyte.ensorcell.enchantment;

import me.thegiggitybyte.ensorcell.Ensorcell;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class AscensionEnchantment extends EnsorcellEnchantment {
    public AscensionEnchantment() {
        super(
                "ascension",
                Rarity.VERY_RARE,
                null,
                Ensorcell.Slots.ALL
        );
    }
    
    @Override
    public boolean isTreasure() {
        return true;
    }
    
    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.getItem() == Items.TOTEM_OF_UNDYING;
    }
}
