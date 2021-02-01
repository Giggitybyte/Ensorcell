package me.thegiggitybyte.ensorcell.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Arrays;
import java.util.List;

public class EnsorcellEnchantment extends Enchantment {
    protected final List<Enchantment> incompatibleEnchantments;
    public final String id;
    
    public EnsorcellEnchantment(String id, Rarity rarity, EnchantmentTarget target, EquipmentSlot[] slotTypes, Enchantment[] incompatibleEnchants) {
        super(rarity, target, slotTypes);
        
        incompatibleEnchantments = Arrays.asList(incompatibleEnchants);
        this.id = id;
    }
    
    public EnsorcellEnchantment(String id, Rarity rarity, EnchantmentTarget target, EquipmentSlot[] slotTypes, Enchantment incompatibleEnchant) {
        this(id, rarity, target, slotTypes, new Enchantment[]{incompatibleEnchant});
    }
    
    public EnsorcellEnchantment(String id, Rarity rarity, EnchantmentTarget target, EquipmentSlot[] slotTypes) {
        this(id, rarity, target, slotTypes, new Enchantment[]{});
    }
    
    @Override
    public Text getName(int level) {
        MutableText name = new TranslatableText(this.getTranslationKey());
        
        if (level != 1 || this.getMaxLevel() != 1) {
            name.append(" ").append(Numeral.get(level));
        }
        
        if (this.isCursed()) {
            name.formatted(Formatting.RED);
        } else {
            name.formatted(Formatting.GRAY);
        }
        
        return name;
    }
    
    @Override
    public boolean canAccept(Enchantment other) {
        return !incompatibleEnchantments.contains(other);
    }
    
    public void register() {
        Registry.register(Registry.ENCHANTMENT, new Identifier("ensorcell", this.id), this);
    }
    
    protected static class Numeral { // Based off of: https://stackoverflow.com/a/39429499
        private static final String[] ONES = {"", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX"};
        private static final String[] TENS = {"", "X", "XX", "XXX", "XL", "L", "LX", "LXX", "LXXX", "XC"};
        private static final String[] HUNDREDS = {"", "C", "CC", "CCC", "CD", "D", "DC", "DCC", "DCCC", "CM"};
        private static final String[] THOUSANDS = {"", "M", "MM", "MMM"};
        
        public static String get(int enchantmentLevel) {
            if (enchantmentLevel < 1 || enchantmentLevel > 3999) {
                throw new IllegalArgumentException("Invalid enchantment level; must be between 1 and 3999.");
            }
            
            return THOUSANDS[enchantmentLevel / 1000] +
                    HUNDREDS[enchantmentLevel % 1000 / 100] +
                    TENS[enchantmentLevel % 100 / 10] +
                    ONES[enchantmentLevel % 10];
        }
    }
}
