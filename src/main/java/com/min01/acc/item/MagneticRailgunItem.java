package com.min01.acc.item;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.item.UpdatesStackTags;
import com.min01.acc.capabilities.ACCCapabilities;
import com.min01.acc.item.renderer.MagneticRailgunRendrerer;
import com.min01.acc.util.ACCClientUtil;
import com.min01.acc.util.ACCUtil;

import net.minecraft.ChatFormatting;
import net.minecraft.client.model.HumanoidModel.ArmPose;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

public class MagneticRailgunItem extends Item implements UpdatesStackTags
{
    public static final int MAX_CHARGE = 1000;
    public static final String PICKUP = "Pickup";
    
    public static final Predicate<ItemStack> AMMO = (stack) ->
    {
        return stack.getItem() == ACBlockRegistry.TESLA_BULB.get().asItem();
    };
    
	public MagneticRailgunItem() 
	{
		super(new Item.Properties().stacksTo(1).rarity(Rarity.RARE));
	}
	
	@Override
	public void inventoryTick(ItemStack stack, Level level, Entity entity, int p_41407_, boolean p_41408_)
	{
		if(isPickup(level, stack) && !p_41408_)
		{
			setPickup(stack, null);
		}
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) 
	{
		ItemStack stack = player.getItemInHand(hand);
		Vec3 lookPos = ACCUtil.getLookPos(new Vec2(player.getXRot(), player.getYHeadRot()), player.getEyePosition(), 0.0F, 0.0F, 5.0F);
		HitResult hitResult = level.clip(new ClipContext(player.getEyePosition(), lookPos, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));
		HitResult entityHitResult = ProjectileUtil.getHitResultOnViewVector(player, t -> t != player, 8.0F);
		if(!isPickup(level, stack))
		{
    		if(hitResult instanceof BlockHitResult blockHit)
    		{
    			BlockPos blockPos = blockHit.getBlockPos();
    			BlockState blockState = level.getBlockState(blockPos);
    			if(blockState.canEntityDestroy(level, blockPos, player))
    			{
    				FallingBlockEntity fallingBlock = FallingBlockEntity.fall(level, blockPos, blockState);
    				fallingBlock.setNoGravity(true);
    				fallingBlock.getCapability(ACCCapabilities.OWNER).ifPresent(t -> t.setOwner(player.getUUID()));
    				level.removeBlock(blockPos, false);
    				setPickup(stack, fallingBlock.getUUID());
    			}
    		}
    		if(entityHitResult instanceof EntityHitResult entityHit)
    		{
    			Entity entity = entityHit.getEntity();
    			entity.getCapability(ACCCapabilities.OWNER).ifPresent(t -> t.setOwner(player.getUUID()));
    			entity.setNoGravity(true);
				setPickup(stack, entity.getUUID());
    		}
		}
		else
		{
			Entity entity = getPickup(level, stack);
			entity.setNoGravity(false);
			ACCUtil.shootFromRotation(entity, player, player.getXRot(), player.getYRot(), 0.0F, 3.5F, 1.0F);
			setPickup(stack, null);
			entity.getCapability(ACCCapabilities.OWNER).ifPresent(t -> 
			{
				t.setOwner(null);
			});
		}
		return InteractionResultHolder.pass(stack);
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
    
    public static boolean isPickup(Level level, ItemStack stack)
    {
    	return getPickup(level, stack) != null;
    }
    
    public static Entity getPickup(Level level, ItemStack stack)
    {
    	UUID uuid = getPickup(stack);
    	if(uuid != null)
    	{
    		return ACCUtil.getEntityByUUID(level, uuid);
    	}
    	return null;
    }
    
    public static UUID getPickup(ItemStack stack)
    {
        CompoundTag tag = stack.getTag();
        return tag != null && tag.contains(PICKUP) ? tag.getUUID(PICKUP) : null;
    }

    public static void setPickup(ItemStack stack, UUID pickup)
    {
        CompoundTag tag = stack.getOrCreateTag();
        if(pickup == null)
        {
        	tag.remove(PICKUP);
        }
        else
        {
        	tag.putUUID(PICKUP, pickup);
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
				return new MagneticRailgunRendrerer(ACCClientUtil.MC.getEntityModels());
			}
			
			@Override
			public @org.jetbrains.annotations.Nullable ArmPose getArmPose(LivingEntity entityLiving, InteractionHand hand, ItemStack itemStack) 
			{
				return ArmPose.BOW_AND_ARROW;
			}
		});
	}
}
