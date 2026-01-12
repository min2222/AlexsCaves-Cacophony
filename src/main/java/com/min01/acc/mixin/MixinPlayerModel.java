package com.min01.acc.mixin;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.tuple.Pair;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.min01.acc.animation.IHierarchicalPlayerModel;
import com.min01.acc.animation.PlayerAnimation;
import com.min01.acc.capabilities.PlayerAnimationCapabilityImpl;
import com.min01.acc.misc.SmoothAnimationState;
import com.min01.acc.util.ACCClientUtil;
import com.min01.acc.util.ACCUtil;

import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.LivingEntity;

@Mixin(value = PlayerModel.class, priority = -15000)
public class MixinPlayerModel<T extends LivingEntity> implements IHierarchicalPlayerModel<T>
{
	private Map<String, Pair<ModelPart, ModelPart>> modelMap = new HashMap<>();
	
    @Inject(at = @At("HEAD"), method = "setupAnim", cancellable = true)
    private void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci)
    {
    	this.setupMap();
    }
    
    @Inject(at = @At("TAIL"), method = "setupAnim", cancellable = true)
    private void setupAnimTail(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci)
    {
    	PlayerAnimationCapabilityImpl cap = ACCUtil.getPlayerAnimationCapability(entity);
    	this.animate(entity, cap.raybladeDrawRightAnimationState, PlayerAnimation.RaybladeAnimation.RAYBLADE_DRAW_RIGHT, ageInTicks);
    	this.animate(entity, cap.raybladeSwingRightAnimationState, PlayerAnimation.RaybladeAnimation.RAYBLADE_SWING_RIGHT, ageInTicks);
    	this.animate(entity, cap.raybladeDrawLeftAnimationState, PlayerAnimation.RaybladeAnimation.RAYBLADE_DRAW_LEFT, ageInTicks);
    	this.animate(entity, cap.raybladeSwingLeftAnimationState, PlayerAnimation.RaybladeAnimation.RAYBLADE_SWING_LEFT, ageInTicks);
    }
    
    @Override
    public void setupAnimFirstPerson(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) 
    {
    	this.setupMap();
    	PlayerAnimationCapabilityImpl cap = ACCUtil.getPlayerAnimationCapability(entity);
    	this.animate(entity, cap.radrifleFireAnimationState, PlayerAnimation.RadrifleAnimation.RADRIFLE_FIRE, ageInTicks);
    	this.animate(entity, cap.radrifleHoldAnimationState, PlayerAnimation.RadrifleAnimation.RADRIFLE_HOLD, ageInTicks);
    	this.animate(entity, cap.radrifleHoldNearWallAnimationState, PlayerAnimation.RadrifleAnimation.RADRIFLE_HOLD_NEAR_WALL, ageInTicks);
    	this.animate(entity, cap.radrifleRunningAnimationState, PlayerAnimation.RadrifleAnimation.RADRIFLE_RUNNING, ageInTicks);
    	this.animate(entity, cap.radrifleOverchargeFireAnimationState, PlayerAnimation.RadrifleAnimation.RADRIFLE_OVERCHARGE_FIRE, ageInTicks);
    	this.animate(entity, cap.radrifleOverheatAnimationState, PlayerAnimation.RadrifleAnimation.RADRIFLE_OVERHEAT, ageInTicks);
    	
    	this.animate(entity, cap.raybladeDrawRightAnimationState, PlayerAnimation.RaybladeAnimation.RAYBLADE_DRAW_RIGHT, ageInTicks);
    	this.animate(entity, cap.raybladeSwingRightAnimationState, PlayerAnimation.RaybladeAnimation.RAYBLADE_SWING_RIGHT, ageInTicks);
    	this.animate(entity, cap.raybladeDrawLeftAnimationState, PlayerAnimation.RaybladeAnimation.RAYBLADE_DRAW_LEFT, ageInTicks);
    	this.animate(entity, cap.raybladeSwingLeftAnimationState, PlayerAnimation.RaybladeAnimation.RAYBLADE_SWING_LEFT, ageInTicks);
    	
    	this.animate(entity, cap.railgunFireAnimationState, PlayerAnimation.RailgunAnimation.RAILGUN_FIRE, ageInTicks);
    	this.animate(entity, cap.railgunHoldAnimationState, PlayerAnimation.RailgunAnimation.RAILGUN_HOLD, ageInTicks);
    	this.animate(entity, cap.railgunHoldNearWallAnimationState, PlayerAnimation.RailgunAnimation.RAILGUN_HOLD_NEAR_WALL, ageInTicks);
    	this.animate(entity, cap.railgunRunningAnimationState, PlayerAnimation.RailgunAnimation.RAILGUN_RUNNING, ageInTicks);
    	this.animate(entity, cap.railgunChargeAnimationState, PlayerAnimation.RailgunAnimation.RAILGUN_CHARGE, ageInTicks);
    	this.animate(entity, cap.railgunReloadAnimationState, PlayerAnimation.RailgunAnimation.RAILGUN_RELOAD, ageInTicks);
    }
    
	@Override
	public ModelPart root() 
	{
		return ACCClientUtil.MC.getEntityModels().bakeLayer(ModelLayers.PLAYER);
	}

	@Override
	public Optional<Pair<ModelPart, ModelPart>> getAnyDescendantWithName(String name) 
	{
		return this.root().getAllParts().findFirst().map(t ->
		{
			return this.modelMap.get(name);
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public void animate(T entity, SmoothAnimationState state, AnimationDefinition definition, float ageInTicks)
	{
		state.animatePlayer(PlayerModel.class.cast(this), definition, ageInTicks);
	}
	
	public void setupMap()
	{
    	if(this.modelMap.isEmpty())
    	{
    		this.modelMap.put("Head", Pair.of(PlayerModel.class.cast(this).head, PlayerModel.class.cast(this).hat));
    		this.modelMap.put("Body", Pair.of(PlayerModel.class.cast(this).body, PlayerModel.class.cast(this).jacket));
    		this.modelMap.put("LeftArm", Pair.of(PlayerModel.class.cast(this).leftArm, PlayerModel.class.cast(this).leftSleeve));
    		this.modelMap.put("RightArm", Pair.of(PlayerModel.class.cast(this).rightArm, PlayerModel.class.cast(this).rightSleeve));
    		this.modelMap.put("LeftLeg", Pair.of(PlayerModel.class.cast(this).leftLeg, PlayerModel.class.cast(this).leftPants));
    		this.modelMap.put("RightLeg", Pair.of(PlayerModel.class.cast(this).rightLeg, PlayerModel.class.cast(this).rightPants));
    	}
    	
    	this.modelMap.values().forEach(t ->
    	{
    		t.getLeft().resetPose();
    		t.getRight().resetPose();
    	});
	}
}
