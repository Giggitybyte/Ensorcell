package me.thegiggitybyte.ensorcell;

import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.serializer.GsonConfigSerializer;
import me.thegiggitybyte.ensorcell.enchantment.*;
import net.fabricmc.api.ModInitializer;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;

public class Ensorcell implements ModInitializer {
    
    @Override
    public void onInitialize() {
        AutoConfig.register(Configuration.class, GsonConfigSerializer::new);
        
        Enchantments.TEMPORAL.register();
        Enchantments.ASCENSION.register();
        Enchantments.ABSORPTION.register();
        Enchantments.DESPERATION.register();
    }
    
    public static boolean isEnchantPresent(ItemStack stack, Enchantment enchant) {
        return EnchantmentHelper.getLevel(enchant, stack) > 0;
    }
    
    public static class Enchantments {
        public static final EnsorcellEnchantment TEMPORAL = new TemporalEnchantment();
        public static final EnsorcellEnchantment ASCENSION = new AscensionEnchantment();
        public static final EnsorcellEnchantment ABSORPTION = new AbsorptionEnchantment();
        // public static final EnsorcellEnchantment DASH = new DashEnchantment();
        public static final EnsorcellEnchantment DESPERATION = new DesperationEnchantment();
    }
    
    public static class Slots {
        public static final EquipmentSlot[] HANDS = new EquipmentSlot[]{EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND};
        
        public static final EquipmentSlot[] MAINHAND = new EquipmentSlot[]{EquipmentSlot.MAINHAND};
        public static final EquipmentSlot[] OFFHAND = new EquipmentSlot[]{EquipmentSlot.OFFHAND};
        
        public static final EquipmentSlot[] ARMOR = new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};
        
        public static final EquipmentSlot[] HELMET = new EquipmentSlot[]{EquipmentSlot.HEAD};
        public static final EquipmentSlot[] CHESTPLATE = new EquipmentSlot[]{EquipmentSlot.CHEST};
        public static final EquipmentSlot[] LEGGINGS = new EquipmentSlot[]{EquipmentSlot.LEGS};
        public static final EquipmentSlot[] BOOTS = new EquipmentSlot[]{EquipmentSlot.FEET};
        
        public static final EquipmentSlot[] ALL = new EquipmentSlot[]{EquipmentSlot.CHEST, EquipmentSlot.FEET, EquipmentSlot.HEAD, EquipmentSlot.LEGS, EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND};
    }
}
