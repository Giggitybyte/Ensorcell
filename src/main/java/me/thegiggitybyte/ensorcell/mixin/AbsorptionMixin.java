package me.thegiggitybyte.ensorcell.mixin;

import me.thegiggitybyte.ensorcell.Ensorcell;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stat;
import net.minecraft.stat.Stats;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class AbsorptionMixin extends LivingEntity {
    @Shadow @Final public PlayerInventory inventory;
    
    public AbsorptionMixin(EntityType<? extends LivingEntity> type, World world) {
        super(type, world);
    }
    
    @Shadow protected abstract void applyDamage(DamageSource source, float amount);
    @Shadow public abstract Iterable<ItemStack> getArmorItems();
    @Shadow public abstract void incrementStat(Stat<?> stat);
    
    @Inject(method = "applyDamage", at = @At("HEAD"), cancellable = true)
    public void absorbDamage(DamageSource source, float amount, CallbackInfo ci) {
        ItemStack chestplate = this.inventory.armor.get(2);
        
        if (this.isInvulnerableTo(source) ||
                amount == Float.MAX_VALUE ||
                source == DamageSource.OUT_OF_WORLD ||
                ((this.getHealth() - amount) > 0.0f) ||
                !Ensorcell.isEnchantPresent(chestplate, Ensorcell.Enchantments.ABSORPTION)) {
            return;
        }
        
        if ((this.getHealth()) > 0.1f) this.setHealth(0.1f);
        
        int damage = chestplate.getDamage() + (Math.round(amount) * 22);
        if (damage >= chestplate.getMaxDamage()) { // Goodbye chestplate.
            this.incrementStat(Stats.BROKEN.getOrCreateStat(chestplate.getItem()));
            this.world.sendEntityStatus(this, (byte) 50); // Breaking noise.
            chestplate.setDamage(0);
            chestplate.decrement(1);
        } else {
            chestplate.setDamage(damage);
        }
        
        if (((Object) this) instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = ((ServerPlayerEntity) (Object) this);
            Criteria.ITEM_DURABILITY_CHANGED.trigger(player, chestplate, damage);
        }
        
        this.applyDamage(source, 0.0f);
        this.world.playSoundFromEntity(
                null,
                this,
                SoundEvents.ENTITY_SHULKER_CLOSE,
                SoundCategory.PLAYERS,
                2.5F,
                0.85F
        );
        
        ci.cancel();
    }
}
