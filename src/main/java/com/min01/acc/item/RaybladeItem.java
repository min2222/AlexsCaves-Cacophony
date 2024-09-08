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
import com.min01.acc.item.renderer.RaybladeRenderer;
import com.min01.acc.util.ACCClientUtil;
import com.min01.acc.util.ACCUtil;

import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

public class RaybladeItem extends Item
{
    public static final String FRAME = "Frame";
    public static final int MAX_CHARGE = 3;

    public static final Predicate<ItemStack> AMMO = (stack) ->
    {
        return stack.getItem() == ACBlockRegistry.URANIUM_ROD.get().asItem();
    };
    
	public RaybladeItem()
	{
		super(new Item.Properties().stacksTo(1).rarity(ACItemRegistry.RARITY_NUCLEAR));
	}
	
	@Override
	public boolean canAttackBlock(BlockState p_43291_, Level p_43292_, BlockPos p_43293_, Player p_43294_)
	{
		return !p_43294_.isCreative();
	}
	
	@Override
	public float getDestroySpeed(ItemStack p_43288_, BlockState p_43289_)
	{
		if(p_43289_.is(Blocks.COBWEB)) 
		{
			return 15.0F;
		} 
		else
		{
			return p_43289_.is(BlockTags.SWORD_EFFICIENT) ? 1.5F : 1.0F;
		}
	}

	@Override
	public boolean isCorrectToolForDrops(BlockState p_43298_) 
	{
		return p_43298_.is(Blocks.COBWEB);
	}

	@Override
	public void inventoryTick(ItemStack p_41404_, Level p_41405_, Entity p_41406_, int p_41407_, boolean p_41408_)
	{
		CompoundTag tag = p_41404_.getOrCreateTag();
		updateFrame(tag, p_41406_);
		int charge = ACCUtil.getCharge(p_41404_);
        if(charge >= MAX_CHARGE)
        {
            AnimationState state = ACCUtil.getAnimationState(p_41404_);
        	state.stop();
        	ACCUtil.setAnimationState(p_41404_, state);
        }
	}
	
	@Override
	public boolean hurtEnemy(ItemStack stack, LivingEntity victim, LivingEntity attacker) 
	{
		if(attacker instanceof Player player)
		{
	        if(!player.getAbilities().instabuild)
	        {
		        int charge = ACCUtil.getCharge(stack);
		        ACCUtil.setCharge(stack, Math.min(charge + 1, MAX_CHARGE));
	        }
		}
		return super.hurtEnemy(stack, victim, attacker);
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) 
	{
		ItemStack stack = player.getItemInHand(hand);
        ItemStack ammo = this.findAmmo(player);
        AnimationState state = ACCUtil.getAnimationState(stack);
        if(!ammo.isEmpty() && ACCUtil.getCharge(stack) >= MAX_CHARGE) 
        {
            ammo.shrink(1);
            ACCUtil.setCharge(stack, 0);
        	state.startIfStopped(player.tickCount);
        	ACCUtil.setAnimationState(stack, state);
        }
		return super.use(level, player, hand);
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
	
	public static void updateFrame(CompoundTag tag, Entity entity)
	{
		if(tag.contains(FRAME))
		{
			if(entity.tickCount % 2 == 0)
			{
				tag.putInt(FRAME, tag.getInt(FRAME) + 1);
			}
			
			if(tag.getInt(FRAME) > 19)
			{
				tag.putInt(FRAME, 0);
			}
		}
		else
		{
			tag.putInt(FRAME, 0);
		}
	}
	
    public static boolean hasCharge(ItemStack stack)
    {
        return ACCUtil.getCharge(stack) < MAX_CHARGE;
    }
    
    public static int getFrame(ItemStack stack)
    {
        CompoundTag compoundtag = stack.getTag();
        return compoundtag != null ? compoundtag.getInt(FRAME) : 0;
    }
    
    public static void setFrame(ItemStack stack, int frame)
    {
        CompoundTag compoundtag = stack.getOrCreateTag();
        compoundtag.putInt(FRAME, frame);
    }
    
    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) 
    {
		ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
		ImmutableMultimap.Builder<Attribute, AttributeModifier> builder2 = ImmutableMultimap.builder();
		builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", 99.0D, AttributeModifier.Operation.ADDITION));
		builder2.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", 2.0D, AttributeModifier.Operation.ADDITION));
		if(slot == EquipmentSlot.MAINHAND)
		{
	    	return ACCUtil.getCharge(stack) < RaybladeItem.MAX_CHARGE ? builder.build() : builder2.build();
		}
    	return super.getAttributeModifiers(slot, stack);
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
}
