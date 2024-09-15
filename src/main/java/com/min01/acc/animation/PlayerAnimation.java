package com.min01.acc.animation;

import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;

public class PlayerAnimation
{
	public static class RaybladeAnimation
	{
		public static final AnimationDefinition RAYBLADE_DRAW_RIGHT = AnimationDefinition.Builder.withLength(0.5F)
				.addAnimation("RightArm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
					new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.5F, KeyframeAnimations.degreeVec(-110.0F, -10.0F, 85.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("RightArm", new AnimationChannel(AnimationChannel.Targets.POSITION, 
					new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.5F, KeyframeAnimations.posVec(-1.0F, -1.8069F, -1.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("RightArmSleeve", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
					new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.5F, KeyframeAnimations.degreeVec(-110.0F, -10.0F, 85.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("RightArmSleeve", new AnimationChannel(AnimationChannel.Targets.POSITION, 
					new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.5F, KeyframeAnimations.posVec(-1.0F, -1.7836F, -1.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.build();

		public static final AnimationDefinition RAYBLADE_HOLD_RIGHT = AnimationDefinition.Builder.withLength(0.5F).looping()
				.addAnimation("RightArm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
					new Keyframe(0.0F, KeyframeAnimations.degreeVec(-110.0F, -10.0F, 85.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("RightArm", new AnimationChannel(AnimationChannel.Targets.POSITION, 
					new Keyframe(0.0F, KeyframeAnimations.posVec(-1.0F, -2.0F, -1.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.5F, KeyframeAnimations.posVec(-1.0F, -2.1F, -1.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("RightArmSleeve", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
					new Keyframe(0.0F, KeyframeAnimations.degreeVec(-110.0F, -10.0F, 85.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("RightArmSleeve", new AnimationChannel(AnimationChannel.Targets.POSITION, 
					new Keyframe(0.0F, KeyframeAnimations.posVec(-1.0F, -2.0F, -1.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.5F, KeyframeAnimations.posVec(-1.0F, -2.1F, -1.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.build();

			public static final AnimationDefinition RAYBLADE_SWING_RIGHT = AnimationDefinition.Builder.withLength(0.75F)
				.addAnimation("RightArm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
					new Keyframe(0.25F, KeyframeAnimations.degreeVec(-110.0F, -10.0F, 85.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.375F, KeyframeAnimations.degreeVec(50.0F, -10.0F, 85.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.75F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("RightArm", new AnimationChannel(AnimationChannel.Targets.POSITION, 
					new Keyframe(0.0F, KeyframeAnimations.posVec(-1.0F, -1.748F, -1.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, -1.7277F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.375F, KeyframeAnimations.posVec(-2.0F, -1.8533F, 2.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("RightArmSleeve", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
					new Keyframe(0.25F, KeyframeAnimations.degreeVec(-110.0F, -10.0F, 85.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.375F, KeyframeAnimations.degreeVec(50.0F, -10.0F, 85.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.75F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("RightArmSleeve", new AnimationChannel(AnimationChannel.Targets.POSITION, 
					new Keyframe(0.0F, KeyframeAnimations.posVec(-1.0F, -1.8433F, -1.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, -1.8936F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.375F, KeyframeAnimations.posVec(-2.0F, -1.8506F, 2.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.build();

			public static final AnimationDefinition RAYBLADE_DRAW_LEFT = AnimationDefinition.Builder.withLength(0.5F)
				.addAnimation("LeftArm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
					new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.5F, KeyframeAnimations.degreeVec(-110.0F, 10.0F, -85.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("LeftArm", new AnimationChannel(AnimationChannel.Targets.POSITION, 
					new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.5F, KeyframeAnimations.posVec(1.0F, -1.8776F, -1.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("LeftArmSleeve", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
					new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.5F, KeyframeAnimations.degreeVec(-110.0F, 10.0F, -85.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("LeftArmSleeve", new AnimationChannel(AnimationChannel.Targets.POSITION, 
					new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.5F, KeyframeAnimations.posVec(1.0F, -1.7899F, -1.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.build();

			public static final AnimationDefinition RAYBLADE_HOLD_LEFT = AnimationDefinition.Builder.withLength(1.5F).looping()
				.addAnimation("LeftArm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
					new Keyframe(0.0F, KeyframeAnimations.degreeVec(-110.0F, 10.0F, -85.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("LeftArm", new AnimationChannel(AnimationChannel.Targets.POSITION, 
					new Keyframe(0.0F, KeyframeAnimations.posVec(1.0F, -1.7097F, -1.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("LeftArmSleeve", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
					new Keyframe(0.0F, KeyframeAnimations.degreeVec(-110.0F, 10.0F, -85.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("LeftArmSleeve", new AnimationChannel(AnimationChannel.Targets.POSITION, 
					new Keyframe(0.0F, KeyframeAnimations.posVec(1.0F, -1.8932F, -1.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.build();

			public static final AnimationDefinition RAYBLADE_SWING_LEFT = AnimationDefinition.Builder.withLength(0.75F)
				.addAnimation("LeftArm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
					new Keyframe(0.25F, KeyframeAnimations.degreeVec(-110.0F, 10.0F, -85.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.375F, KeyframeAnimations.degreeVec(50.0F, 10.0F, -85.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.75F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("LeftArm", new AnimationChannel(AnimationChannel.Targets.POSITION, 
					new Keyframe(0.0F, KeyframeAnimations.posVec(1.0F, -1.8767F, -1.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, -1.7863F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.375F, KeyframeAnimations.posVec(2.0F, -1.8757F, 2.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("LeftArmSleeve", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
					new Keyframe(0.25F, KeyframeAnimations.degreeVec(-110.0F, 10.0F, -85.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.375F, KeyframeAnimations.degreeVec(50.0F, 10.0F, -85.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.75F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("LeftArmSleeve", new AnimationChannel(AnimationChannel.Targets.POSITION, 
					new Keyframe(0.0F, KeyframeAnimations.posVec(1.0F, -1.747F, -1.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, -1.8974F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.375F, KeyframeAnimations.posVec(2.0F, -1.7974F, 2.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.build();
	}
}
