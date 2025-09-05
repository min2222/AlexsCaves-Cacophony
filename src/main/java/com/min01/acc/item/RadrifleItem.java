package com.min01.acc.item;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.enchantment.ACEnchantmentRegistry;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.min01.acc.item.animation.IAnimatableItem;
import com.min01.acc.item.renderer.RadrifleRenderer;
import com.min01.acc.util.ACCClientUtil;
import com.min01.acc.util.ACCUtil;

import net.minecraft.ChatFormatting;
import net.minecraft.client.model.HumanoidModel.ArmPose;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

public class RadrifleItem extends Item implements IAnimatableItem
{
    public static final int MAX_CHARGE = 1000;
    public static final String RADRIFLE_FIRE = "RadrifleFire";
    public static final String RADRIFLE_HOLD = "RadrifleHold";
    public static final String RADRIFLE_RUNNING = "RadrifleRunning";
    public static final String RADRIFLE_HOLD_TO_RUN = "RadrifleHoldToRun";

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
        if(p_41404_.getEnchantmentLevel(ACEnchantmentRegistry.SOLAR.get()) > 0) 
        {
            int charge = ACCUtil.getCharge(p_41404_);
            if(charge > 0 && p_41405_.random.nextFloat() < 0.02F)
            {
                BlockPos playerPos = p_41406_.blockPosition().above();
                float timeOfDay = p_41405_.getTimeOfDay(1.0F);
                if(p_41405_.canSeeSky(playerPos) && p_41405_.isDay() && !p_41405_.dimensionType().hasFixedTime() && (timeOfDay < 0.259 || timeOfDay > 0.74))
                {
                	ACCUtil.setCharge(p_41406_, p_41404_, charge - 1);
                }
            }
        }
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) 
	{
		ItemStack stack = player.getItemInHand(hand);
        ItemStack ammo = this.findAmmo(player);
		int charge = ACCUtil.getCharge(stack);
        if(ACCUtil.getCharge(stack) < MAX_CHARGE)
        {
        	ACCUtil.setPlayerAnimationState(player, 1);
        	ACCUtil.setPlayerAnimationTick(player, 10);
        	if(!player.getAbilities().instabuild)
        	{
        		int units = 100;
        		int enchantLevel = stack.getEnchantmentLevel(ACEnchantmentRegistry.ENERGY_EFFICIENCY.get());
        		if(enchantLevel == 1)
        		{
        			units = 80;
        		}
        		else if(enchantLevel == 2)
        		{
        			units = 66;
        		}
        		else if(enchantLevel >= 3)
        		{
        			units = 33;
        		}
                ACCUtil.setCharge(player, stack, Math.min(charge + units, MAX_CHARGE));
        	}
        	player.getCooldowns().addCooldown(stack.getItem(), 20);
        }
        else if(!ammo.isEmpty())
        {
            ammo.shrink(1);
            ACCUtil.setCharge(player, stack, 0);
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
				return ArmPose.EMPTY;
			}
		});
	}
	
	@Override
	public Vec3 getOffset() 
	{
		return new Vec3(0.0F, 4.0F, -5.0F);
	}
}
