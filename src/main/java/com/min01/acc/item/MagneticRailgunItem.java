package com.min01.acc.item;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.min01.acc.advancements.ACCCriteriaTriggers;
import com.min01.acc.entity.ACCEntities;
import com.min01.acc.entity.projectile.EntityThrowableFallingBlock;
import com.min01.acc.item.animation.IAnimatableItem;
import com.min01.acc.item.renderer.MagneticRailgunRendrerer;
import com.min01.acc.util.ACCClientUtil;
import com.min01.acc.util.ACCUtil;

import net.minecraft.ChatFormatting;
import net.minecraft.client.model.HumanoidModel.ArmPose;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
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
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

public class MagneticRailgunItem extends Item implements IAnimatableItem
{
    public static final int MAX_CHARGE = 1000;
    public static final String RAILGUN_BASE = "RailgunBase";
    public static final String RAILGUN_ACTIVATE = "RailgunActivate";
    public static final String RAILGUN_ACTIVE = "RailgunActive";
    public static final String RAILGUN_FIRE = "RailgunFire";
    
    public static final Predicate<ItemStack> AMMO = (stack) ->
    {
        return stack.getItem() == ACBlockRegistry.TESLA_BULB.get().asItem();
    };
    
	public MagneticRailgunItem() 
	{
		super(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON));
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) 
	{
		ItemStack stack = player.getItemInHand(hand);
		if(ACCUtil.getOwner(player) != null)
		{
			Entity entity = ACCUtil.getOwner(player);
			if(entity instanceof EntityThrowableFallingBlock block && block.getBlockState().is(ACBlockRegistry.NUCLEAR_BOMB.get()) && player instanceof ServerPlayer serverPlayer)
			{
				ACCCriteriaTriggers.SHOOT_NUCLEAR_BOMB.trigger(serverPlayer);
			}
			ACCUtil.shootFromRotation(entity, player, player.getXRot(), player.getYRot(), 0.0F, 3.5F, 1.0F);
			ACCUtil.setOwner(player, null);
			ACCUtil.setOwner(entity, null);
			ACCUtil.setItemAnimationState(stack, 3);
			ACCUtil.setItemAnimationTick(stack, 40);
			entity.setNoGravity(false);
		}
		else
		{
			HitResult hit = ProjectileUtil.getHitResultOnViewVector(player, t -> t != player, 5.0F);
    		if(hit instanceof EntityHitResult entityHit)
    		{
    			Entity entity = entityHit.getEntity();
    			if(entity instanceof LivingEntity living)
    			{
        			ACCUtil.setOwner(entity, player);
        			ACCUtil.setOwner(player, living);
        			ACCUtil.setItemAnimationState(stack, 1);
        			ACCUtil.setItemAnimationTick(stack, 40);
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
        			ACCUtil.setItemAnimationState(stack, 1);
        			ACCUtil.setItemAnimationTick(stack, 40);
        			fallingBlock.setBlockState(blockState);
        			fallingBlock.setNoGravity(true);
    				level.removeBlock(blockPos, false);
    				level.addFreshEntity(fallingBlock);
    			}
    		}
		}
		return InteractionResultHolder.pass(stack);
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
