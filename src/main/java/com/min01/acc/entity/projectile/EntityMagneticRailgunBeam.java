package com.min01.acc.entity.projectile;

import com.min01.acc.entity.AbstractOwnableEntity;
import com.min01.acc.misc.Laser;
import com.min01.acc.misc.Laser.LaserHitResult;
import com.min01.acc.util.ACCUtil;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class EntityMagneticRailgunBeam extends AbstractOwnableEntity<LivingEntity>
{
	public static final EntityDataAccessor<Float> BEAM_DAMAGE = SynchedEntityData.defineId(EntityMagneticRailgunBeam.class, EntityDataSerializers.FLOAT);
	
	public final Laser laser = new Laser();
	
	public EntityMagneticRailgunBeam(EntityType<?> pEntityType, Level pLevel)
	{
		super(pEntityType, pLevel);
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
	    	Vec3 startPos = this.position().add(0.0F, 0.5F, 0.0F);
			Vec3 lookPos = ACCUtil.getLookPos(this.getRotationVector(), startPos, 0.0F, 0.0F, 30.0F);
			LaserHitResult laserHit = this.laser.raytrace(this.level, this.position(), startPos, lookPos, 0.5F, this.getYRot(), this.getXRot(), t -> t != this.getOwner() && !t.isAlliedTo(this.getOwner()), false, this.getOwner());
			laserHit.entities.forEach(t -> 
	        {
	        	//TODO custom damage source;
	        	if(t.hurt(this.damageSources().mobProjectile(this, this.getOwner()), this.getBeamDamage()))
	        	{
	        		t.addDeltaMovement(ACCUtil.getVelocityTowards(t.position(), this.laser.collidePos, 1.5F));
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
	public void addAdditionalSaveData(CompoundTag pCompound) 
	{
		super.addAdditionalSaveData(pCompound);
		pCompound.putFloat("BeamDamage", this.getBeamDamage());
	}
	
	@Override
	public void readAdditionalSaveData(CompoundTag pCompound)
	{
		super.readAdditionalSaveData(pCompound);
		this.setBeamDamage(pCompound.getFloat("BeamDamage"));
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
