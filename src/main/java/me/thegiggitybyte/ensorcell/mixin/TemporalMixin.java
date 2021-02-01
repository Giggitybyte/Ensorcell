package me.thegiggitybyte.ensorcell.mixin;

import me.thegiggitybyte.ensorcell.Ensorcell;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class TemporalMixin extends Entity {
    @Shadow private int age;
    @Shadow @Final private static TrackedData<ItemStack> STACK;
    
    public TemporalMixin(EntityType<?> type, World world) {
        super(type, world);
    }
    
    @Inject(method = "tick", at = @At("HEAD"))
    private void tick(CallbackInfo info) {
        if (this.age < 5999) return;
        
        ItemStack stack = this.getDataTracker().get(STACK);
        if (Ensorcell.isEnchantPresent(stack, Ensorcell.Enchantments.TEMPORAL)) this.age = 0;
    }
}
