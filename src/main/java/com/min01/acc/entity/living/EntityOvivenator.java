package com.min01.acc.entity.living;

import java.util.List;

import com.github.alexmodguy.alexscaves.client.particle.ACParticleRegistry;
import com.github.alexmodguy.alexscaves.server.block.MultipleDinosaurEggsBlock;
import com.github.alexmodguy.alexscaves.server.entity.ai.AnimalBreedEggsGoal;
import com.github.alexmodguy.alexscaves.server.entity.ai.AnimalFollowOwnerGoal;
import com.github.alexmodguy.alexscaves.server.entity.ai.AnimalLayEggGoal;
import com.github.alexmodguy.alexscaves.server.entity.living.DinosaurEntity;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.min01.acc.block.ACCBlocks;
import com.min01.acc.entity.ACCEntities;
import com.min01.acc.entity.AbstractAnimatableDinosaur;
import com.min01.acc.entity.ai.goal.OvivenatorEatEggGoal;
import com.min01.acc.entity.ai.goal.OvivenatorStealEggGoal;
import com.min01.acc.entity.ai.goal.OvivenatorTickBoostGoal;
import com.min01.acc.misc.ACCTags;
import com.min01.acc.misc.SmoothAnimationState;
import com.min01.acc.util.ACCUtil;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
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
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public class EntityOvivenator extends AbstractAnimatableDinosaur
{
	public static final EntityDataAccessor<Boolean> IS_PANIC = SynchedEntityData.defineId(EntityOvivenator.class, EntityDataSerializers.BOOLEAN);
	public static final EntityDataAccessor<Boolean> HOLDING_EGG = SynchedEntityData.defineId(EntityOvivenator.class, EntityDataSerializers.BOOLEAN);
	public static final EntityDataAccessor<BlockPos> EGG_POS = SynchedEntityData.defineId(EntityOvivenator.class, EntityDataSerializers.BLOCK_POS);
	public static final EntityDataAccessor<ItemStack> CURRENT_EGG = SynchedEntityData.defineId(EntityOvivenator.class, EntityDataSerializers.ITEM_STACK);
	public static final EntityDataAccessor<Integer> EGG_HOLDING_TICK = SynchedEntityData.defineId(EntityOvivenator.class, EntityDataSerializers.INT);
	
	public final SmoothAnimationState idleAnimationState = new SmoothAnimationState();
	public final SmoothAnimationState idleWithEggAnimationState = new SmoothAnimationState();
	public final SmoothAnimationState holdAnimationState = new SmoothAnimationState();
	public final SmoothAnimationState noiseAnimationState = new SmoothAnimationState();
	public final SmoothAnimationState scratchRightAnimationState = new SmoothAnimationState();
	public final SmoothAnimationState scratchLeftAnimationState = new SmoothAnimationState();
	public final SmoothAnimationState lookAnimationState = new SmoothAnimationState();
	public final SmoothAnimationState danceAnimationState = new SmoothAnimationState();
	public final SmoothAnimationState pickupAnimationState = new SmoothAnimationState();
	public final SmoothAnimationState eatAnimationState = new SmoothAnimationState();
	public final SmoothAnimationState sitAnimationState = new SmoothAnimationState(1.0F);
	
	public final SmoothAnimationState runAnimationState = new SmoothAnimationState();
	
	public AmbientType ambientType;
	public int ambientTick;
	
	public EntityOvivenator(EntityType<? extends DinosaurEntity> p_33002_, Level p_33003_)
	{
		super(p_33002_, p_33003_);
	}
	
    public static AttributeSupplier.Builder createAttributes()
    {
        return Monster.createMonsterAttributes()
    			.add(Attributes.MAX_HEALTH, 18.0F)
    			.add(Attributes.MOVEMENT_SPEED, 0.2F);
    }
	
	@Override
	protected void registerGoals() 
	{
		super.registerGoals();
        this.goalSelector.addGoal(1, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(2, new AnimalFollowOwnerGoal(this, 1.0D, 5.0F, 2.0F, false)
        {
            @Override
            public boolean shouldFollow() 
            {
                return EntityOvivenator.this.getCommand() == 2;
            }
        });
        this.goalSelector.addGoal(3, new AnimalBreedEggsGoal(this, 1));
        this.goalSelector.addGoal(4, new AnimalLayEggGoal(this, 100, 1));
        this.goalSelector.addGoal(0, new OvivenatorStealEggGoal(this));
        this.goalSelector.addGoal(0, new OvivenatorEatEggGoal(this));
        this.goalSelector.addGoal(0, new OvivenatorTickBoostGoal(this));
	}
	
    @Override
    protected void defineSynchedData() 
    {
    	super.defineSynchedData();
    	this.entityData.define(IS_PANIC, false);
    	this.entityData.define(HOLDING_EGG, false);
    	this.entityData.define(EGG_POS, BlockPos.ZERO);
    	this.entityData.define(CURRENT_EGG, ItemStack.EMPTY);
    	this.entityData.define(EGG_HOLDING_TICK, 0);
    }
    
	@Override
	public void handleEntityEvent(byte p_21375_) 
	{
		super.handleEntityEvent(p_21375_);
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
		if(p_21375_ == 99 && !this.getCurrentEgg().isEmpty()) 
		{
			Vec3 lookPos = ACCUtil.getLookPos(new Vec2(this.getXRot(), this.yBodyRot), this.position(), 0.0F, 1.5F, 1.5F);
            for(int i = 0; i < 15 + this.random.nextInt(5); i++) 
            {
    			this.level.addParticle(new ItemParticleOption(ParticleTypes.ITEM, this.getCurrentEgg()), lookPos.x, lookPos.y, lookPos.z, this.random.nextGaussian() * 0.03F, 0.0F, this.random.nextGaussian() * 0.03F);
            }
		}
	}

	@Override
	public BlockState createEggBlockState() 
	{
		return ACCBlocks.OVIVENATOR_EGG.get().defaultBlockState().setValue(MultipleDinosaurEggsBlock.EGGS, 1 + this.random.nextInt(2));
	}

	@Override
	public AgeableMob getBreedOffspring(ServerLevel p_146743_, AgeableMob p_146744_) 
	{
		return ACCEntities.OVIVENATOR.get().create(p_146743_);
	}
	
	@Override
	public void tick() 
	{
		super.tick();
		if(this.level.isClientSide)
		{
			this.idleAnimationState.updateWhen(!this.isHoldingEgg() && !this.isDancing() && !this.isOrderedToSit() && this.getAnimationState() == 0, this.tickCount);
			this.idleWithEggAnimationState.updateWhen(this.isHoldingEgg() && !this.isDancing() && !this.isOrderedToSit() && this.getAnimationState() == 0, this.tickCount);
			this.holdAnimationState.updateWhen(this.isHoldingEgg() && !this.isDancing(), this.tickCount);
			this.noiseAnimationState.updateWhen(this.ambientType == AmbientType.NOISE && !this.isDancing() && !this.isOrderedToSit(), this.tickCount);
			this.scratchRightAnimationState.updateWhen(this.ambientType == AmbientType.SCRATCH_RIGHT && !this.isDancing() && !this.isHoldingEgg() && !this.isOrderedToSit(), this.tickCount);
			this.scratchLeftAnimationState.updateWhen(this.ambientType == AmbientType.SCRATCH_LEFT && !this.isDancing() && !this.isHoldingEgg() && !this.isOrderedToSit(), this.tickCount);
			this.lookAnimationState.updateWhen(this.ambientType == AmbientType.LOOK && !this.isDancing(), this.tickCount);
			this.danceAnimationState.updateWhen(this.isDancing(), this.tickCount);
			this.pickupAnimationState.updateWhen(this.isUsingSkill(1) && !this.isDancing(), this.tickCount);
			this.eatAnimationState.updateWhen(this.isUsingSkill(2) && !this.isDancing(), this.tickCount);
			this.sitAnimationState.updateWhen(this.isOrderedToSit() && !this.isDancing(), this.tickCount);
			this.runAnimationState.updateWhen(this.isPanic() && this.getAnimationState() == 0, this.tickCount);
		}
		if(this.ambientType != null)
		{
			this.ambientTick--;
			if(this.ambientTick <= 0)
			{
				this.ambientType = null;
			}
		}
		if(this.getNavigation().isDone())
		{
			if(this.isHoldingEgg())
			{
				List<DinosaurEntity> list = this.level.getEntitiesOfClass(DinosaurEntity.class, this.getBoundingBox().inflate(3.0F), t -> !(t instanceof EntityOvivenator));
				if(!list.isEmpty())
				{
					ACCUtil.runAway(this, list.get(0).position());
					this.setPanic(true);
				}
				else if(this.isPanic())
				{
					this.setPanic(false);
				}
			}
			else if(this.isPanic())
			{
				this.setPanic(false);
			}
		}
		if(!this.getEggPos().equals(BlockPos.ZERO))
		{
			if(this.getEggPos().distToCenterSqr(this.position()) <= 1.5F && this.getCurrentEgg().isEmpty())
			{
				BlockState state = this.level.getBlockState(this.getEggPos());
				this.setAnimationState(1);
				this.setAnimationTick(18);
				if(!state.isAir())
				{
					this.level.destroyBlock(this.getEggPos(), false);
					this.setCurrentEgg(new ItemStack(Item.BY_BLOCK.get(state.getBlock())));
				}
			}
		}
		if(this.getAnimationTick() <= 0)
		{
			if(this.getAnimationState() == 1)
			{
				this.setHoldingEgg(true);
				this.setAnimationState(0);
			}
		}
		if(this.isHoldingEgg())
		{
			this.setEggHoldingTick(this.getEggHoldingTick() + 1);
		}
	}
    
    @Override
    public boolean hurt(DamageSource p_27567_, float p_27568_) 
    {
    	if(!this.isPanic() && p_27567_.getDirectEntity() != null && !this.isTame())
    	{
    		List<EntityOvivenator> list = this.level.getEntitiesOfClass(EntityOvivenator.class, this.getBoundingBox().inflate(10), t -> !t.isPanic() && !t.isTame());
    		list.forEach(t -> 
    		{
    			t.setPanic(true);
        		ACCUtil.runAway(t, p_27567_.getSourcePosition());
    		});
    		this.setPanic(true);
    		ACCUtil.runAway(this, p_27567_.getSourcePosition());
    	}
    	return super.hurt(p_27567_, p_27568_);
    }
	
    public int getAltSkinForItem(ItemStack stack) 
    {
    	if(stack.is(ACItemRegistry.AMBER_CURIOSITY.get()))
        {
            return 1;
        } 
        else if(stack.is(ACItemRegistry.TECTONIC_SHARD.get())) 
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
            if(itemstack.is(ACCTags.ACCItems.OVIVENATOR_TAMEABLE) && !this.isTame())
            {
                if(!player.getAbilities().instabuild)
                {
                	itemstack.shrink(1);
                }
                if(this.random.nextInt(3) == 0 && !net.minecraftforge.event.ForgeEventFactory.onAnimalTame(this, player))
            	{
                    this.heal(5);
                    this.tame(player);
                    this.navigation.stop();
                    this.setCommand(0);
                    this.setOrderedToSit(true);
                    this.level.broadcastEntityEvent(this, (byte) 7);
            	}
                else
                {
                    this.level.broadcastEntityEvent(this, (byte)6);
                }
                return InteractionResult.SUCCESS;
            }
        }
        return type;
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
	
	@Override
	public boolean tamesFromHatching() 
	{
		return true;
	}
	
	@Override
	public boolean canOwnerCommand(Player ownerPlayer) 
	{
		return true;
	}
	
	@Override
	public void readAdditionalSaveData(CompoundTag p_21450_)
	{
		super.readAdditionalSaveData(p_21450_);
		this.setPanic(p_21450_.getBoolean("isPanic"));
		this.setHoldingEgg(p_21450_.getBoolean("HoldingEgg"));
		this.setEggPos(NbtUtils.readBlockPos(p_21450_.getCompound("EggPos")));
		this.setEggHoldingTick(p_21450_.getInt("EggHoldingTick"));
		if(p_21450_.contains("CurrentEgg"))
		{
			this.setCurrentEgg(ItemStack.of(p_21450_.getCompound("CurrentEgg")));
		}
	}

	@Override
	public void addAdditionalSaveData(CompoundTag p_21484_)
	{
		super.addAdditionalSaveData(p_21484_);
		p_21484_.putBoolean("isPanic", this.isPanic());
		p_21484_.putBoolean("HoldingEgg", this.isHoldingEgg());
		p_21484_.put("EggPos", NbtUtils.writeBlockPos(this.getEggPos()));
		p_21484_.putInt("EggHoldingTick", this.getEggHoldingTick());
		if(!this.getCurrentEgg().isEmpty())
		{
			p_21484_.put("CurrentEgg", this.getCurrentEgg().save(new CompoundTag()));
		}
	}
	
	public void setEggHoldingTick(int value)
	{
		this.entityData.set(EGG_HOLDING_TICK, value);
	}
	
	public int getEggHoldingTick()
	{
		return this.entityData.get(EGG_HOLDING_TICK);
	}
	
	public void setCurrentEgg(ItemStack stack)
	{
		this.entityData.set(CURRENT_EGG, stack);
	}
	
	public ItemStack getCurrentEgg()
	{
		return this.entityData.get(CURRENT_EGG);
	}
	
	public void setEggPos(BlockPos value)
	{
		this.entityData.set(EGG_POS, value);
	}
	
	public BlockPos getEggPos()
	{
		return this.entityData.get(EGG_POS);
	}
	
	public void setPanic(boolean value)
	{
		this.entityData.set(IS_PANIC, value);
	}
	
	public boolean isPanic()
	{
		return this.entityData.get(IS_PANIC);
	}
	
	public void setHoldingEgg(boolean value)
	{
		this.entityData.set(HOLDING_EGG, value);
	}
	
	public boolean isHoldingEgg()
	{
		return this.entityData.get(HOLDING_EGG);
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