package com.min01.acc.item;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.client.particle.ACParticleRegistry;
import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.enchantment.ACEnchantmentRegistry;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.github.alexmodguy.alexscaves.server.item.UpdatesStackTags;
import com.github.alexmodguy.alexscaves.server.message.UpdateEffectVisualityEntityMessage;
import com.github.alexmodguy.alexscaves.server.message.UpdateItemTagMessage;
import com.github.alexmodguy.alexscaves.server.misc.ACDamageTypes;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import com.github.alexmodguy.alexscaves.server.potion.ACEffectRegistry;
import com.github.alexmodguy.alexscaves.server.potion.IrradiatedEffect;
import com.min01.acc.item.renderer.RadrifleRenderer;
import com.min01.acc.util.ACCClientUtil;
import com.min01.acc.util.ACCUtil;

import net.minecraft.ChatFormatting;
import net.minecraft.client.model.HumanoidModel.ArmPose;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

public class RadrifleItem extends Item implements UpdatesStackTags
{
    public static final int MAX_CHARGE = 1000;
    public static final String BEAM_LENGTH = "BeamLength";

    public static final Predicate<ItemStack> AMMO = (stack) ->
    {
        return stack.getItem() == ACBlockRegistry.URANIUM_ROD.get().asItem();
    };
    
	public RadrifleItem() 
	{
		super(new Item.Properties().stacksTo(1).rarity(ACItemRegistry.RARITY_NUCLEAR));
	}
	
	@Override
	public void inventoryTick(ItemStack p_41404_, Level p_41405_, Entity p_41406_, int p_41407_, boolean p_41408_)
	{
        CompoundTag compoundtag = p_41404_.getTag();
        if(compoundtag != null && compoundtag.contains(ACCUtil.ANIMATION_TICK))
        {
    		int tick = ACCUtil.getAnimationTick(p_41404_);
            if(tick <= 0)
            {
                AnimationState state = ACCUtil.getAnimationState(p_41404_);
            	state.stop();
            	ACCUtil.setAnimationState(p_41404_, state);
            }
            else
            {
            	ACCUtil.setAnimationTick(p_41404_, tick - 1);
            }
        }
        
        if(p_41404_.getEnchantmentLevel(ACEnchantmentRegistry.SOLAR.get()) > 0) 
        {
            int charge = ACCUtil.getCharge(p_41404_);
            if(charge > 0 && p_41405_.random.nextFloat() < 0.02F)
            {
                BlockPos playerPos = p_41406_.blockPosition().above();
                float timeOfDay = p_41405_.getTimeOfDay(1.0F);
                if(p_41405_.canSeeSky(playerPos) && p_41405_.isDay() && !p_41405_.dimensionType().hasFixedTime() && (timeOfDay < 0.259 || timeOfDay > 0.74))
                {
                	ACCUtil.setCharge(p_41404_, charge - 1);
                }
            }
        }
        
        if(p_41405_.isClientSide) 
        {
            AlexsCaves.sendMSGToServer(new UpdateItemTagMessage(p_41406_.getId(), p_41404_));
        }
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) 
	{
		ItemStack stack = player.getItemInHand(hand);
        ItemStack ammo = this.findAmmo(player);
		int charge = ACCUtil.getCharge(stack);
        AnimationState state = ACCUtil.getAnimationState(stack);
        if(ACCUtil.getCharge(stack) < MAX_CHARGE)
        {
        	state.startIfStopped(player.tickCount);
        	ACCUtil.setAnimationState(stack, state);
        	ACCUtil.setAnimationTick(stack, 13);
        	Vec3 riflePos = ACCUtil.getLookPos(new Vec2(player.getXRot(), player.getYHeadRot()), player.getEyePosition(), 0.0F, 0.0F, 3.0F);
			Vec3 lookPos = ACCUtil.getLookPos(new Vec2(player.getXRot(), player.getYHeadRot()), riflePos, 0.0F, 0.0F, 100.0F);

			HitResult hitResult = level.clip(new ClipContext(riflePos, lookPos, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));
			EntityHitResult entityHit = ProjectileUtil.getEntityHitResult(level, player, riflePos, lookPos, player.getBoundingBox().inflate(100.0F), Entity::canBeHitByProjectile);
			
            if(entityHit != null)
            {
            	Vec3 pos = entityHit.getLocation();
	        	setBeamLength(stack, (float) pos.subtract(riflePos).length() - 0.5F);
	        	
        		Direction direction = Direction.UP;
	            float offset = 0.05F + level.random.nextFloat() * 0.09F;
	            Vec3 particleVec = pos.add(offset * direction.getStepX(), offset * direction.getStepY(), offset * direction.getStepZ());
	            level.addParticle(ACParticleRegistry.RAYGUN_BLAST.get(), particleVec.x, particleVec.y, particleVec.z, direction.get3DDataValue(), 0, 0);
	            
	        	if(!level.isClientSide)
	        	{
	                Vec3 vec31 = pos.subtract(riflePos);
	                Vec3 vec32 = vec31.normalize();
	                List<LivingEntity> arrayList = new ArrayList<>();
	                for(int i = 1; i < Mth.floor(vec31.length()) + 1; ++i)
	                {
	                	Vec3 vec33 = riflePos.add(vec32.scale(i));
	                	List<LivingEntity> list = level.getEntitiesOfClass(LivingEntity.class, new AABB(vec33, vec33).inflate(0.1F));
	                	list.removeIf(t -> t == player || t.isAlliedTo(player) || t.getType().is(ACTagRegistry.RESISTS_RADIATION));
	                	list.forEach(t -> 
	                	{
	                		if(!arrayList.contains(t))
	                		{
	                			arrayList.add(t);
	                		}
	                	});
	                }
	                
                	arrayList.forEach(t -> 
                	{
                        boolean gamma = stack.getEnchantmentLevel(ACEnchantmentRegistry.GAMMA_RAY.get()) > 0;
                        int radiationLevel = gamma ? IrradiatedEffect.BLUE_LEVEL : 0;
                        if(t.hurt(ACDamageTypes.causeRaygunDamage(level.registryAccess(), player), gamma ? 2.0F : 1.5F)) 
                        {
                            if(t.addEffect(new MobEffectInstance(ACEffectRegistry.IRRADIATED.get(), 800, radiationLevel)))
                            {
                                AlexsCaves.sendMSGToAll(new UpdateEffectVisualityEntityMessage(t.getId(), player.getId(), gamma ? 4 : 0, 800));
                            }
                        }
                	});
	        	}
            }
            else
            {
            	Vec3 pos = hitResult.getLocation();
	        	setBeamLength(stack, (float) pos.subtract(riflePos).length() - 0.5F);
	            
	        	if(hitResult instanceof BlockHitResult blockHit)
	        	{
	        		Direction direction = blockHit.getDirection();
		            float offset = 0.05F + level.random.nextFloat() * 0.09F;
		            Vec3 particleVec = pos.add(offset * direction.getStepX(), offset * direction.getStepY(), offset * direction.getStepZ());
		            level.addParticle(ACParticleRegistry.RAYGUN_BLAST.get(), particleVec.x, particleVec.y, particleVec.z, direction.get3DDataValue(), 0, 0);
	        	}
            }
        	if(!player.getAbilities().instabuild)
        	{
                ACCUtil.setCharge(stack, Math.min(charge + 1, MAX_CHARGE));
        	}
        	player.getCooldowns().addCooldown(stack.getItem(), 60);
        }
        else if(!ammo.isEmpty())
        {
            ammo.shrink(1);
            ACCUtil.setCharge(stack, 0);
        }
        if(level.isClientSide) 
        {
            AlexsCaves.sendMSGToServer(new UpdateItemTagMessage(player.getId(), stack));
        }
		return super.use(level, player, hand);
	}
	
	@Override
	public UseAnim getUseAnimation(ItemStack p_41452_) 
	{
		return UseAnim.BOW;
	}
	
	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) 
	{
		return newStack.getItem() != this;
	}
	
    public ItemStack findAmmo(Player entity) 
    {
        for(int i = 0; i < entity.getInventory().getContainerSize(); ++i)
        {
            ItemStack itemstack1 = entity.getInventory().getItem(i);
            if(AMMO.test(itemstack1))
            {
                return itemstack1;
            }
        }
        return ItemStack.EMPTY;
    }
    
    public static float getBeamLength(ItemStack stack)
    {
        CompoundTag compoundtag = stack.getTag();
        return compoundtag != null ? compoundtag.getFloat(BEAM_LENGTH) : 0;
    }

    public static void setBeamLength(ItemStack stack, float length)
    {
        CompoundTag compoundtag = stack.getOrCreateTag();
        compoundtag.putFloat(BEAM_LENGTH, length);
    }
    
    @Override
    public boolean isBarVisible(ItemStack stack) 
    {
        return ACCUtil.getCharge(stack) != 0;
    }

    @Override
    public int getBarWidth(ItemStack stack) 
    {
        return Math.round(13.0F - (float) ACCUtil.getCharge(stack) * 13.0F / (float) MAX_CHARGE);
    }

    @Override
    public int getBarColor(ItemStack stack) 
    {
        float pulseRate = (float) ACCUtil.getCharge(stack) / (float) MAX_CHARGE * 2.0F;
        float f = AlexsCaves.PROXY.getPlayerTime() + AlexsCaves.PROXY.getPartialTicks();
        float f1 = 0.5F * (float) (1.0F + Math.sin(f * pulseRate));
        return Mth.hsvToRgb(0.3F, f1 * 0.6F + 0.2F, 1.0F);
    }
    
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn)
    {
        if(ACCUtil.getCharge(stack) != 0)
        {
            String chargeLeft = "" + (int) (MAX_CHARGE - ACCUtil.getCharge(stack));
            tooltip.add(Component.translatable("item.accacophony.radrifle.charge", chargeLeft, MAX_CHARGE).withStyle(ChatFormatting.GREEN));
        }
    }
    
	@Override
	public void initializeClient(Consumer<IClientItemExtensions> consumer) 
	{
		consumer.accept(new IClientItemExtensions()
		{
			@Override
			public BlockEntityWithoutLevelRenderer getCustomRenderer() 
			{
				return new RadrifleRenderer(ACCClientUtil.MC.getEntityModels());
			}
			
			@Override
			public @org.jetbrains.annotations.Nullable ArmPose getArmPose(LivingEntity entityLiving, InteractionHand hand, ItemStack itemStack) 
			{
				return ArmPose.BOW_AND_ARROW;
			}
		});
	}
}
