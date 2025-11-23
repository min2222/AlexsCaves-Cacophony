package com.min01.acc.entity.projectile;

import java.util.ArrayList;
import java.util.List;

import com.min01.acc.entity.AbstractOwnableEntity;
import com.min01.acc.util.ACCUtil;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class EntityMagneticRailgunBeam extends AbstractOwnableEntity<LivingEntity>
{
	public static final EntityDataAccessor<Float> BEAM_DAMAGE = SynchedEntityData.defineId(EntityMagneticRailgunBeam.class, EntityDataSerializers.FLOAT);
	
	public EntityMagneticRailgunBeam(EntityType<?> p_19870_, Level p_19871_)
	{
		super(p_19870_, p_19871_);
	}
	
	@Override
	protected void defineSynchedData() 
	{
		super.defineSynchedData();
		this.entityData.define(BEAM_DAMAGE, 0.0F);
	}
	
	@Override
	public void tick() 
	{
		super.tick();
		
		if(this.tickCount >= 5)
		{
			this.discard();
		}
		else if(this.getOwner() != null)
		{
			List<LivingEntity> arrayList = new ArrayList<>();
	    	Vec3 startPos = this.position().add(0.0F, 0.5F, 0.0F);
			Vec3 lookPos = ACCUtil.getLookPos(this.getRotationVector(), startPos, 0.0F, 0.0F, 30.0F);
			HitResult hitResult = this.level.clip(new ClipContext(startPos, lookPos, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
	    	Vec3 hitPos = hitResult.getLocation();
	        Vec3 targetPos = hitPos.subtract(startPos);
	        Vec3 normalizedPos = targetPos.normalize();
	        for(int i = 1; i < Mth.floor(targetPos.length()); ++i)
	        {
	        	Vec3 pos = startPos.add(normalizedPos.scale(i));
	        	List<LivingEntity> list = this.level.getEntitiesOfClass(LivingEntity.class, new AABB(pos, pos).inflate(0.5F), t -> t != this.getOwner() && !t.isAlliedTo(this.getOwner()));
	        	if(!arrayList.containsAll(list))
	        	{
	        		arrayList.addAll(list);
	        	}
	        }
	        arrayList.forEach(t -> 
	        {
	        	//TODO custom damage source;
	        	if(t.hurt(this.damageSources().mobProjectile(this, this.getOwner()), this.getBeamDamage()))
	        	{
	        		t.addDeltaMovement(ACCUtil.fromToVector(t.position(), hitPos, 1.5F));
	        		if(t instanceof ServerPlayer player)
	        		{
		    			player.connection.send(new ClientboundSetEntityMotionPacket(t));
	        		}
	        	}
	        });
		}
	}
	
	@Override
	public boolean displayFireAnimation() 
	{
		return false;
	}
	
	@Override
	public void addAdditionalSaveData(CompoundTag p_37265_) 
	{
		super.addAdditionalSaveData(p_37265_);
		p_37265_.putFloat("BeamDamage", this.getBeamDamage());
	}
	
	@Override
	public void readAdditionalSaveData(CompoundTag p_37262_)
	{
		super.readAdditionalSaveData(p_37262_);
		this.setBeamDamage(p_37262_.getFloat("BeamDamage"));
	}
	
	public float getBeamDamage()
	{
		return this.entityData.get(BEAM_DAMAGE);
	}
	
	public void setBeamDamage(float damage)
	{
		this.entityData.set(BEAM_DAMAGE, damage);
	}
}
