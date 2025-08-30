package com.min01.acc.util;

import java.lang.reflect.Method;
import java.util.UUID;
import java.util.function.Consumer;

import org.joml.Math;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.server.message.UpdateItemTagMessage;
import com.min01.acc.capabilities.ACCCapabilities;
import com.min01.acc.capabilities.IItemAnimationCapability;
import com.min01.acc.capabilities.IPlayerAnimationCapability;
import com.min01.acc.capabilities.ItemAnimationCapabilityImpl;
import com.min01.acc.capabilities.PlayerAnimationCapabilityImpl;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.LevelEntityGetter;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.LogicalSidedProvider;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

public class ACCUtil 
{
	public static final Method GET_ENTITY = ObfuscationReflectionHelper.findMethod(Level.class, "m_142646_");
	public static final String ALEXS_CAVES = "alexscaves";
    public static final String CHARGE_USED = "ChargeUsed";
    public static final String IS_VISIBLE = "isVisible";
    
    public static void runAway(PathfinderMob mob, Vec3 pos)
    {
        mob.setTarget(null);
        mob.setLastHurtByMob(null);
        if(mob.onGround())
        {
            Vec3 randomShake = new Vec3(mob.level.random.nextFloat() - 0.5F, 0, mob.level.random.nextFloat() - 0.5F).scale(0.1F);
            mob.setDeltaMovement(mob.getDeltaMovement().multiply(0.7F, 1, 0.7F).add(randomShake));
        }
        Vec3 vec = LandRandomPos.getPosAway(mob, 16, 7, pos);
        if(vec != null)
        {
            mob.getNavigation().moveTo(vec.x, vec.y, vec.z, 1.0F);
        }
    }
    
    public static void shoot(Entity projectile, double p_37266_, double p_37267_, double p_37268_, float p_37269_, float p_37270_) 
    {
    	RandomSource random = projectile.level.random;
    	Vec3 vec3 = (new Vec3(p_37266_, p_37267_, p_37268_)).normalize().add(random.triangle(0.0D, 0.0172275D * (double)p_37270_), random.triangle(0.0D, 0.0172275D * (double)p_37270_), random.triangle(0.0D, 0.0172275D * (double)p_37270_)).scale((double)p_37269_);
    	projectile.setDeltaMovement(vec3);
    	double d0 = vec3.horizontalDistance();
    	projectile.setYRot((float)(Mth.atan2(vec3.x, vec3.z) * (double)(180.0F / (float)Math.PI)));
    	projectile.setXRot((float)(Mth.atan2(vec3.y, d0) * (double)(180.0F / (float)Math.PI)));
    	projectile.yRotO = projectile.getYRot();
    	projectile.xRotO = projectile.getXRot();
    }

    public static void shootFromRotation(Entity projectile, Entity p_37252_, float p_37253_, float p_37254_, float p_37255_, float p_37256_, float p_37257_) 
    {
    	float f = -Mth.sin(p_37254_ * ((float)Math.PI / 180.0F)) * Mth.cos(p_37253_ * ((float)Math.PI / 180.0F));
    	float f1 = -Mth.sin((p_37253_ + p_37255_) * ((float)Math.PI / 180.0F));
    	float f2 = Mth.cos(p_37254_ * ((float)Math.PI / 180.0F)) * Mth.cos(p_37253_ * ((float)Math.PI / 180.0F));
    	shoot(projectile, (double)f, (double)f1, (double)f2, p_37256_, p_37257_);
    	Vec3 vec3 = p_37252_.getDeltaMovement();
    	projectile.setDeltaMovement(projectile.getDeltaMovement().add(vec3.x, p_37252_.onGround() ? 0.0D : vec3.y, vec3.z));
    }
    
	public static void getClientLevel(Consumer<Level> consumer)
	{
		LogicalSidedProvider.CLIENTWORLD.get(LogicalSide.CLIENT).filter(ClientLevel.class::isInstance).ifPresent(level -> 
		{
			consumer.accept(level);
		});
	}
	
    public static CompoundTag getPlayerAnimationTag(Entity player)
    {
    	if(player.getCapability(ACCCapabilities.PLAYER_ANIMATION).isPresent())
    	{
    		IPlayerAnimationCapability cap = player.getCapability(ACCCapabilities.PLAYER_ANIMATION).orElse(new PlayerAnimationCapabilityImpl());
    		return cap.getCompoundTag();
    	}
    	return new CompoundTag();
    }
	
    public static AnimationState getPlayerAnimation(Entity player, String name)
    {
    	if(player.getCapability(ACCCapabilities.PLAYER_ANIMATION).isPresent())
    	{
    		IPlayerAnimationCapability cap = player.getCapability(ACCCapabilities.PLAYER_ANIMATION).orElse(new PlayerAnimationCapabilityImpl());
    		return cap.getAnimationState(name);
    	}
    	return new AnimationState();
    }
    
    public static void startPlayerAnimation(Entity player, String name)
    {
    	if(player.getCapability(ACCCapabilities.PLAYER_ANIMATION).isPresent())
    	{
    		IPlayerAnimationCapability cap = player.getCapability(ACCCapabilities.PLAYER_ANIMATION).orElse(new PlayerAnimationCapabilityImpl());
    		cap.startPlayerAnimation(name);
    	}
    }
    
    public static void stopPlayerAnimation(Entity player, String name)
    {
    	if(player.getCapability(ACCCapabilities.PLAYER_ANIMATION).isPresent())
    	{
    		IPlayerAnimationCapability cap = player.getCapability(ACCCapabilities.PLAYER_ANIMATION).orElse(new PlayerAnimationCapabilityImpl());
    		cap.stopPlayerAnimation(name);
    	}
    }
    
    public static AnimationState getItemAnimation(ItemStack stack, String name)
    {
    	if(stack.getCapability(ACCCapabilities.ITEM_ANIMATION).isPresent())
    	{
    		IItemAnimationCapability cap = stack.getCapability(ACCCapabilities.ITEM_ANIMATION).orElse(new ItemAnimationCapabilityImpl());
    		return cap.getAnimationState(name);
    	}
    	return new AnimationState();
    }
    
    public static void startItemAnimation(ItemStack stack, String name)
    {
    	if(stack.getCapability(ACCCapabilities.ITEM_ANIMATION).isPresent())
    	{
    		IItemAnimationCapability cap = stack.getCapability(ACCCapabilities.ITEM_ANIMATION).orElse(new ItemAnimationCapabilityImpl());
    		cap.startItemAnimation(name);
    	}
    }
    
    public static void stopItemAnimation(ItemStack stack, String name)
    {
    	if(stack.getCapability(ACCCapabilities.ITEM_ANIMATION).isPresent())
    	{
    		IItemAnimationCapability cap = stack.getCapability(ACCCapabilities.ITEM_ANIMATION).orElse(new ItemAnimationCapabilityImpl());
    		cap.stopItemAnimation(name);
    	}
    }
    
    public static void setItemAnimationTick(ItemStack stack, int tick)
    {
    	if(stack.getCapability(ACCCapabilities.ITEM_ANIMATION).isPresent())
    	{
    		IItemAnimationCapability cap = stack.getCapability(ACCCapabilities.ITEM_ANIMATION).orElse(new ItemAnimationCapabilityImpl());
    		cap.setAnimationTick(tick);
    	}
    }
    
    public static int getItemAnimationTick(ItemStack stack)
    {
    	if(stack.getCapability(ACCCapabilities.ITEM_ANIMATION).isPresent())
    	{
    		IItemAnimationCapability cap = stack.getCapability(ACCCapabilities.ITEM_ANIMATION).orElse(new ItemAnimationCapabilityImpl());
    		return cap.getAnimationTick();
    	}
    	return 0;
    }
    
    public static int getTickCount(ItemStack stack)
    {
    	if(stack.getCapability(ACCCapabilities.ITEM_ANIMATION).isPresent())
    	{
    		IItemAnimationCapability cap = stack.getCapability(ACCCapabilities.ITEM_ANIMATION).orElse(new ItemAnimationCapabilityImpl());
    		return cap.getTickCount();
    	}
    	return 0;
    }
    
    public static void writeAnimationTime(CompoundTag tag, String name, AnimationState state)
    {
        CompoundTag animationsTag;
        if(tag.contains("Animations", 10))
        {
            animationsTag = tag.getCompound("Animations");
        }
        else
        {
            animationsTag = new CompoundTag();
            tag.put("Animations", animationsTag);
        }
        CompoundTag timeTag = new CompoundTag();
        timeTag.putLong("LastTime", state.lastTime);
        timeTag.putLong("AccumulatedTime", state.accumulatedTime);
        animationsTag.put(name, timeTag);
    }
    
    public static void readAnimationTime(CompoundTag tag, String name, AnimationState state)
    {
        if(tag.contains("Animations", 10)) 
        {
            CompoundTag animationsTag = tag.getCompound("Animations");
            if(animationsTag.contains(name, 10))
            {
                CompoundTag timeTag = animationsTag.getCompound(name);
                state.lastTime = timeTag.getLong("LastTime");
                state.accumulatedTime = timeTag.getLong("AccumulatedTime");
            }
        }
    }

    public static boolean isVisible(ItemStack stack)
    {
        CompoundTag tag = stack.getTag();
        return tag != null && tag.getBoolean(IS_VISIBLE);
    }

    public static void setVisible(ItemStack stack, boolean visible)
    {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putBoolean(IS_VISIBLE, visible);
    }
    
    public static boolean hasCharge(ItemStack stack, int maxCharge)
    {
        return getCharge(stack) < maxCharge;
    }
    
    public static int getCharge(ItemStack stack)
    {
        CompoundTag tag = stack.getTag();
        return tag != null ? tag.getInt(CHARGE_USED) : 0;
    }

    public static void setCharge(Entity player, ItemStack stack, int charge)
    {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putInt(CHARGE_USED, charge);
        
    	if(player.level.isClientSide)
    	{
            AlexsCaves.sendMSGToServer(new UpdateItemTagMessage(player.getId(), stack));
    	}
    }
	
	@SuppressWarnings("unchecked")
	public static Entity getEntityByUUID(Level level, UUID uuid)
	{
		try 
		{
			LevelEntityGetter<Entity> entities = (LevelEntityGetter<Entity>) GET_ENTITY.invoke(level);
			return entities.get(uuid);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public static boolean isModLoaded(String modid)
	{
		return ModList.get().isLoaded(modid);
	}
	
	public static Vec3 fromToPos(Vec3 from, Vec3 to)
	{
		Vec3 pos = to.subtract(from);
		return pos;
	}
	
	public static Vec3 fromToVector(Vec3 from, Vec3 to, float scale)
	{
		Vec3 motion = to.subtract(from).normalize();
		return motion.scale(scale);
	}
	
	public static Vec3 getLookPos(Vec2 rotation, Vec3 position, double left, double up, double forwards) 
	{
		Vec2 vec2 = rotation;
		Vec3 vec3 = position;
		float f = Mth.cos((vec2.y + 90.0F) * ((float)Math.PI / 180.0F));
		float f1 = Mth.sin((vec2.y + 90.0F) * ((float)Math.PI / 180.0F));
		float f2 = Mth.cos(-vec2.x * ((float)Math.PI / 180.0F));
		float f3 = Mth.sin(-vec2.x * ((float)Math.PI / 180.0F));
		float f4 = Mth.cos((-vec2.x + 90.0F) * ((float)Math.PI / 180.0F));
		float f5 = Mth.sin((-vec2.x + 90.0F) * ((float)Math.PI / 180.0F));
		Vec3 vec31 = new Vec3((double)(f * f2), (double)f3, (double)(f1 * f2));
		Vec3 vec32 = new Vec3((double)(f * f4), (double)f5, (double)(f1 * f4));
		Vec3 vec33 = vec31.cross(vec32).scale(-1.0D);
		double d0 = vec31.x * forwards + vec32.x * up + vec33.x * left;
		double d1 = vec31.y * forwards + vec32.y * up + vec33.y * left;
		double d2 = vec31.z * forwards + vec32.z * up + vec33.z * left;
		return new Vec3(vec3.x + d0, vec3.y + d1, vec3.z + d2);
	}
	
	public static Vec3 getSpreadPosition(Level level, Vec3 startPos, double range)
	{
        double x = startPos.x + (level.random.nextDouble() - level.random.nextDouble()) * range + 0.5D;
        double y = startPos.y + (level.random.nextDouble() - level.random.nextDouble()) * range + 0.5D;
        double z = startPos.z + (level.random.nextDouble() - level.random.nextDouble()) * range + 0.5D;
        return new Vec3(x, y, z);
	}
}