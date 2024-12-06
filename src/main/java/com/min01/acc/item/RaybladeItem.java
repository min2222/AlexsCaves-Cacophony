package com.min01.acc.item;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.github.alexmodguy.alexscaves.server.item.UpdatesStackTags;
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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

public class RaybladeItem extends Item implements UpdatesStackTags
{
    public static final String FRAME = "Frame";
    public static final String RAYBLADE_DRAW_RIGHT = "RaybladeDrawRight";
    public static final String RAYBLADE_HOLD_RIGHT = "RaybladeHoldRight";
    public static final String RAYBLADE_SWING_RIGHT = "RaybladeSwingRight";
    public static final String RAYBLADE_SWING = "RaybladeSwing";
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
    	ACCUtil.animationTick(p_41406_, p_41404_);
    	int tick = ACCUtil.getAnimationTick(p_41404_);
    	//FIXME item animation stop immediately after started;
    	if(p_41408_)
    	{
    		if(!ACCUtil.getPlayerAnimationState(p_41406_, RAYBLADE_HOLD_RIGHT).isStarted() && !ACCUtil.getPlayerAnimationState(p_41406_, RAYBLADE_SWING_RIGHT).isStarted())
    		{
    			ACCUtil.setVisible(p_41406_, p_41404_, false);
    			ACCUtil.startPlayerAnimation(p_41406_, RAYBLADE_DRAW_RIGHT);
    		}
        	if(ACCUtil.getItemAnimationState(p_41404_, RAYBLADE_SWING).isStarted())
        	{
        		if(tick <= 0)
        		{
        			ACCUtil.setVisible(p_41406_, p_41404_, false);
                	ACCUtil.stopItemAnimation(p_41406_, p_41404_, RAYBLADE_SWING);
                	ACCUtil.stopPlayerAnimation(p_41406_, RAYBLADE_HOLD_RIGHT);
                	ACCUtil.stopPlayerAnimation(p_41406_, RAYBLADE_SWING_RIGHT);
        		}
        		else if(tick == 9 && p_41406_ instanceof LivingEntity living)
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
        	}
    	}
    	else
    	{
			ACCUtil.setVisible(p_41406_, p_41404_, false);
        	ACCUtil.stopItemAnimation(p_41406_, p_41404_, RAYBLADE_SWING);
        	ACCUtil.stopPlayerAnimation(p_41406_, RAYBLADE_HOLD_RIGHT);
        	ACCUtil.stopPlayerAnimation(p_41406_, RAYBLADE_SWING_RIGHT);
    	}
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level p_41432_, Player p_41433_, InteractionHand p_41434_) 
	{
		ItemStack stack = p_41433_.getItemInHand(p_41434_);
		int charge = ACCUtil.getCharge(stack);
        if(charge < MAX_CHARGE)
        {
        	p_41433_.startUsingItem(p_41434_);
			ACCUtil.setVisible(p_41433_, stack, true);
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
		ACCUtil.startPlayerAnimation(p_41429_, RAYBLADE_HOLD_RIGHT);
	}
	
	@Override
	public void releaseUsing(ItemStack p_41412_, Level p_41413_, LivingEntity p_41414_, int p_41415_) 
	{
		int i = this.getUseDuration(p_41412_) - p_41415_;
		int charge = ACCUtil.getCharge(p_41412_);
    	if(i >= 1)
    	{
    		ACCUtil.stopPlayerAnimation(p_41414_, RAYBLADE_HOLD_RIGHT);
    		ACCUtil.startPlayerAnimation(p_41414_, RAYBLADE_SWING_RIGHT);
        	ACCUtil.startItemAnimation(p_41414_, p_41412_, RAYBLADE_SWING);
        	ACCUtil.setAnimationTick(p_41412_, 15);
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
