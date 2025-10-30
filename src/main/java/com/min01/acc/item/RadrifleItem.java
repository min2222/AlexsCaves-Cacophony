package com.min01.acc.item;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.client.particle.ACParticleRegistry;
import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.enchantment.ACEnchantmentRegistry;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.min01.acc.enchantment.ACCEnchantments;
import com.min01.acc.entity.ACCEntities;
import com.min01.acc.entity.projectile.EntityRadrifleBeam;
import com.min01.acc.item.animation.IAnimatableItem;
import com.min01.acc.item.renderer.RadrifleRenderer;
import com.min01.acc.util.ACCClientUtil;
import com.min01.acc.util.ACCUtil;

import net.minecraft.ChatFormatting;
import net.minecraft.client.model.HumanoidModel.ArmPose;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

public class RadrifleItem extends Item implements IAnimatableItem
{
    public static final int MAX_CHARGE = 1000;
    public static final String RADRIFLE_FIRE = "RadrifleFire";
    public static final String RADRIFLE_HOLD = "RadrifleHold";
    public static final String RADRIFLE_RUNNING = "RadrifleRunning";
    public static final String RADRIFLE_HOLD_TO_RUN = "RadrifleHoldToRun";
    public static final String RADRIFLE_OVERCHARGE_FIRE = "RadrifleOverchargeFire";
    public static final String RADRIFLE_OVERHEAT = "RadrifleOverheat";

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
        int charge = ACCUtil.getCharge(p_41404_);
		int overheat = ACCUtil.getOverlayProgress("Overheat", p_41406_);
		if(overheat >= 1000)
		{
			ACCUtil.setCharge(p_41404_, Math.min(charge + 500, MAX_CHARGE));
			ACCUtil.setOverlayProgress("Overheat", 0, p_41406_);
			ACCUtil.setItemAnimationState(p_41404_, 1);
			ACCUtil.setItemAnimationTick(p_41404_, 80);
        	ACCUtil.setPlayerAnimationState(p_41406_, 4);
        	ACCUtil.setPlayerAnimationTick(p_41406_, 80);
		}
        if(p_41404_.getEnchantmentLevel(ACEnchantmentRegistry.SOLAR.get()) > 0) 
        {
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
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) 
	{
		ItemStack stack = player.getItemInHand(hand);
        ItemStack ammo = this.findAmmo(player);
		int charge = ACCUtil.getCharge(stack);
		boolean isGamma = stack.getEnchantmentLevel(ACEnchantmentRegistry.GAMMA_RAY.get()) > 0;
		boolean isXRay = stack.getEnchantmentLevel(ACEnchantmentRegistry.X_RAY.get()) > 0;
		boolean isRicochet = stack.getEnchantmentLevel(ACCEnchantments.RICOCHET.get()) > 0;
		boolean isPulse = stack.getEnchantmentLevel(ACCEnchantments.PULSE.get()) > 0;
		boolean isOvercharge = stack.getEnchantmentLevel(ACCEnchantments.OVERCHARGE.get()) > 0;
		int count = isPulse ? 3 : 1;
        if(charge < MAX_CHARGE)
        {
        	if(ACCUtil.getItemAnimationState(stack) == 0)
        	{
            	if(isOvercharge)
            	{
                	ACCUtil.setPlayerAnimationState(player, 3);
                	ACCUtil.setPlayerAnimationTick(player, 40);
            	}
            	else
            	{
                	ACCUtil.setPlayerAnimationState(player, 1);
                	ACCUtil.setPlayerAnimationTick(player, 20);
            	}
            	if(!player.getAbilities().instabuild)
            	{
            		int overheat = ACCUtil.getOverlayProgress("Overheat", player);
            		if(overheat < 1000)
            		{
            			ACCUtil.setOverlayProgress("Overheat", Math.min(overheat + 100, 1000), player);
            		}
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
            		if(isOvercharge)
            		{
            			units = MAX_CHARGE;
            		}
                    ACCUtil.setCharge(stack, Math.min(charge + units, MAX_CHARGE));
            	}
            	for(int i = 0; i < count; i++)
            	{
                	Vec2 headRotation = new Vec2(player.getXRot(), player.yHeadRot);
                	Vec2 headRotation2 = new Vec2(player.getXRot(), (player.yHeadRot - 25.0F) + (25.0F * i));
                	Vec3 startPos = ACCUtil.getLookPos(headRotation, player.getEyePosition(), -0.25F, -0.15, 1.0F);
                	Vec3 endPos = ACCUtil.getLookPos(isPulse ? headRotation2 : headRotation, startPos, 0, 0, 20.0F);
                	HitResult hitResult = player.level.clip(new ClipContext(startPos, endPos, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));
    				EntityHitResult entityHit = ProjectileUtil.getEntityHitResult(level, player, startPos, endPos, player.getBoundingBox().expandTowards(ACCUtil.fromToVector(startPos, hitResult.getLocation(), 5.0F)).inflate(1.0D), Entity::canBeHitByProjectile);
            		Direction direction = Direction.DOWN;
            		if(hitResult instanceof BlockHitResult blockHit)
            		{
            			direction = blockHit.getDirection();
            		}
            		else if(entityHit != null)
            		{
            			Vec3 pos = entityHit.getLocation();
            			hitResult = entityHit;
            			direction = Direction.getNearest(pos.x, pos.y, pos.z);
            		}
                	Vec3 hitPos = hitResult.getLocation();
                	EntityRadrifleBeam beam = new EntityRadrifleBeam(ACCEntities.RADRIFLE_BEAM.get(), level);
                	beam.setOwner(player);
                	beam.setPos(startPos);
                	beam.setGamma(isGamma);
                	beam.setRicochet(isRicochet);
            		beam.setOvercharge(isOvercharge);
                	beam.setEndPos(isXRay ? endPos : hitPos);
                	beam.setEndDir(direction);
            		beam.onHit(hitResult);
                	level.addFreshEntity(beam);
                	if(!isRicochet && !isOvercharge)
                	{
        	            float offset = 0.05F + level.random.nextFloat() * 0.09F;
        	            Vec3 particleVec = hitPos.add(offset * direction.getStepX(), offset * direction.getStepY(), offset * direction.getStepZ());
        	            level.addParticle(ACParticleRegistry.RAYGUN_BLAST.get(), particleVec.x, particleVec.y, particleVec.z, direction.get3DDataValue(), 0, 0);
                	}
            	}
            	player.getCooldowns().addCooldown(stack.getItem(), isOvercharge ? 40 : 20);
        	}
        }
        else if(!ammo.isEmpty())
        {
        	if(!player.getAbilities().instabuild)
        	{
                ammo.shrink(1);
        	}
            ACCUtil.setCharge(stack, 0);
        }
		return InteractionResultHolder.pass(stack);
	}
	
    @Override
    public int getEnchantmentValue()
    {
        return 1;
    }
    
    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) 
    {
    	return true;
    }

    @Override
    public boolean isEnchantable(ItemStack stack)
    {
        return stack.getCount() == 1;
    }
	
	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment)
	{
		if(enchantment.category == ACEnchantmentRegistry.RAYGUN)
		{
			return true;
		}
		return super.canApplyAtEnchantingTable(stack, enchantment);
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
            ItemStack stack = entity.getInventory().getItem(i);
            if(AMMO.test(stack))
            {
                return stack;
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
	
	@Override
	public boolean isFirstPersonAnim() 
	{
		return true;
	}
}
