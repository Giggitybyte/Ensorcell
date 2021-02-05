package me.thegiggitybyte.ensorcell.mixin;

import me.thegiggitybyte.ensorcell.Ensorcell;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class DesperationMixin extends Entity {
    @Shadow public abstract boolean damage(DamageSource source, float amount);
    
    public DesperationMixin(EntityType<?> type, World world) {
        super(type, world);
    }
    
    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    public void applyExtraDamage(DamageSource source, float damage, CallbackInfoReturnable<Boolean> cir) {
        if (!(source.getAttacker() instanceof PlayerEntity)) return;
        
        PlayerEntity player = (PlayerEntity) source.getAttacker();
        float currentHealth = player.getHealth();
        float maximumHealth = player.getMaxHealth();
        
        if (currentHealth > (maximumHealth - 1)) return;
        
        for (ItemStack stack : player.getItemsHand()) {
            if (!Ensorcell.isEnchantPresent(stack, Ensorcell.Enchantments.DESPERATION)) continue;
            
            double level = EnchantmentHelper.getLevel(Ensorcell.Enchantments.DESPERATION, stack);
            double scaledPercentage = ((level * 15) * ((maximumHealth - currentHealth) / maximumHealth)) / 100;
            float extraDamage = (float) (damage * scaledPercentage);
            
            this.damage(DamageSource.MAGIC, damage + extraDamage);
            break;
        }
        
        cir.setReturnValue(false);
    }
}
