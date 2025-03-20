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
import net.minecraft.nbt.ListTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
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
	public static final String ALEXS_CAVES = "alexscaves";
    public static final String CHARGE_USED = "ChargeUsed";
    public static final String IS_VISIBLE = "isVisible";
    
	public static void getClientLevel(Consumer<Level> consumer)
	{
		LogicalSidedProvider.CLIENTWORLD.get(LogicalSide.CLIENT).filter(ClientLevel.class::isInstance).ifPresent(level -> 
		{
			consumer.accept(level);
		});
	}
    
    public static void updatePlayerTick(LivingEntity player)
    {
    	player.getCapability(ACCCapabilities.PLAYER_ANIMATION).ifPresent(t -> 
    	{
    		t.update();
    	});
    }
    
    public static void updateItemTick(LivingEntity player, ItemStack stack)
    {
    	stack.getCapability(ACCCapabilities.ITEM_ANIMATION).ifPresent(t -> 
    	{
    		t.update();
    		t.setEntity(player);
    	});
    }
    
    public static int getPlayerAnimationTick(LivingEntity player)
    {
        IPlayerAnimationCapability cap = player.getCapability(ACCCapabilities.PLAYER_ANIMATION).orElse(new PlayerAnimationCapabilityImpl());
        return cap.getAnimationTick();
    }

    public static void setPlayerAnimationTick(LivingEntity player, int tick)
    {
    	player.getCapability(ACCCapabilities.PLAYER_ANIMATION).ifPresent(t -> 
    	{
    		t.setAnimationTick(tick);
    	});
    }
    
    public static int getItemAnimationTick(ItemStack stack)
    {
        IItemAnimationCapability cap = stack.getCapability(ACCCapabilities.ITEM_ANIMATION).orElse(new ItemAnimationCapabilityImpl());
        return cap.getAnimationTick();
    }

    public static void setItemAnimationTick(ItemStack stack, int tick)
    {
    	stack.getCapability(ACCCapabilities.ITEM_ANIMATION).ifPresent(t -> 
    	{
    		t.setAnimationTick(tick);
    	});
    }
    
    public static void startItemAnimation(ItemStack stack, String animationName, int tickCount)
    {
    	stopAllItemAnimations(stack);
    	AnimationState animationState = getItemAnimationState(stack, animationName);
    	animationState.startIfStopped(tickCount);
    	setItemAnimationState(stack, animationState, animationName);
    }
    
    public static void stopAllItemAnimations(ItemStack stack)
    {
    	stack.getCapability(ACCCapabilities.ITEM_ANIMATION).ifPresent(t -> 
    	{
    		ListTag list = t.getTag().getLeft();
    		for(int i = 0; i < list.size(); ++i)
    		{
    			CompoundTag compoundTag = list.getCompound(i);
    	    	AnimationState animationState = getItemAnimationState(stack, compoundTag.getString("Name"));
    	    	animationState.stop();
    	    	setItemAnimationState(stack, animationState, compoundTag.getString("Name"));
    		}
    	});
    }
    
    public static void stopItemAnimation(ItemStack stack, String animationName)
    {
    	AnimationState animationState = getItemAnimationState(stack, animationName);
    	animationState.stop();
    	setItemAnimationState(stack, animationState, animationName);
    }
    
    public static void startPlayerAnimation(Entity player, String animationName)
    {
    	stopAllPlayerAnimations(player);
    	AnimationState animationState = getPlayerAnimationState(player, animationName);
    	animationState.startIfStopped(player.tickCount);
    	setPlayerAnimationState(player, animationState, animationName);
    }
    
    public static void stopAllPlayerAnimations(Entity player)
    {
    	player.getCapability(ACCCapabilities.PLAYER_ANIMATION).ifPresent(t -> 
    	{
    		ListTag list = t.getTag().getLeft();
    		for(int i = 0; i < list.size(); ++i)
    		{
    			CompoundTag compoundTag = list.getCompound(i);
    	    	AnimationState animationState = getPlayerAnimationState(player, compoundTag.getString("Name"));
    	    	animationState.stop();
    	    	setPlayerAnimationState(player, animationState, compoundTag.getString("Name"));
    		}
    	});
    }
    
    public static void stopPlayerAnimation(Entity player, String animationName)
    {
    	AnimationState animationState = getPlayerAnimationState(player, animationName);
    	animationState.stop();
    	setPlayerAnimationState(player, animationState, animationName);
    }
    
    public static AnimationState getPlayerAnimationState(Entity player, String animationName)
    {
        IPlayerAnimationCapability cap = player.getCapability(ACCCapabilities.PLAYER_ANIMATION).orElse(new PlayerAnimationCapabilityImpl());
        return cap.getAnimationState(animationName);
    }

    public static void setPlayerAnimationState(Entity player, AnimationState state, String animationName)
    {
    	player.getCapability(ACCCapabilities.PLAYER_ANIMATION).ifPresent(t -> 
    	{
    		t.setAnimationState(state, animationName);
    	});
    }
	
    public static AnimationState getItemAnimationState(ItemStack stack, String animationName)
    {
        IItemAnimationCapability cap = stack.getCapability(ACCCapabilities.ITEM_ANIMATION).orElse(new ItemAnimationCapabilityImpl());
        return cap.getAnimationState(animationName);
    }

    public static void setItemAnimationState(ItemStack stack, AnimationState state, String animationName)
    {
    	stack.getCapability(ACCCapabilities.ITEM_ANIMATION).ifPresent(t -> 
    	{
    		t.setAnimationState(state, animationName);
    	});
    }
	
	public static void writeAnimationState(ListTag list, AnimationState state, String animationName)
	{
		CompoundTag compoundTag = getAnimationTag(list, animationName);
		compoundTag.putString("Name", animationName);
		compoundTag.putLong("LastTime", state.lastTime);
		compoundTag.putLong("AccumulatedTime", state.accumulatedTime);
		if(!hasAnimation(list, animationName))
		{
			list.add(compoundTag);
		}
	}
	
	public static boolean hasAnimation(ListTag list, String animationName)
	{
		boolean flag = false;
		for(int i = 0; i < list.size(); ++i)
		{
			CompoundTag compoundTag = list.getCompound(i);
			if(compoundTag.getString("Name").equals(animationName))
			{
				flag = true;
				break;
			}
		}
		return flag;
	}
	
	public static CompoundTag getAnimationTag(ListTag list, String animationName)
	{
		for(int i = 0; i < list.size(); ++i)
		{
			CompoundTag compoundTag = list.getCompound(i);
			if(compoundTag.getString("Name").equals(animationName))
			{
				return compoundTag;
			}
		}
		return new CompoundTag();
	}
	
	public static AnimationState readAnimationState(ListTag list, String animationName)
	{
		AnimationState state = new AnimationState();
		for(int i = 0; i < list.size(); ++i)
		{
			CompoundTag compoundTag = list.getCompound(i);
			if(compoundTag.getString("Name").equals(animationName))
			{
				state.lastTime = compoundTag.getLong("LastTime");
				state.accumulatedTime = compoundTag.getLong("AccumulatedTime");
				return state;
			}
		}
		return state;
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
        CompoundTag compoundtag = stack.getTag();
        return compoundtag != null ? compoundtag.getInt(CHARGE_USED) : 0;
    }

    public static void setCharge(Entity player, ItemStack stack, int charge)
    {
        CompoundTag compoundtag = stack.getOrCreateTag();
        compoundtag.putInt(CHARGE_USED, charge);
        
    	if(player.level.isClientSide)
    	{
            AlexsCaves.sendMSGToServer(new UpdateItemTagMessage(player.getId(), stack));
    	}
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
	
    public static boolean isMoving(Entity entity) 
    {
    	return entity.getDeltaMovement().horizontalDistanceSqr() > 1.0E-6D;
    }
}
