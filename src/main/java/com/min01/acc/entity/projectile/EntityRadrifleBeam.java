package com.min01.acc.entity.projectile;

import java.util.ArrayList;
import java.util.List;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.server.message.UpdateEffectVisualityEntityMessage;
import com.github.alexmodguy.alexscaves.server.misc.ACDamageTypes;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import com.github.alexmodguy.alexscaves.server.potion.ACEffectRegistry;
import com.github.alexmodguy.alexscaves.server.potion.IrradiatedEffect;
import com.min01.acc.entity.ACCEntities;
import com.min01.acc.entity.AbstractOwnableEntity;
import com.min01.acc.misc.ACCEntityDataSerializers;
import com.min01.acc.util.ACCUtil;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public class EntityRadrifleBeam extends AbstractOwnableEntity<LivingEntity>
{
	public static final EntityDataAccessor<Boolean> IS_GAMMA = SynchedEntityData.defineId(EntityRadrifleBeam.class, EntityDataSerializers.BOOLEAN);
	public static final EntityDataAccessor<Boolean> IS_END = SynchedEntityData.defineId(EntityRadrifleBeam.class, EntityDataSerializers.BOOLEAN);
	public static final EntityDataAccessor<Boolean> IS_RICOCHET = SynchedEntityData.defineId(EntityRadrifleBeam.class, EntityDataSerializers.BOOLEAN);
	public static final EntityDataAccessor<Boolean> IS_OVERCHARGE = SynchedEntityData.defineId(EntityRadrifleBeam.class, EntityDataSerializers.BOOLEAN);
	public static final EntityDataAccessor<Integer> BOUNCE = SynchedEntityData.defineId(EntityRadrifleBeam.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<Direction> END_DIR = SynchedEntityData.defineId(EntityRadrifleBeam.class, EntityDataSerializers.DIRECTION);
	public static final EntityDataAccessor<Vec3> END_POS = SynchedEntityData.defineId(EntityRadrifleBeam.class, ACCEntityDataSerializers.VEC3.get());
	
	public EntityRadrifleBeam(EntityType<?> pEntityType, Level pLevel)
	{
		super(pEntityType, pLevel);
		this.noCulling = true;
	}
	
	@Override
	protected void defineSynchedData() 
	{
		super.defineSynchedData();
		this.entityData.define(IS_GAMMA, false);
		this.entityData.define(IS_END, false);
		this.entityData.define(IS_RICOCHET, false);
		this.entityData.define(IS_OVERCHARGE, false);
		this.entityData.define(BOUNCE, 0);
		this.entityData.define(END_DIR, Direction.DOWN);
		this.entityData.define(END_POS, Vec3.ZERO);
	}
	
	@Override
	public boolean displayFireAnimation()
	{
		return false;
	}
	
	@Override
	public void tick() 
	{
		super.tick();
		float tick = this.tickCount * 0.08F;
		float alpha = Math.max(1.0F - tick, 0.0F);
		if(this.tickCount == 1 && this.getOwner() != null && !this.isEnd())
		{
			if(!this.isRicochet() && !this.isOvercharge())
			{
	        	EntityRadrifleBeam beam = new EntityRadrifleBeam(ACCEntities.RADRIFLE_BEAM.get(), this.level);
	        	beam.setOwner(this.getOwner());
	        	beam.setPos(this.getEndPos());
	        	beam.setGamma(this.isGamma());
	        	beam.setEnd(true);
	        	beam.setEndDir(this.getEndDir());
	        	this.level.addFreshEntity(beam);
			}
        	
    		List<LivingEntity> arrayList = new ArrayList<>();
            Vec3 vec3 = this.position();
            Vec3 vec31 = this.getEndPos().subtract(vec3);
            Vec3 vec32 = vec31.normalize();
            
            for(int i = 1; i < Mth.floor(vec31.length()); ++i)
            {
            	Vec3 vec33 = vec3.add(vec32.scale(i));
            	List<LivingEntity> list = this.level.getEntitiesOfClass(LivingEntity.class, new AABB(vec33, vec33).inflate(this.isOvercharge() ? 0.5F : 0.25F), t -> t != this.getOwner() && !t.isAlliedTo(this.getOwner()) && !t.getType().is(ACTagRegistry.RESISTS_RADIATION));
            	if(!arrayList.containsAll(list))
            	{
            		arrayList.addAll(list);
            	}
            }
            
        	arrayList.forEach(t -> 
        	{
                boolean gamma = this.isGamma();
                boolean overcharge = this.isOvercharge();
                int radiationLevel = gamma ? IrradiatedEffect.BLUE_LEVEL : 0;
                if(t.hurt(ACDamageTypes.causeRaygunDamage(this.level.registryAccess(), this.getOwner()), gamma ? 10.5F : overcharge ? this.random.nextInt(100, 250) : 10.0F)) 
                {
                    if(t.addEffect(new MobEffectInstance(ACEffectRegistry.IRRADIATED.get(), 800, radiationLevel)))
                    {
                    	if(!this.level.isClientSide)
                    	{
                            AlexsCaves.sendMSGToAll(new UpdateEffectVisualityEntityMessage(t.getId(), this.getOwner().getId(), gamma ? 4 : 0, 800));
                    	}
                    }
                }
        	});
		}
		if(alpha <= 0.0F)
		{
			this.discard();
		}
		else if(this.isOvercharge() && this.tickCount % 2 == 0 && !this.level.isClientSide)
		{
			Vec3 pos = this.getEndPos();
			this.level.explode(this, pos.x, pos.y, pos.z, 5.0F, Level.ExplosionInteraction.BLOCK);
			Vec2 rotation = ACCUtil.lookAt(this.position(), pos);
        	Vec3 endPos = ACCUtil.getLookPos(rotation, pos, 0, 0, 20.0F);
        	HitResult hitResult = this.level.clip(new ClipContext(pos, endPos, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
			this.setEndPos(hitResult.getLocation());
		}
	}
	
	public void onHit(HitResult hitResult) 
	{
		if(this.isRicochet())
		{
		    int bounce = this.getBounce();
		    double x = this.getMovement().x;
		    double y = this.getMovement().y;
		    double z = this.getMovement().z;

		    Direction direction = Direction.DOWN;
		    
			if(hitResult instanceof BlockHitResult blockHitResult)
			{
				direction = blockHitResult.getDirection();
			}
			else if(hitResult instanceof EntityHitResult entityHitResult)
			{
				Vec3 pos = entityHitResult.getLocation();
				direction = Direction.getNearest(pos.x, pos.y, pos.z);
			}
			
			if(direction == Direction.EAST) 
			{
				x = -x;
			}
			else if(direction == Direction.SOUTH) 
			{
				z = -z;
			}
			else if(direction == Direction.WEST) 
			{
				x = -x;
			}
			else if(direction == Direction.NORTH)
			{
				z = -z;
			}
			else if(direction == Direction.UP)
			{
				y = -y;
			}
			else if(direction == Direction.DOWN)
			{
				y = -y;
			}
			
			if(bounce < 10 && hitResult.getType() != Type.MISS)
			{
				EntityRadrifleBeam segment = new EntityRadrifleBeam(ACCEntities.RADRIFLE_BEAM.get(), this.level);
				if(this.getOwner() != null)
				{
					segment.setOwner(this.getOwner());
				}
				Vec3 motion = new Vec3(x, y, z);
				segment.setPos(this.getEndPos());
            	HitResult blockHit = segment.level.clip(new ClipContext(segment.position(), segment.position().add(motion), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, segment));
				EntityHitResult entityHit = ProjectileUtil.getEntityHitResult(this.level, this, segment.position(), segment.position().add(motion), this.getBoundingBox().expandTowards(motion).inflate(1.0D), Entity::canBeHitByProjectile);
				if(entityHit != null)
				{
					blockHit = entityHit;
				}
				segment.setEndPos(blockHit.getLocation());
				segment.setBounce(bounce + 1);
				segment.setGamma(this.isGamma());
				segment.setRicochet(this.isRicochet());
				this.level.addFreshEntity(segment);
            	HitResult blockHit2 = segment.level.clip(new ClipContext(segment.position(), segment.getEndPos(), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, segment));
				EntityHitResult entityHit2 = ProjectileUtil.getEntityHitResult(this.level, this, segment.position(), segment.getEndPos(), this.getBoundingBox().expandTowards(motion).inflate(1.0D), Entity::canBeHitByProjectile);
				if(entityHit2 != null)
				{
					blockHit2 = entityHit2;
				}
				segment.onHit(blockHit2);
			}
			else if(!this.isEnd() && this.getOwner() != null)
			{
	        	EntityRadrifleBeam beam = new EntityRadrifleBeam(ACCEntities.RADRIFLE_BEAM.get(), this.level);
	        	beam.setOwner(this.getOwner());
	        	beam.setPos(this.getEndPos());
	        	beam.setGamma(this.isGamma());
	        	beam.setEnd(true);
	        	beam.setEndDir(direction);
	        	this.level.addFreshEntity(beam);
			}
		}
	}
	
	public Vec3 getMovement()
	{
		return ACCUtil.getVelocityTowards(this.position(), this.getEndPos(), 10.0F);
	}
	
	@Override
	public void addAdditionalSaveData(CompoundTag pCompound) 
	{
		super.addAdditionalSaveData(pCompound);
		pCompound.putBoolean("isGamma", this.isGamma());
		pCompound.putBoolean("isEnd", this.isEnd());
		pCompound.putBoolean("isRicochet", this.isRicochet());
		pCompound.putBoolean("isOvercharge", this.isOvercharge());
		pCompound.putInt("Bounce", this.getBounce());
		pCompound.putInt("EndDir", this.getEndDir().ordinal());
		this.writeVec3(this.getEndPos(), "EndPos", pCompound);
	}
	
	public void writeVec3(Vec3 vec3, String name, CompoundTag nbt)
	{
		CompoundTag tag = new CompoundTag();
		tag.putDouble("X", vec3.x);
		tag.putDouble("Y", vec3.y);
		tag.putDouble("Z", vec3.z);
		nbt.put(name, tag);
	}
	
	@Override
	public void readAdditionalSaveData(CompoundTag pCompound)
	{
		super.readAdditionalSaveData(pCompound);
		this.setGamma(pCompound.getBoolean("isGamma"));
		this.setEnd(pCompound.getBoolean("isEnd"));
		this.setRicochet(pCompound.getBoolean("isRicochet"));
		this.setOvercharge(pCompound.getBoolean("isOvercharge"));
		this.setBounce(pCompound.getInt("Bounce"));
		this.setEndDir(Direction.values()[pCompound.getInt("EndDir")]);
		this.setEndPos(this.readVec3(pCompound, "EndPos"));
	}
	
	public Vec3 readVec3(CompoundTag nbt, String name)
	{
		CompoundTag tag = nbt.getCompound(name);
		return new Vec3(tag.getDouble("X"), tag.getDouble("Y"), tag.getDouble("Z"));
	}
	
	public void setGamma(boolean value)
	{
		this.entityData.set(IS_GAMMA, value);
	}
	
	public boolean isGamma()
	{
		return this.entityData.get(IS_GAMMA);
	}
	
	public void setEnd(boolean value)
	{
		this.entityData.set(IS_END, value);
	}
	
	public boolean isEnd()
	{
		return this.entityData.get(IS_END);
	}
	
	public void setRicochet(boolean value)
	{
		this.entityData.set(IS_RICOCHET, value);
	}
	
	public boolean isRicochet()
	{
		return this.entityData.get(IS_RICOCHET);
	}
	
	public void setOvercharge(boolean value)
	{
		this.entityData.set(IS_OVERCHARGE, value);
	}
	
	public boolean isOvercharge()
	{
		return this.entityData.get(IS_OVERCHARGE);
	}
	
	public void setBounce(int value)
	{
		this.entityData.set(BOUNCE, value);
	}
	
	public int getBounce()
	{
		return this.entityData.get(BOUNCE);
	}
	
	public void setEndPos(Vec3 value)
	{
		this.entityData.set(END_POS, value);
	}
	
	public Vec3 getEndPos()
	{
		return this.entityData.get(END_POS);
	}
	
	public void setEndDir(Direction value)
	{
		this.entityData.set(END_DIR, value);
	}
	
	public Direction getEndDir()
	{
		return this.entityData.get(END_DIR);
	}
}
