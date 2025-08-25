package com.min01.acc.entity.living;

import com.github.alexmodguy.alexscaves.client.particle.ACParticleRegistry;
import com.github.alexmodguy.alexscaves.server.entity.ai.AnimalFollowOwnerGoal;
import com.github.alexmodguy.alexscaves.server.entity.living.DinosaurEntity;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.min01.acc.entity.AbstractAnimatableDinosaur;
import com.min01.acc.util.ACCUtil;

import net.minecraft.Util;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class EntityOvivenator extends AbstractAnimatableDinosaur
{
	public static final EntityDataAccessor<Integer> ALT_SKIN = SynchedEntityData.defineId(EntityOvivenator.class, EntityDataSerializers.INT);
	
	public final AnimationState idleAnimationState = new AnimationState();
	public final AnimationState noiseAnimationState = new AnimationState();
	public final AnimationState scratchRightAnimationState = new AnimationState();
	public final AnimationState scratchLeftAnimationState = new AnimationState();
	public final AnimationState lookAnimationState = new AnimationState();
	
	public AmbientType ambientType;
	public int ambientTick;
	
	public EntityOvivenator(EntityType<? extends DinosaurEntity> p_33002_, Level p_33003_)
	{
		super(p_33002_, p_33003_);
	}
	
	@Override
	protected void registerGoals() 
	{
		this.goalSelector.addGoal(1, new FloatGoal(this));
		this.goalSelector.addGoal(8, new RandomStrollGoal(this, 1.0F));
		this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 3.0F, 1.0F));
		this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));
        this.goalSelector.addGoal(1, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(3, new AnimalFollowOwnerGoal(this, 0.7D, 5.0F, 2.0F, false) 
        {
            @Override
            public boolean shouldFollow() 
            {
                return EntityOvivenator.this.getCommand() == 2;
            }
        });
        //this.goalSelector.addGoal(4, new AnimalBreedEggsGoal(this, 0.7F));
        //this.goalSelector.addGoal(5, new AnimalLayEggGoal(this, 200, 0.7F));
	}
	
    @Override
    protected void defineSynchedData() 
    {
    	super.defineSynchedData();
    	this.entityData.define(ALT_SKIN, 0);
    }
    
	@Override
	public void handleEntityEvent(byte p_21375_) 
	{
		if(p_21375_ == 82 || p_21375_ == 83) 
		{
            ParticleOptions options;
            if(p_21375_ == 82)
            {
                options = ACParticleRegistry.DINOSAUR_TRANSFORMATION_AMBER.get();
            }
            else
            {
                options = ACParticleRegistry.DINOSAUR_TRANSFORMATION_TECTONIC.get();
            }
            
            for(int i = 0; i < 15 + this.random.nextInt(5); i++) 
            {
                this.level.addParticle(options, this.getX(), this.getY(0.5F), this.getZ(), 0, 0, 0);
            }
        }
	}
	
    public int getAltSkinForItem(ItemStack stack) 
    {
    	if(stack.is(ACItemRegistry.AMBER_CURIOSITY.get()))
        {
            return 1;
        } 
        else if (stack.is(ACItemRegistry.TECTONIC_SHARD.get())) 
        {
            return 2;
        } 
        return 0;
    }
    
    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) 
    {
        ItemStack itemstack = player.getItemInHand(hand);
        InteractionResult interactionresult = itemstack.interactLivingEntity(player, this, hand);
        InteractionResult type = super.mobInteract(player, hand);
        if(!interactionresult.consumesAction() && !type.consumesAction()) 
        {
            int altSkinFromItem = this.getAltSkinForItem(itemstack);
            if(altSkinFromItem > 0) 
            {
            	if(!player.getAbilities().instabuild)
            	{
            		itemstack.shrink(1);
            	}
                this.playSound(altSkinFromItem == 2 ? ACSoundRegistry.TECTONIC_SHARD_TRANSFORM.get() : ACSoundRegistry.AMBER_MONOLITH_SUMMON.get());
                if(!this.level.isClientSide) 
                {
                    if(altSkinFromItem == this.getAltSkin())
                    {
                        this.setAltSkin(0);
                    }
                    else
                    {
                        this.setAltSkin(altSkinFromItem);
                    }
                    this.level.broadcastEntityEvent(this, (byte) (altSkinFromItem == 2 ? 83 : 82));
                }
                return InteractionResult.SUCCESS;
            }
        }
        return type;
    }

	@Override
	public BlockState createEggBlockState() 
	{
		return null;
	}

	@Override
	public AgeableMob getBreedOffspring(ServerLevel p_146743_, AgeableMob p_146744_) 
	{
		return null;
	}
	
	@Override
	public void tick() 
	{
		super.tick();
		if(this.level.isClientSide)
		{
			this.idleAnimationState.animateWhen(!ACCUtil.isMoving(this), this.tickCount);
			this.noiseAnimationState.animateWhen(this.ambientType == AmbientType.NOISE, this.tickCount);
			this.scratchRightAnimationState.animateWhen(this.ambientType == AmbientType.SCRATCH_RIGHT, this.tickCount);
			this.scratchLeftAnimationState.animateWhen(this.ambientType == AmbientType.SCRATCH_LEFT, this.tickCount);
			this.lookAnimationState.animateWhen(this.ambientType == AmbientType.LOOK, this.tickCount);
		}
		if(this.ambientType != null)
		{
			this.ambientTick--;
			if(this.ambientTick <= 0)
			{
				this.ambientType = null;
			}
		}
	}
	
	@Override
	public void playAmbientSound() 
	{
		AmbientType type = AmbientType.getRandom(this.random);
		this.ambientType = type;
		this.ambientTick = type.tick;
		if(type == AmbientType.NOISE)
		{
			super.playAmbientSound();
		}
	}
	
	@Override
	protected SoundEvent getDeathSound() 
	{
		return ACSoundRegistry.VALLUMRAPTOR_DEATH.get();
	}
	
	@Override
	protected SoundEvent getHurtSound(DamageSource p_21239_)
	{
		return ACSoundRegistry.VALLUMRAPTOR_HURT.get();
	}
	
	@Override
	protected SoundEvent getAmbientSound() 
	{
		return ACSoundRegistry.VALLUMRAPTOR_CALL.get();
	}
	
	@Override
	public float getVoicePitch()
	{
		return 1.2F;
	}
	
	@Override
	protected void updateWalkAnimation(float p_268283_)
	{
		float f = Math.min(p_268283_ * 20.0F, 1.0F);
		this.walkAnimation.update(f, 0.4F);
	}
	
	public static enum AmbientType
	{
		NOISE(10),
		SCRATCH_RIGHT(20),
		SCRATCH_LEFT(20),
		LOOK(40);
		
		int tick;
		
		private AmbientType(int tick) 
		{
			this.tick = tick;
		}
		
		public static AmbientType getRandom(RandomSource random)
		{
			return Util.getRandom(values(), random);
		}
	}
}