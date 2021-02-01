package me.thegiggitybyte.ensorcell.enchantment;

import me.thegiggitybyte.ensorcell.Ensorcell;
import net.minecraft.item.*;

public class TemporalEnchantment extends EnsorcellEnchantment {
    public TemporalEnchantment() {
        super(
                "temporal",
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
        Item item = stack.getItem();
        return item instanceof SwordItem ||
                item instanceof MiningToolItem ||
                item instanceof ArmorItem ||
                item instanceof TridentItem;
    }
}
