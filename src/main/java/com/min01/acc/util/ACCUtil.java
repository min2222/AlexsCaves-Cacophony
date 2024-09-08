package com.min01.acc.util;

import java.lang.reflect.Method;
import java.util.UUID;

import org.joml.Math;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.LevelEntityGetter;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

public class ACCUtil 
{
	public static final String ALEXS_CAVES = "alexscaves";
    public static final String CHARGE_USED = "ChargeUsed";
    public static final String ANIMATION_TICK = "AnimationTick";
    
    public static int getAnimationTick(ItemStack stack)
    {
        CompoundTag compoundtag = stack.getTag();
        return compoundtag != null ? compoundtag.getInt(ANIMATION_TICK) : 0;
    }

    public static void setAnimationTick(ItemStack stack, int tick)
    {
        CompoundTag compoundtag = stack.getOrCreateTag();
        compoundtag.putInt(ANIMATION_TICK, tick);
    }
	
    public static AnimationState getAnimationState(ItemStack stack)
    {
        CompoundTag compoundtag = stack.getTag();
        return compoundtag != null ? ACCUtil.readAnimationState(compoundtag) : new AnimationState();
    }

    public static void setAnimationState(ItemStack stack, AnimationState state)
    {
        CompoundTag compoundtag = stack.getOrCreateTag();
        ACCUtil.writeAnimationState(compoundtag, state);
    }
    
    public static boolean hasCharge(ItemStack stack, int maxCharge)
    {
        return ACCUtil.getCharge(stack) < maxCharge;
    }
    
    public static int getCharge(ItemStack stack)
    {
        CompoundTag compoundtag = stack.getTag();
        return compoundtag != null ? compoundtag.getInt(CHARGE_USED) : 0;
    }

    public static void setCharge(ItemStack stack, int charge)
    {
        CompoundTag compoundtag = stack.getOrCreateTag();
        compoundtag.putInt(CHARGE_USED, charge);
    }
	
	@SuppressWarnings("unchecked")
	public static Entity getEntityByUUID(Level level, UUID uuid)
	{
		Method m = ObfuscationReflectionHelper.findMethod(Level.class, "m_142646_");
		try 
		{
			LevelEntityGetter<Entity> entities = (LevelEntityGetter<Entity>) m.invoke(level);
			return entities.get(uuid);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public static void writeAnimationState(CompoundTag tag, AnimationState state)
	{
		tag.putLong("LastTime", state.lastTime);
		tag.putLong("AccumulatedTime", state.accumulatedTime);
	}
	
	public static void writeAnimationState(FriendlyByteBuf buf, AnimationState state)
	{
		buf.writeLong(state.lastTime);
		buf.writeLong(state.accumulatedTime);
	}
	
	public static AnimationState readAnimationState(CompoundTag tag)
	{
		AnimationState state = new AnimationState();
		state.lastTime = tag.getLong("LastTime");
		state.accumulatedTime = tag.getLong("AccumulatedTime");
		return state;
	}
	
	public static AnimationState readAnimationState(FriendlyByteBuf buf)
	{
		AnimationState state = new AnimationState();
		state.lastTime = buf.readLong();
		state.accumulatedTime = buf.readLong();
		return state;
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
        double x = (double) startPos.x + (level.random.nextDouble() - level.random.nextDouble()) * (double)range + 0.5D;
        double y = (double) startPos.y + (level.random.nextDouble() - level.random.nextDouble()) * (double)range + 0.5D;
        double z = (double) startPos.z + (level.random.nextDouble() - level.random.nextDouble()) * (double)range + 0.5D;
        return new Vec3(x, y, z);
	}
	
    public static boolean isMoving(Entity entity) 
    {
    	return entity.getDeltaMovement().horizontalDistanceSqr() > 1.0E-6D;
    }
}
