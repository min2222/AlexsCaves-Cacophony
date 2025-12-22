package com.min01.acc.util;

import java.lang.reflect.Method;
import java.util.UUID;
import java.util.function.Consumer;

import org.joml.Math;

import com.min01.acc.capabilities.ACCCapabilities;
import com.min01.acc.capabilities.IItemAnimationCapability;
import com.min01.acc.capabilities.IOverlayCapability;
import com.min01.acc.capabilities.IOwnerCapability;
import com.min01.acc.capabilities.IPlayerAnimationCapability;
import com.min01.acc.capabilities.ItemAnimationCapabilityImpl;
import com.min01.acc.capabilities.OverlayCapabilityImpl;
import com.min01.acc.capabilities.OwnerCapabilityImpl;
import com.min01.acc.capabilities.PlayerAnimationCapabilityImpl;
import com.min01.acc.item.animation.IAnimatableItem;
import com.min01.acc.misc.SmoothAnimationState;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.player.Player;
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
    	boolean flag = true;
    	if(mob instanceof TamableAnimal animal)
    	{
    		flag = !animal.isInSittingPose();
    	}
    	if(flag)
    	{
            mob.setTarget(null);
            mob.setLastHurtByMob(null);
            if(mob.onGround())
            {
                Vec3 randomShake = new Vec3(mob.level.random.nextFloat() - 0.5F, 0, mob.level.random.nextFloat() - 0.5F).scale(0.1F);
                mob.setDeltaMovement(mob.getDeltaMovement().multiply(0.7F, 1, 0.7F).add(randomShake));
            }
            Vec3 vec3 = LandRandomPos.getPosAway(mob, 16, 7, pos);
            if(vec3 != null)
            {
                mob.getNavigation().moveTo(vec3.x, vec3.y, vec3.z, 2.0F);
            }
    	}
    }
    
	public static void tickOwner(Entity entity)
	{
		IOwnerCapability cap = entity.getCapability(ACCCapabilities.OWNER).orElse(new OwnerCapabilityImpl());
		cap.tick(entity);
	}
	
	public static void setOwner(Entity entity, Entity owner)
	{
		IOwnerCapability cap = entity.getCapability(ACCCapabilities.OWNER).orElse(new OwnerCapabilityImpl());
		cap.setOwner(owner);
	}
    
	public static Entity getOwner(Entity entity)
	{
		IOwnerCapability cap = entity.getCapability(ACCCapabilities.OWNER).orElse(new OwnerCapabilityImpl());
		return cap.getOwner(entity);
	}
    
    public static void shoot(Entity projectile, double pX, double pY, double pZ, float pVelocity, float pInaccuracy) 
    {
    	RandomSource random = projectile.level.random;
        Vec3 vec3 = (new Vec3(pX, pY, pZ)).normalize().add(random.triangle(0.0D, 0.0172275D * (double)pInaccuracy), random.triangle(0.0D, 0.0172275D * (double)pInaccuracy), random.triangle(0.0D, 0.0172275D * (double)pInaccuracy)).scale((double)pVelocity);
        projectile.setDeltaMovement(vec3);
        double d0 = vec3.horizontalDistance();
        projectile.setYRot((float)(Mth.atan2(vec3.x, vec3.z) * (double)(180.0F / (float)Math.PI)));
        projectile.setXRot((float)(Mth.atan2(vec3.y, d0) * (double)(180.0F / (float)Math.PI)));
        projectile.yRotO = projectile.getYRot();
        projectile.xRotO = projectile.getXRot();
    }

    public static void shootFromRotation(Entity projectile, Entity pShooter, float pX, float pY, float pZ, float pVelocity, float pInaccuracy) 
    {
        float f = -Mth.sin(pY * ((float)Math.PI / 180.0F)) * Mth.cos(pX * ((float)Math.PI / 180.0F));
        float f1 = -Mth.sin((pX + pZ) * ((float)Math.PI / 180.0F));
        float f2 = Mth.cos(pY * ((float)Math.PI / 180.0F)) * Mth.cos(pX * ((float)Math.PI / 180.0F));
        shoot(projectile, (double)f, (double)f1, (double)f2, pVelocity, pInaccuracy);
        Vec3 vec3 = pShooter.getDeltaMovement();
        projectile.setDeltaMovement(projectile.getDeltaMovement().add(vec3.x, pShooter.onGround() ? 0.0D : vec3.y, vec3.z));
    }
    
	public static void getClientLevel(Consumer<Level> consumer)
	{
		LogicalSidedProvider.CLIENTWORLD.get(LogicalSide.CLIENT).filter(ClientLevel.class::isInstance).ifPresent(level -> 
		{
			consumer.accept(level);
		});
	}
	
    public static void tickOverlayProgress(LivingEntity player)
    {
		IOverlayCapability cap = player.getCapability(ACCCapabilities.OVERLAY).orElse(new OverlayCapabilityImpl());
		cap.tick(player);
    }
    
    public static void setOverlayProgress(String name, int value, Entity player)
    {
		IOverlayCapability cap = player.getCapability(ACCCapabilities.OVERLAY).orElse(new OverlayCapabilityImpl());
		cap.setOverlayProgress(name, value);
    }
    
    public static int getOverlayProgress(String name, Entity player)
    {
		IOverlayCapability cap = player.getCapability(ACCCapabilities.OVERLAY).orElse(new OverlayCapabilityImpl());
		return cap.getOverlayProgress(name);
    }
    
    public static void tickItemAnimation(Player player)
    {
		for(int i = 0; i < player.getInventory().getContainerSize(); i++)
		{
			ItemStack stack = player.getInventory().getItem(i);
			if(stack.getItem() instanceof IAnimatableItem)
			{
				stack.getCapability(ACCCapabilities.ITEM_ANIMATION).ifPresent(t -> 
				{
					t.tick(player, stack);
				});
			}
		}
    }
    
    public static void tickPlayerAnimation(LivingEntity player)
    {
		IPlayerAnimationCapability cap = player.getCapability(ACCCapabilities.PLAYER_ANIMATION).orElse(new PlayerAnimationCapabilityImpl());
		cap.tick(player);
    }
    
    public static void setPlayerAnimationState(Entity player, int state)
    {
		IPlayerAnimationCapability cap = player.getCapability(ACCCapabilities.PLAYER_ANIMATION).orElse(new PlayerAnimationCapabilityImpl());
		cap.setAnimationState(state);
    }
    
    public static int getPlayerAnimationState(Entity player)
    {
		IPlayerAnimationCapability cap = player.getCapability(ACCCapabilities.PLAYER_ANIMATION).orElse(new PlayerAnimationCapabilityImpl());
		return cap.getAnimationState();
    }
    
    public static SmoothAnimationState getPlayerAnimationStateByName(Entity player, String name)
    {
		IPlayerAnimationCapability cap = player.getCapability(ACCCapabilities.PLAYER_ANIMATION).orElse(new PlayerAnimationCapabilityImpl());
		return cap.getAnimationStateByName(name);
    }
    
    public static void setPlayerAnimationTick(Entity player, int tick)
    {
		IPlayerAnimationCapability cap = player.getCapability(ACCCapabilities.PLAYER_ANIMATION).orElse(new PlayerAnimationCapabilityImpl());
		cap.setAnimationTick(tick);
    }
    
    public static int getPlayerAnimationTick(Entity player)
    {
		IPlayerAnimationCapability cap = player.getCapability(ACCCapabilities.PLAYER_ANIMATION).orElse(new PlayerAnimationCapabilityImpl());
		return cap.getAnimationTick();
    }
    
    public static void setItemAnimationState(ItemStack stack, int state)
    {
		IItemAnimationCapability cap = stack.getCapability(ACCCapabilities.ITEM_ANIMATION).orElse(new ItemAnimationCapabilityImpl());
		cap.setAnimationState(state);
    }
    
    public static int getItemAnimationState(ItemStack stack)
    {
		IItemAnimationCapability cap = stack.getCapability(ACCCapabilities.ITEM_ANIMATION).orElse(new ItemAnimationCapabilityImpl());
		return cap.getAnimationState();
    }
    
    public static SmoothAnimationState getItemAnimationStateByName(ItemStack stack, String name)
    {
		IItemAnimationCapability cap = stack.getCapability(ACCCapabilities.ITEM_ANIMATION).orElse(new ItemAnimationCapabilityImpl());
		return cap.getAnimationStateByName(name);
    }
    
    public static void setItemAnimationTick(ItemStack stack, int tick)
    {
		IItemAnimationCapability cap = stack.getCapability(ACCCapabilities.ITEM_ANIMATION).orElse(new ItemAnimationCapabilityImpl());
		cap.setAnimationTick(tick);
    }
    
    public static int getItemAnimationTick(ItemStack stack)
    {
		IItemAnimationCapability cap = stack.getCapability(ACCCapabilities.ITEM_ANIMATION).orElse(new ItemAnimationCapabilityImpl());
		return cap.getAnimationTick();
    }
    
    public static int getItemTickCount(ItemStack stack)
    {
		IItemAnimationCapability cap = stack.getCapability(ACCCapabilities.ITEM_ANIMATION).orElse(new ItemAnimationCapabilityImpl());
		return cap.getTickCount();
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

    public static void setCharge(ItemStack stack, int charge)
    {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putInt(CHARGE_USED, charge);
    }
	
	@SuppressWarnings("unchecked")
	public static <T extends Entity> T getEntityByUUID(Level level, UUID uuid)
	{
		try 
		{
			LevelEntityGetter<Entity> entities = (LevelEntityGetter<Entity>) GET_ENTITY.invoke(level);
			return (T) entities.get(uuid);
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
	
	public static Vec3 getPositionTowards(Vec3 from, Vec3 to)
	{
		Vec3 pos = to.subtract(from);
		return pos;
	}
	
	public static Vec3 getVelocityTowards(Vec3 from, Vec3 to, float speed)
	{
		Vec3 motion = to.subtract(from).normalize();
		return motion.scale(speed);
	}
	
	public static Vec2 lookAt(Vec3 startPos, Vec3 pos)
	{
		Vec3 vec3 = startPos;
		double d0 = pos.x - vec3.x;
		double d1 = pos.y - vec3.y;
		double d2 = pos.z - vec3.z;
		double d3 = Math.sqrt(d0 * d0 + d2 * d2);
		float xRot = Mth.wrapDegrees((float)(-(Mth.atan2(d1, d3) * (double)(180.0F / (float)Math.PI))));
		float yRot = Mth.wrapDegrees((float)(Mth.atan2(d2, d0) * (double)(180.0F / (float)Math.PI)) - 90.0F);
	    return new Vec2(xRot, yRot);
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