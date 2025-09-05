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
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class EntityRadrifleBeam extends AbstractOwnableEntity<LivingEntity>
{
	public static final EntityDataAccessor<Boolean> IS_GAMMA = SynchedEntityData.defineId(EntityRadrifleBeam.class, EntityDataSerializers.BOOLEAN);
	public static final EntityDataAccessor<Boolean> IS_END = SynchedEntityData.defineId(EntityRadrifleBeam.class, EntityDataSerializers.BOOLEAN);
	public static final EntityDataAccessor<Boolean> IS_RECOCHET = SynchedEntityData.defineId(EntityRadrifleBeam.class, EntityDataSerializers.BOOLEAN);
	public static final EntityDataAccessor<Integer> BOUNCE = SynchedEntityData.defineId(EntityRadrifleBeam.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<Direction> END_DIR = SynchedEntityData.defineId(EntityRadrifleBeam.class, EntityDataSerializers.DIRECTION);
	public static final EntityDataAccessor<Vec3> END_POS = SynchedEntityData.defineId(EntityRadrifleBeam.class, ACCEntityDataSerializers.VEC3.get());
	
	public EntityRadrifleBeam(EntityType<?> p_19870_, Level p_19871_)
	{
		super(p_19870_, p_19871_);
		this.noCulling = true;
	}
	
	@Override
	protected void defineSynchedData() 
	{
		super.defineSynchedData();
		this.entityData.define(IS_GAMMA, false);
		this.entityData.define(IS_END, false);
		this.entityData.define(IS_RECOCHET, false);
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
		if(this.tickCount == 1 && this.getOwner() != null && !this.isEnd())
		{
			if(!this.isRecochet())
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
            
            for(int i = 1; i < Mth.floor(vec31.length()) + this.position().distanceTo(this.getEndPos()); ++i)
            {
            	Vec3 vec33 = vec3.add(vec32.scale(i));
            	List<LivingEntity> list = this.level.getEntitiesOfClass(LivingEntity.class, new AABB(vec33, vec33).inflate(0.1F), t -> t != this.getOwner() && !t.isAlliedTo(this.getOwner()) && !t.getType().is(ACTagRegistry.RESISTS_RADIATION));
            	list.forEach(t -> 
            	{
            		if(!arrayList.contains(t))
            		{
            			arrayList.add(t);
            		}
            	});
            }
            
        	arrayList.forEach(t -> 
        	{
                boolean gamma = this.isGamma();
                int radiationLevel = gamma ? IrradiatedEffect.BLUE_LEVEL : 0;
                if(t.hurt(ACDamageTypes.causeRaygunDamage(this.level.registryAccess(), this.getOwner()), gamma ? 10.5F : 10.0F)) 
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
		if(this.tickCount >= 20)
		{
			this.discard();
		}
	}
	
	public void onHitBlock(BlockHitResult pResult) 
	{
		if(this.isRecochet())
		{
		    int bounce = this.getBounce();
		    double x = this.getMovement().x;
		    double y = this.getMovement().y;
		    double z = this.getMovement().z;

			Direction direction = pResult.getDirection();
			
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
			
			if(bounce < 10)
			{
				EntityRadrifleBeam segment = new EntityRadrifleBeam(ACCEntities.RADRIFLE_BEAM.get(), this.level);
				if(this.getOwner() != null)
				{
					segment.setOwner(this.getOwner());
				}
				Vec3 motion = new Vec3(x, y, z);
				segment.setPos(this.getEndPos());
            	BlockHitResult hitResult = segment.level.clip(new ClipContext(segment.position(), segment.position().add(motion), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, segment));
				segment.setEndPos(hitResult.getLocation());
				segment.setEndPos(hitResult.getLocation());
				segment.setBounce(bounce + 1);
				segment.setGamma(this.isGamma());
				segment.setRecochet(this.isRecochet());
				this.level.addFreshEntity(segment);
            	BlockHitResult hitResult2 = segment.level.clip(new ClipContext(segment.position(), segment.getEndPos(), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, segment));
	    		segment.onHitBlock(hitResult2);
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
		return ACCUtil.fromToVector(this.position(), this.getEndPos(), 20.0F);
	}
	
	@Override
	public void addAdditionalSaveData(CompoundTag p_37265_) 
	{
		super.addAdditionalSaveData(p_37265_);
		p_37265_.putBoolean("isGamma", this.isGamma());
		p_37265_.putBoolean("isEnd", this.isEnd());
		p_37265_.putBoolean("isRecochet", this.isRecochet());
		p_37265_.putInt("Bounce", this.getBounce());
		p_37265_.putInt("EndDir", this.getEndDir().ordinal());
		this.writeVec3(this.getEndPos(), "EndPos", p_37265_);
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
	public void readAdditionalSaveData(CompoundTag p_37262_)
	{
		super.readAdditionalSaveData(p_37262_);
		this.setGamma(p_37262_.getBoolean("isGamma"));
		this.setEnd(p_37262_.getBoolean("isEnd"));
		this.setRecochet(p_37262_.getBoolean("isRecochet"));
		this.setBounce(p_37262_.getInt("Bounce"));
		this.setEndDir(Direction.values()[p_37262_.getInt("EndDir")]);
		this.setEndPos(this.readVec3(p_37262_, "EndPos"));
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
	
	public void setRecochet(boolean value)
	{
		this.entityData.set(IS_RECOCHET, value);
	}
	
	public boolean isRecochet()
	{
		return this.entityData.get(IS_RECOCHET);
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
