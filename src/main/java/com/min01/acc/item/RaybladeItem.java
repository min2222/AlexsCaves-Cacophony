package com.min01.acc.item;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.min01.acc.item.renderer.RaybladeRenderer;
import com.min01.acc.util.ACCClientUtil;
import com.min01.acc.util.ACCUtil;

import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;

public class RaybladeItem extends SwordItem
{
    public static final String RAYBLADE_DRAW_RIGHT = "RaybladeDrawRight";
    public static final String RAYBLADE_HOLD_RIGHT = "RaybladeHoldRight";
    public static final String RAYBLADE_SWING_RIGHT = "RaybladeSwingRight";
    public static final String RAYBLADE_SWING = "RaybladeSwing";
    public static final String IS_SELECTED = "isSelected";
    public static final String FRAME = "Frame";
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
	public void inventoryTick(ItemStack p_41404_, Level p_41405_, Entity p_41406_, int p_41407_, boolean p_41408_)
	{
		int frame = getFrame(p_41404_);
		if(ACCUtil.getTickCount(p_41404_) % 2 == 0)
		{
			setFrame(p_41404_, frame + 1);
		}
		if(frame > 19)
		{
			setFrame(p_41404_, 0);
		}
		int tick = ACCUtil.getItemAnimationTick(p_41404_);
		AnimationState drawState = ACCUtil.getPlayerAnimation(p_41406_, RAYBLADE_DRAW_RIGHT);
		AnimationState swingState = ACCUtil.getPlayerAnimation(p_41406_, RAYBLADE_SWING_RIGHT);
    	if(p_41408_ && !drawState.isStarted() && !isSelected(p_41404_))
    	{
			ACCUtil.startPlayerAnimation(p_41406_, RAYBLADE_DRAW_RIGHT);
    	}
    	if(p_41408_ && swingState.isStarted())
    	{
    		if(tick == 60 - 6 && p_41406_ instanceof LivingEntity living)
    		{
    			Vec3 pos = living.getEyePosition().subtract(0, 1.5F, 0.0F);
    			Vec3 lookPos = ACCUtil.getLookPos(new Vec2(0.0F, living.yBodyRot), pos, 0.0F, 0.0F, 1.5F);
    			List<LivingEntity> list = p_41405_.getEntitiesOfClass(LivingEntity.class, new AABB(pos, lookPos).inflate(1.0F, 0.5F, 1.0F));
    			list.removeIf(t -> t == living || t.isAlliedTo(living));
    			list.forEach(t -> 
    			{
    				t.hurt(living.damageSources().mobAttack(living), 100.0F);
    			});
    		}
    		if(tick <= 0)
    		{
    			ACCUtil.setVisible(p_41404_, false);
    			stopAllAnimations(p_41406_, p_41404_);
    		}
    	}
		setSelected(p_41404_, p_41408_);
	}
	
	public static void stopAllAnimations(Entity entity, ItemStack stack)
	{
    	ACCUtil.stopPlayerAnimation(entity, RAYBLADE_DRAW_RIGHT);
    	ACCUtil.stopPlayerAnimation(entity, RAYBLADE_HOLD_RIGHT);
    	ACCUtil.stopPlayerAnimation(entity, RAYBLADE_SWING_RIGHT);
		ACCUtil.stopItemAnimation(stack, RAYBLADE_SWING);
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level p_41432_, Player p_41433_, InteractionHand p_41434_) 
	{
		ItemStack stack = p_41433_.getItemInHand(p_41434_);
		int charge = ACCUtil.getCharge(stack);
        if(charge < MAX_CHARGE)
        {
        	p_41433_.startUsingItem(p_41434_);
			stopAllAnimations(p_41433_, stack);
			ACCUtil.setVisible(stack, true);
        }
        else
        {
            ItemStack ammo = this.findAmmo(p_41433_);
            if(!ammo.isEmpty())
            {
            	ammo.shrink(1);
                ACCUtil.setCharge(p_41433_, stack, 0);
            }
        }
		return InteractionResultHolder.pass(stack);
	}
	
	@Override
	public void onUseTick(Level p_41428_, LivingEntity p_41429_, ItemStack p_41430_, int p_41431_) 
	{
		AnimationState state = ACCUtil.getPlayerAnimation(p_41429_, RAYBLADE_HOLD_RIGHT);
		if(!state.isStarted())
		{
			ACCUtil.startPlayerAnimation(p_41429_, RAYBLADE_HOLD_RIGHT);
		}
	}
	
	@Override
	public void releaseUsing(ItemStack p_41412_, Level p_41413_, LivingEntity p_41414_, int p_41415_) 
	{
		int i = this.getUseDuration(p_41412_) - p_41415_;
		int charge = ACCUtil.getCharge(p_41412_);
    	if(i >= 1)
    	{
    		stopAllAnimations(p_41414_, p_41412_);
    		ACCUtil.startPlayerAnimation(p_41414_, RAYBLADE_SWING_RIGHT);
        	ACCUtil.startItemAnimation(p_41412_, RAYBLADE_SWING);
        	ACCUtil.setItemAnimationTick(p_41412_, 60);
        	if(p_41414_ instanceof Player player)
        	{
        		if(!player.getAbilities().instabuild)
        		{
        			ACCUtil.setCharge(p_41414_, p_41412_, Math.min(charge + 1, MAX_CHARGE));
        		}
        	}
    	}
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
    
	public static int getFrame(ItemStack stack) 
	{
		CompoundTag tag = stack.getTag();
		if(tag != null)
		{
			return tag.getInt(FRAME);
		}
		return 0;
	}
	
	public static void setFrame(ItemStack stack, int frame) 
	{
		CompoundTag tag = stack.getOrCreateTag();
		tag.putInt(FRAME, frame);
	}
	
	public static boolean isSelected(ItemStack stack) 
	{
		CompoundTag tag = stack.getTag();
		return tag != null && tag.getBoolean(IS_SELECTED);
	}
	
	public static void setSelected(ItemStack stack, boolean isSelected) 
	{
		CompoundTag tag = stack.getOrCreateTag();
		tag.putBoolean(IS_SELECTED, isSelected);
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
}
