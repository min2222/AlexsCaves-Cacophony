package com.min01.acc.item;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.min01.acc.advancements.ACCCriteriaTriggers;
import com.min01.acc.entity.ACCEntities;
import com.min01.acc.entity.projectile.EntityMagneticRailgunBeam;
import com.min01.acc.entity.projectile.EntityThrowableFallingBlock;
import com.min01.acc.item.animation.IAnimatableItem;
import com.min01.acc.item.renderer.MagneticRailgunRendrerer;
import com.min01.acc.particle.ACCParticles;
import com.min01.acc.util.ACCClientUtil;
import com.min01.acc.util.ACCUtil;

import net.minecraft.ChatFormatting;
import net.minecraft.client.model.HumanoidModel.ArmPose;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

public class MagneticRailgunItem extends Item implements IAnimatableItem
{
    public static final int MAX_CHARGE = 1000;
    public static final String RAILGUN_HOLD = "RailgunHold";
    public static final String RAILGUN_HOLD_NEAR_WALL = "RailgunHoldNearWall";
    public static final String RAILGUN_RUNNING = "RailgunRunning";
    public static final String RAILGUN_CHARGE = "RailgunCharge";
    public static final String RAILGUN_RELOAD = "RailgunReload";
    public static final String RAILGUN_FIRE = "RailgunFire";
    
    public static final Predicate<ItemStack> AMMO = (stack) ->
    {
        return stack.getItem() == ACCItems.MAGNETIC_CARTRIDGE.get();
    };
    
	public MagneticRailgunItem() 
	{
		super(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON));
	}
	
	@Override
	public void inventoryTick(ItemStack p_41404_, Level p_41405_, Entity p_41406_, int p_41407_, boolean p_41408_) 
	{
		int charge = ACCUtil.getCharge(p_41404_);
		if(ACCUtil.getOwner(p_41406_) != null && p_41406_ instanceof Player player && p_41408_)
		{
			if(!player.getAbilities().instabuild)
			{
				if(charge <= MAX_CHARGE - 2)
				{
					ACCUtil.setCharge(p_41404_, Math.min(charge + 2, MAX_CHARGE));
				}
				else
				{
					release(player, ACCUtil.getOwner(player), p_41404_);
				}
			}
		}
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) 
	{
		ItemStack stack = player.getItemInHand(hand);
		boolean isRepel = isRepel(stack);
        ItemStack ammo = this.findAmmo(player);
		int charge = ACCUtil.getCharge(stack);
		if(charge < MAX_CHARGE)
		{
			if(ACCUtil.getPlayerAnimationState(player) == 0)
			{
				if(player.isShiftKeyDown())
				{
					if(ACCUtil.getOwner(player) == null)
					{
						setRepel(stack, !isRepel);
					}
				}
				else
				{
					if(ACCUtil.getOwner(player) != null)
					{
						Entity entity = ACCUtil.getOwner(player);
						if(entity instanceof EntityThrowableFallingBlock block && block.getBlockState().is(ACBlockRegistry.NUCLEAR_BOMB.get()) && player instanceof ServerPlayer serverPlayer)
						{
							ACCCriteriaTriggers.SHOOT_NUCLEAR_BOMB.trigger(serverPlayer);
						}
						ACCUtil.shootFromRotation(entity, player, player.getXRot(), player.getYRot(), 0.0F, 3.5F, 1.0F);
						release(player, entity, stack);
					}
					else
					{
						if(!isRepel)
						{
							HitResult hit = ProjectileUtil.getHitResultOnViewVector(player, t -> t != player, 5.0F);
				    		if(hit.getType() != Type.MISS)
				    		{
				        		if(hit instanceof EntityHitResult entityHit)
				        		{
				        			Entity entity = entityHit.getEntity();
				        			if(entity instanceof LivingEntity living)
				        			{
				            			ACCUtil.setOwner(entity, player);
				            			ACCUtil.setOwner(player, living);
				            			entity.setNoGravity(true);
				        			}
				        		}
				        		else if(hit instanceof BlockHitResult blockHit)
				        		{
				        			BlockPos blockPos = blockHit.getBlockPos();
				        			BlockState blockState = level.getBlockState(blockPos);
				        			if(blockState.canEntityDestroy(level, blockPos, player) && !blockState.isAir() && !blockState.is(BlockTags.DRAGON_IMMUNE))
				        			{
				        				EntityThrowableFallingBlock fallingBlock = new EntityThrowableFallingBlock(ACCEntities.THROWABLE_FALLING_BLOCK.get(), blockPos.getX(), blockPos.getY(), blockPos.getZ(), level);
				            			ACCUtil.setOwner(fallingBlock, player);
				            			ACCUtil.setOwner(player, fallingBlock);
				            			fallingBlock.setBlockState(blockState);
				            			fallingBlock.setNoGravity(true);
				        				level.removeBlock(blockPos, false);
				        				level.addFreshEntity(fallingBlock);
				        			}
				        		}
				    		}
						}
						else
						{
			    			player.startUsingItem(hand);
					    	ACCUtil.setPlayerAnimationState(player, 2);
					    	ACCUtil.setPlayerAnimationTick(player, 64);
			    			return InteractionResultHolder.consume(stack);
						}
					}
				}
			}
		}
        else if(!ammo.isEmpty())
        {
	    	ACCUtil.setPlayerAnimationState(player, 3);
	    	ACCUtil.setPlayerAnimationTick(player, 140);
	    	ACCUtil.setItemAnimationState(stack, 1);
	    	ACCUtil.setItemAnimationTick(stack, 140);
        	if(!player.getAbilities().instabuild)
        	{
                ammo.shrink(1);
        	}
            ACCUtil.setCharge(stack, 0);
        }
		return InteractionResultHolder.pass(stack);
	}
	
	public static void release(Entity player, Entity entity, ItemStack stack)
	{
		ACCUtil.setOwner(player, null);
		ACCUtil.setOwner(entity, null);
		entity.setNoGravity(false);
	}
	
	@Override
	public void onUseTick(Level p_41428_, LivingEntity p_41429_, ItemStack p_41430_, int p_41431_) 
	{
		if(p_41428_.isClientSide && p_41431_ % 5 == 0 && this.getUseDuration(p_41430_) - p_41431_ >= 32)
		{
			Vec3 lookPos = ACCUtil.getLookPos(new Vec2(p_41429_.getXRot(), p_41429_.getYHeadRot()), p_41429_.getEyePosition(), -0.25F, -0.25F, 2);
			p_41428_.addAlwaysVisibleParticle(ACCParticles.RAILGUN_CHARGE.get(), lookPos.x, lookPos.y, lookPos.z, 0, 0, 0);
		}
	}
	
	@Override
	public void releaseUsing(ItemStack stack, Level p_41413_, LivingEntity entity, int p_41415_)
	{
		if(this.getUseDuration(stack) - p_41415_ >= 32)
		{
			int charge = ACCUtil.getCharge(stack);
			if(charge <= MAX_CHARGE - 250)
			{
				EntityMagneticRailgunBeam beam = new EntityMagneticRailgunBeam(ACCEntities.MAGNETIC_RAILGUN_BEAM.get(), entity.level);
				Vec3 lookPos = ACCUtil.getLookPos(new Vec2(entity.getXRot(), entity.getYHeadRot()), entity.getEyePosition(), -0.25F, -0.5F, 2);
				beam.setPos(lookPos);
				beam.setOwner(entity);
				beam.setXRot(entity.getXRot());
				beam.setYRot(entity.getYHeadRot());
				beam.setBeamDamage(10.0F);
				entity.level.addFreshEntity(beam);
				if(entity instanceof Player player && !player.getAbilities().instabuild)
				{
					player.getCooldowns().addCooldown(stack.getItem(), 32);
			        ACCUtil.setCharge(stack, Math.min(charge + 250, MAX_CHARGE));
				}
		    	ACCUtil.setPlayerAnimationState(entity, 1);
		    	ACCUtil.setPlayerAnimationTick(entity, 30);
		    	ACCUtil.setItemAnimationState(stack, 2);
		    	ACCUtil.setItemAnimationTick(stack, 30);
			}
		}
		else
		{
	    	ACCUtil.setPlayerAnimationState(entity, 0);
		}
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
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn)
    {
        if(ACCUtil.getCharge(stack) != 0)
        {
            String chargeLeft = "" + (int) (MAX_CHARGE - ACCUtil.getCharge(stack));
            tooltip.add(Component.translatable("item.accacophony.magnetic_railgun.charge", chargeLeft, MAX_CHARGE).withStyle(ChatFormatting.BLUE));
        }
    }
    
	@Override
	public int getUseDuration(ItemStack p_41454_) 
	{
		return 72000;
	}
    
    public static boolean isRepel(ItemStack stack)
    {
        CompoundTag tag = stack.getTag();
        return tag != null ? tag.getBoolean("isRepel") : false;
    }

    public static void setRepel(ItemStack stack, boolean isRepel)
    {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putBoolean("isRepel", isRepel);
    }
    
    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity)
    {
    	return true;
    }
    
	@Override
	public void initializeClient(Consumer<IClientItemExtensions> consumer) 
	{
		consumer.accept(new IClientItemExtensions()
		{
			@Override
			public BlockEntityWithoutLevelRenderer getCustomRenderer() 
			{
				return new MagneticRailgunRendrerer(ACCClientUtil.MC.getEntityModels());
			}
			
			@Override
			public @org.jetbrains.annotations.Nullable ArmPose getArmPose(LivingEntity entityLiving, InteractionHand hand, ItemStack itemStack) 
			{
				return ArmPose.EMPTY;
			}
		});
	}
	
	@Override
	public Vec3 getOffset() 
	{
		return new Vec3(0.0F, 4.0F, -5.0F);
	}
	
	@Override
	public boolean isFirstPersonAnim() 
	{
		return true;
	}
}
