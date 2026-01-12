package com.min01.acc.item;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.min01.acc.item.animation.IAnimatableItem;
import com.min01.acc.item.renderer.RaybladeRenderer;
import com.min01.acc.util.ACCClientUtil;
import com.min01.acc.util.ACCUtil;

import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;

public class RaybladeItem extends SwordItem implements IAnimatableItem
{
    public static final int MAX_CHARGE = 3;

    public static final Predicate<ItemStack> AMMO = (stack) ->
    {
        return stack.getItem() == ACBlockRegistry.URANIUM_ROD.get().asItem();
    };
    
	public RaybladeItem()
	{
		super(Tiers.NETHERITE, 0, 0.0F, new Item.Properties().stacksTo(1).rarity(ACItemRegistry.RARITY_NUCLEAR));
	}
	
	@Override
    public UseAnim getUseAnimation(ItemStack stack) 
    {
        return UseAnim.NONE;
    }
	
	@Override
	public int getUseDuration(ItemStack p_41454_) 
	{
		return 72000;
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand)
	{
		ItemStack stack = pPlayer.getItemInHand(pUsedHand);
		int charge = ACCUtil.getCharge(stack);
        if(charge < MAX_CHARGE)
        {
        	pPlayer.startUsingItem(pUsedHand);
        }
		return InteractionResultHolder.pass(stack);
	}
	
	@Override
	public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity, int pTimeCharged) 
	{
		if(pLivingEntity instanceof Player player)
		{
	        ItemStack ammo = this.findAmmo(player);
	        if(!ammo.isEmpty())
	        {
	        	if(!player.getAbilities().instabuild)
	        	{
	                ammo.shrink(1);
	        	}
	            ACCUtil.setCharge(pStack, 0);
	        }
		}
		ACCUtil.setPlayerAnimationState(pLivingEntity, 1);
		ACCUtil.setPlayerAnimationTick(pLivingEntity, 30);
		ACCUtil.setItemAnimationState(pStack, 1);
		ACCUtil.setItemAnimationTick(pStack, 30);
		ACCUtil.setVisible(pStack, true);
	}
	
	@Override
	public boolean onEntitySwing(ItemStack stack, LivingEntity entity)
	{
		return true;
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
	
    public static boolean hasCharge(ItemStack stack)
    {
        return ACCUtil.getCharge(stack) < MAX_CHARGE;
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
            tooltip.add(Component.translatable("item.accacophony.rayblade.charge", chargeLeft, MAX_CHARGE).withStyle(ChatFormatting.GREEN));
        }
    }
    
    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) 
    {
    	return ImmutableMultimap.of();
    }
	
	@Override
	public void initializeClient(Consumer<IClientItemExtensions> consumer) 
	{
		consumer.accept(new IClientItemExtensions()
		{
			@Override
			public BlockEntityWithoutLevelRenderer getCustomRenderer() 
			{
				return new RaybladeRenderer(ACCClientUtil.MC.getEntityModels());
			}
		});
	}
	
	@Override
	public boolean canPerformAction(ItemStack stack, ToolAction toolAction)
	{
		return toolAction == ToolActions.SWORD_DIG;
	}
	
	@Override
	public boolean isFirstPersonAnim() 
	{
		return true;
	}
}
