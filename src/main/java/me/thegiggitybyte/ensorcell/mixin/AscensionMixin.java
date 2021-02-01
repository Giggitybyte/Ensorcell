package me.thegiggitybyte.ensorcell.mixin;

import com.mojang.authlib.GameProfile;
import me.thegiggitybyte.ensorcell.Ensorcell;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.BlockState;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldProperties;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerEntity.class)
public abstract class AscensionMixin extends PlayerEntity {
    public AscensionMixin(World world, BlockPos pos, float yaw, GameProfile profile) {
        super(world, pos, yaw, profile);
    }
    
    @Shadow public abstract boolean isSpawnPointSet();
    @Shadow @Nullable public abstract BlockPos getSpawnPointPosition();
    @Shadow public abstract void sendMessage(Text message, boolean actionBar);
    
    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    public void saveFromVoid(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if ((source != DamageSource.OUT_OF_WORLD) | (amount == Float.MAX_VALUE)) return;
        
        ItemStack handStack = ItemStack.EMPTY;
        for (ItemStack stack : this.getItemsHand()) {
            if (stack.getItem() == Items.TOTEM_OF_UNDYING) {
                handStack = stack;
                break;
            }
        }
        
        if (!Ensorcell.isEnchantPresent(handStack, Ensorcell.Enchantments.ASCENSION)) return;
        
        this.incrementStat(Stats.USED.getOrCreateStat(Items.TOTEM_OF_UNDYING));
        Criteria.USED_TOTEM.trigger((ServerPlayerEntity) (Object) this, handStack);
        handStack.decrement(1);
        
        BlockPos levitationTarget = null;
        BlockPos centerPos = new BlockPos(this.getX(), -10, this.getZ());
        
        for (BlockPos pos : BlockPos.iterateOutwards(centerPos, 256, 256, 256)) {
            BlockState block = this.world.getBlockState(pos);
            if (block.isAir() | !block.isFullCube(this.world, pos)) continue;
            
            if (world.getBlockState(pos.up(1)).isAir() && world.getBlockState(pos.up(2)).isAir()) {
                levitationTarget = pos;
                break;
            }
        }
        
        this.clearStatusEffects();
        
        if (levitationTarget != null) {
            this.teleport(getX(), -10, getZ());
            
            int distance = levitationTarget.getManhattanDistance(this.getBlockPos());
            int duration = MathHelper.ceil(((distance * 0.1) + distance) + 5);
            
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.LEVITATION, duration * 20, 0));
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, duration * 20 + 100, 0));
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, (duration * 20) / 2, 1));
            
        } else {
            this.setHealth(20.0f);
            this.fallDistance = 0.0f;
            
            LiteralText msg = new LiteralText("Could not find a solid block to float to!\nYou've been sent to ");
            
            if (this.isSpawnPointSet()) {
                BlockPos spawnPos = this.getSpawnPointPosition();
                this.teleport(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ());
                
                msg.append("your spawn point");
                
            } else {
                WorldProperties worldInfo = this.world.getLevelProperties();
                this.teleport(worldInfo.getSpawnX(), worldInfo.getSpawnY(), worldInfo.getSpawnZ());
                
                msg.append("the world spawn");
            }
            
            msg.append(" instead.");
            msg.formatted(Formatting.ITALIC, Formatting.GRAY);
            this.sendMessage(msg, false);
        }
        
        this.world.sendEntityStatus(this, (byte) 35); // Send totem animation to client.
        cir.setReturnValue(false);
    }
}
