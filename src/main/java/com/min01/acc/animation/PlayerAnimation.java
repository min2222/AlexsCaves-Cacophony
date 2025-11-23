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
					new Keyframe(0.5F, KeyframeAnimations.posVec(-1.0F, -1.8828F, -1.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.build();

		public static final AnimationDefinition RAYBLADE_SWING_RIGHT = AnimationDefinition.Builder.withLength(0.75F)
				.addAnimation("RightArm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
					new Keyframe(0.25F, KeyframeAnimations.degreeVec(-110.0F, -10.0F, 85.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.375F, KeyframeAnimations.degreeVec(50.0F, -10.0F, 85.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.75F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("RightArm", new AnimationChannel(AnimationChannel.Targets.POSITION, 
					new Keyframe(0.0F, KeyframeAnimations.posVec(-1.0F, -1.8096F, -1.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, -1.8402F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.375F, KeyframeAnimations.posVec(-2.0F, -1.8448F, 2.0F), AnimationChannel.Interpolations.CATMULLROM),
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
					new Keyframe(0.5F, KeyframeAnimations.posVec(1.0F, -1.8071F, -1.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.build();

		public static final AnimationDefinition RAYBLADE_SWING_LEFT = AnimationDefinition.Builder.withLength(0.75F)
				.addAnimation("LeftArm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
					new Keyframe(0.25F, KeyframeAnimations.degreeVec(-110.0F, 10.0F, -85.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.375F, KeyframeAnimations.degreeVec(50.0F, 10.0F, -85.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.75F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("LeftArm", new AnimationChannel(AnimationChannel.Targets.POSITION, 
					new Keyframe(0.0F, KeyframeAnimations.posVec(1.0F, -1.7244F, -1.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, -1.8175F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.375F, KeyframeAnimations.posVec(2.0F, -1.8781F, 2.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.build();
	}
	
	public static class RadrifleAnimation
	{
		public static final AnimationDefinition RADRIFLE_FIRE = AnimationDefinition.Builder.withLength(0.4583F)
				.addAnimation("RightArm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
					new Keyframe(0.0F, KeyframeAnimations.degreeVec(-87.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.0417F, KeyframeAnimations.degreeVec(-87.5184F, 0.0F, 7.3847F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.0833F, KeyframeAnimations.degreeVec(-87.5095F, -0.2178F, 4.9953F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.2083F, KeyframeAnimations.degreeVec(-87.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("RightArm", new AnimationChannel(AnimationChannel.Targets.POSITION, 
					new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.0833F, KeyframeAnimations.posVec(0.0F, 1.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.125F, KeyframeAnimations.posVec(0.0F, 0.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.2083F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("LeftArm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
					new Keyframe(0.0F, KeyframeAnimations.degreeVec(-80.0F, 32.5F, 45.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.0417F, KeyframeAnimations.degreeVec(-80.2603F, 30.037F, 44.4987F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.0833F, KeyframeAnimations.degreeVec(-77.7603F, 30.037F, 44.4987F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.2083F, KeyframeAnimations.degreeVec(-80.0F, 32.5F, 45.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("LeftArm", new AnimationChannel(AnimationChannel.Targets.POSITION, 
					new Keyframe(0.0F, KeyframeAnimations.posVec(-5.0F, -3.0F, -9.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.0417F, KeyframeAnimations.posVec(-5.0F, -2.0F, -9.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.0833F, KeyframeAnimations.posVec(-5.0F, -2.0F, -8.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.1667F, KeyframeAnimations.posVec(-5.0F, -3.0F, -9.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.build();
		
		public static final AnimationDefinition RADRIFLE_HOLD = AnimationDefinition.Builder.withLength(0.0F)
				.addAnimation("RightArm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
					new Keyframe(0.0F, KeyframeAnimations.degreeVec(-87.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
				))
				.addAnimation("LeftArm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
					new Keyframe(0.0F, KeyframeAnimations.degreeVec(-80.0F, 32.5F, 45.0F), AnimationChannel.Interpolations.LINEAR)
				))
				.addAnimation("LeftArm", new AnimationChannel(AnimationChannel.Targets.POSITION, 
					new Keyframe(0.0F, KeyframeAnimations.posVec(-5.0F, -3.0F, -9.0F), AnimationChannel.Interpolations.LINEAR)
				))
				.build();
		
		public static final AnimationDefinition RADRIFLE_HOLD_NEAR_WALL = AnimationDefinition.Builder.withLength(0.0F)
				.addAnimation("RightArm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
					new Keyframe(0.0F, KeyframeAnimations.degreeVec(-160.0F, -72.5F, 60.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("RightArm", new AnimationChannel(AnimationChannel.Targets.POSITION, 
					new Keyframe(0.0F, KeyframeAnimations.posVec(-5.0F, -5.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("LeftArm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
					new Keyframe(0.0F, KeyframeAnimations.degreeVec(-120.0F, 45.0F, 85.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("LeftArm", new AnimationChannel(AnimationChannel.Targets.POSITION, 
					new Keyframe(0.0F, KeyframeAnimations.posVec(1.0F, -7.0F, -2.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.build();
		
		public static final AnimationDefinition RADRIFLE_RUNNING = AnimationDefinition.Builder.withLength(0.5F).looping()
				.addAnimation("RightArm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
					new Keyframe(0.0F, KeyframeAnimations.degreeVec(-160.0F, -72.5F, 60.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.125F, KeyframeAnimations.degreeVec(-165.0F, -65.0F, 65.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.25F, KeyframeAnimations.degreeVec(-165.0F, -62.5F, 65.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.375F, KeyframeAnimations.degreeVec(-160.0F, -70.0F, 62.5F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.5F, KeyframeAnimations.degreeVec(-160.0F, -72.5F, 60.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("RightArm", new AnimationChannel(AnimationChannel.Targets.POSITION, 
					new Keyframe(0.0F, KeyframeAnimations.posVec(-5.0F, -5.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.125F, KeyframeAnimations.posVec(-4.0F, -4.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.25F, KeyframeAnimations.posVec(-4.0F, -5.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.375F, KeyframeAnimations.posVec(-5.0F, -4.05F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.5F, KeyframeAnimations.posVec(-5.0F, -5.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("LeftArm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
					new Keyframe(0.0F, KeyframeAnimations.degreeVec(-120.0F, 45.0F, 85.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("LeftArm", new AnimationChannel(AnimationChannel.Targets.POSITION, 
					new Keyframe(0.0F, KeyframeAnimations.posVec(1.0F, -7.0F, -2.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.125F, KeyframeAnimations.posVec(2.0F, -6.0F, -2.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.25F, KeyframeAnimations.posVec(1.79F, -7.0F, -2.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.375F, KeyframeAnimations.posVec(1.0F, -7.0F, -2.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.5F, KeyframeAnimations.posVec(1.0F, -7.0F, -2.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.build();
		
		public static final AnimationDefinition RADRIFLE_OVERCHARGE_FIRE = AnimationDefinition.Builder.withLength(1.0F)
				.addAnimation("RightArm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
					new Keyframe(0.0F, KeyframeAnimations.degreeVec(-87.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.0417F, KeyframeAnimations.degreeVec(-87.5184F, 0.0F, 7.3847F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.0833F, KeyframeAnimations.degreeVec(-87.5095F, -0.2178F, 4.9953F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.75F, KeyframeAnimations.degreeVec(-87.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("RightArm", new AnimationChannel(AnimationChannel.Targets.POSITION, 
					new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.0833F, KeyframeAnimations.posVec(0.0F, 1.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.125F, KeyframeAnimations.posVec(0.0F, 0.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.75F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("LeftArm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
					new Keyframe(0.0F, KeyframeAnimations.degreeVec(-80.0F, 32.5F, 45.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.0417F, KeyframeAnimations.degreeVec(-80.2603F, 30.037F, 44.4987F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.0833F, KeyframeAnimations.degreeVec(-77.7603F, 30.037F, 44.4987F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.75F, KeyframeAnimations.degreeVec(-80.0F, 32.5F, 45.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("LeftArm", new AnimationChannel(AnimationChannel.Targets.POSITION, 
					new Keyframe(0.0F, KeyframeAnimations.posVec(-5.0F, -3.0F, -9.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.0417F, KeyframeAnimations.posVec(-5.0F, -2.0F, -9.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.0833F, KeyframeAnimations.posVec(-5.0F, -2.0F, -8.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.7083F, KeyframeAnimations.posVec(-5.0F, -3.0F, -9.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.build();

			public static final AnimationDefinition RADRIFLE_OVERHEAT = AnimationDefinition.Builder.withLength(2.0F)
				.addAnimation("RightArm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
					new Keyframe(0.0F, KeyframeAnimations.degreeVec(-87.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.0417F, KeyframeAnimations.degreeVec(-87.5F, 15.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.125F, KeyframeAnimations.degreeVec(-87.5F, 15.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.25F, KeyframeAnimations.degreeVec(-87.5F, 5.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.375F, KeyframeAnimations.degreeVec(-87.5F, 10.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.6667F, KeyframeAnimations.degreeVec(-87.5F, 10.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.8333F, KeyframeAnimations.degreeVec(-87.5F, 5.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(1.0F, KeyframeAnimations.degreeVec(-87.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(1.1667F, KeyframeAnimations.degreeVec(-87.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(1.375F, KeyframeAnimations.degreeVec(-90.0F, -2.5F, 75.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(1.625F, KeyframeAnimations.degreeVec(-85.0F, -2.5F, 77.5F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(1.7083F, KeyframeAnimations.degreeVec(-85.0F, 5.0F, 15.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(1.8333F, KeyframeAnimations.degreeVec(-87.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("RightArm", new AnimationChannel(AnimationChannel.Targets.POSITION, 
					new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
					new Keyframe(0.875F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.9167F, KeyframeAnimations.posVec(-1.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.9583F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(1.4583F, KeyframeAnimations.posVec(0.0F, 0.06F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(1.5F, KeyframeAnimations.posVec(0.0F, -1.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(1.5417F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(1.625F, KeyframeAnimations.posVec(0.0F, 0.0F, -1.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(1.75F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("LeftArm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
					new Keyframe(0.0F, KeyframeAnimations.degreeVec(-80.0F, 32.5F, 45.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.0417F, KeyframeAnimations.degreeVec(-80.0F, 32.5F, 45.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.125F, KeyframeAnimations.degreeVec(-80.0F, -32.5F, 35.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.2083F, KeyframeAnimations.degreeVec(-80.0F, -25.0F, 35.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.375F, KeyframeAnimations.degreeVec(-125.0F, -55.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.4167F, KeyframeAnimations.degreeVec(-120.0F, -50.0F, 7.5F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.4583F, KeyframeAnimations.degreeVec(-80.0F, -47.5F, 25.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.5F, KeyframeAnimations.degreeVec(-67.5F, -45.0F, 20.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.5417F, KeyframeAnimations.degreeVec(-75.0F, -40.0F, 7.5F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.5833F, KeyframeAnimations.degreeVec(-142.5F, -32.5F, 10.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.625F, KeyframeAnimations.degreeVec(-140.0F, -35.0F, -7.5F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.7917F, KeyframeAnimations.degreeVec(-145.0F, -70.0F, 15.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.9167F, KeyframeAnimations.degreeVec(-125.0F, -1.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(1.4167F, KeyframeAnimations.degreeVec(-140.0F, -25.0F, -42.5F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(1.5F, KeyframeAnimations.degreeVec(-120.0F, 0.0F, -102.5F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(1.75F, KeyframeAnimations.degreeVec(-80.0F, 32.5F, 45.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("LeftArm", new AnimationChannel(AnimationChannel.Targets.POSITION, 
					new Keyframe(0.0F, KeyframeAnimations.posVec(-5.0F, -3.0F, -9.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.125F, KeyframeAnimations.posVec(-2.33F, 1.89F, -7.22F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.1667F, KeyframeAnimations.posVec(-1.0F, 0.0F, -6.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.625F, KeyframeAnimations.posVec(-1.0F, 0.0F, -6.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.8333F, KeyframeAnimations.posVec(-5.0F, -3.0F, -9.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.9167F, KeyframeAnimations.posVec(-8.0F, -2.0F, -9.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(1.0833F, KeyframeAnimations.posVec(-7.0F, -7.0F, -9.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(1.4167F, KeyframeAnimations.posVec(-3.0F, 3.0F, -9.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(1.75F, KeyframeAnimations.posVec(-5.0F, -3.0F, -9.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.build();
	}
	
	public static class RailgunAnimation
	{
		public static final AnimationDefinition RAILGUN_HOLD = AnimationDefinition.Builder.withLength(0.0F)
				.addAnimation("RightArm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
					new Keyframe(0.0F, KeyframeAnimations.degreeVec(-87.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("RightArm", new AnimationChannel(AnimationChannel.Targets.POSITION, 
					new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("LeftArm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
					new Keyframe(0.0F, KeyframeAnimations.degreeVec(-80.0F, 32.5F, 45.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("LeftArm", new AnimationChannel(AnimationChannel.Targets.POSITION, 
					new Keyframe(0.0F, KeyframeAnimations.posVec(-7.0F, -4.0F, -9.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.build();

			public static final AnimationDefinition RAILGUN_HOLD_NEAR_WALL = AnimationDefinition.Builder.withLength(0.0F).looping()
				.addAnimation("RightArm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
					new Keyframe(0.0F, KeyframeAnimations.degreeVec(-160.0F, -72.5F, 60.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("RightArm", new AnimationChannel(AnimationChannel.Targets.POSITION, 
					new Keyframe(0.0F, KeyframeAnimations.posVec(-5.0F, -5.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("LeftArm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
					new Keyframe(0.0F, KeyframeAnimations.degreeVec(-112.5F, 25.0F, 100.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("LeftArm", new AnimationChannel(AnimationChannel.Targets.POSITION, 
					new Keyframe(0.0F, KeyframeAnimations.posVec(-2.0F, -6.0F, -2.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.build();

			public static final AnimationDefinition RAILGUN_RELOAD = AnimationDefinition.Builder.withLength(3.5F)
					.addAnimation("RightArm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
						new Keyframe(0.0F, KeyframeAnimations.degreeVec(-87.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.5417F, KeyframeAnimations.degreeVec(-89.4586F, -2.4407F, 77.4885F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.9167F, KeyframeAnimations.degreeVec(-89.4586F, -2.4407F, 77.4885F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.0833F, KeyframeAnimations.degreeVec(-94.2617F, -9.0548F, 100.3376F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.625F, KeyframeAnimations.degreeVec(-94.4586F, -2.4407F, 77.4885F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(2.0F, KeyframeAnimations.degreeVec(-89.4586F, -2.4407F, 77.4885F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(2.1667F, KeyframeAnimations.degreeVec(-81.2588F, -1.9941F, 63.3099F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(2.4167F, KeyframeAnimations.degreeVec(-82.953F, -1.8918F, 45.3483F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(2.625F, KeyframeAnimations.degreeVec(-85.029F, -8.5185F, 23.3838F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(2.875F, KeyframeAnimations.degreeVec(-85.988F, -2.7175F, -4.7548F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(3.0417F, KeyframeAnimations.degreeVec(-87.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
					))
					.addAnimation("RightArm", new AnimationChannel(AnimationChannel.Targets.POSITION, 
						new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.0833F, KeyframeAnimations.posVec(0.0F, -1.0F, -0.02F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.125F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.0833F, KeyframeAnimations.posVec(0.19F, 0.0F, -0.73F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.625F, KeyframeAnimations.posVec(-1.0F, 0.0F, -1.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(2.1667F, KeyframeAnimations.posVec(0.4F, 0.0F, -0.6F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(2.2917F, KeyframeAnimations.posVec(0.4F, -1.0F, -0.42F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(2.375F, KeyframeAnimations.posVec(0.4F, -1.0F, -0.42F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(2.4167F, KeyframeAnimations.posVec(-0.67F, -1.89F, -0.33F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(2.875F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
					))
					.addAnimation("LeftArm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
						new Keyframe(0.0F, KeyframeAnimations.degreeVec(-80.0F, 32.5F, 45.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.375F, KeyframeAnimations.degreeVec(-185.0F, 15.0F, -35.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.7083F, KeyframeAnimations.degreeVec(-185.0F, 15.0F, -45.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.875F, KeyframeAnimations.degreeVec(-185.0F, 15.0F, -45.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.0833F, KeyframeAnimations.degreeVec(-195.1369F, 4.5593F, -99.9474F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.2917F, KeyframeAnimations.degreeVec(5.0F, 35.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.5F, KeyframeAnimations.degreeVec(-89.0703F, 30.2962F, -17.187F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.7083F, KeyframeAnimations.degreeVec(-165.059F, 4.8324F, -98.7223F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.9167F, KeyframeAnimations.degreeVec(-164.3216F, -0.4989F, -79.425F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(2.0833F, KeyframeAnimations.degreeVec(-165.0F, 0.0F, -85.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(2.1667F, KeyframeAnimations.degreeVec(-161.9889F, -7.5411F, -37.2459F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(2.2917F, KeyframeAnimations.degreeVec(-165.1422F, -33.6948F, -6.1026F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(2.4167F, KeyframeAnimations.degreeVec(-154.008F, -5.1733F, -65.6397F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(2.6667F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(3.0F, KeyframeAnimations.degreeVec(-80.0F, 32.5F, 45.0F), AnimationChannel.Interpolations.CATMULLROM)
					))
					.addAnimation("LeftArm", new AnimationChannel(AnimationChannel.Targets.POSITION, 
						new Keyframe(0.0F, KeyframeAnimations.posVec(-7.0F, -4.0F, -9.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.25F, KeyframeAnimations.posVec(-1.0F, -5.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.375F, KeyframeAnimations.posVec(0.02F, -4.83F, -9.6F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.7083F, KeyframeAnimations.posVec(6.0F, -4.0F, -9.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.875F, KeyframeAnimations.posVec(6.0F, -4.0F, -9.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.0F, KeyframeAnimations.posVec(6.0F, -11.0F, -9.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.2917F, KeyframeAnimations.posVec(7.0F, -1.0F, -9.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.5833F, KeyframeAnimations.posVec(7.0F, -1.0F, -9.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.75F, KeyframeAnimations.posVec(9.0F, -1.0F, -6.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.9167F, KeyframeAnimations.posVec(7.0F, 0.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(2.0833F, KeyframeAnimations.posVec(5.0F, 0.0F, -6.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(2.2083F, KeyframeAnimations.posVec(-1.63F, -3.79F, -4.33F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(2.375F, KeyframeAnimations.posVec(-0.72F, -0.74F, -3.7F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(2.5F, KeyframeAnimations.posVec(4.0F, 0.0F, -4.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(2.9167F, KeyframeAnimations.posVec(-7.0F, -4.0F, -9.0F), AnimationChannel.Interpolations.CATMULLROM)
					))
					.build();

			public static final AnimationDefinition RAILGUN_CHARGE = AnimationDefinition.Builder.withLength(1.5833F)
				.addAnimation("RightArm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
					new Keyframe(0.0F, KeyframeAnimations.degreeVec(-87.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.2083F, KeyframeAnimations.degreeVec(-87.5095F, -0.2178F, 4.9953F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.3333F, KeyframeAnimations.degreeVec(-87.5008F, 4.7774F, 5.213F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.5833F, KeyframeAnimations.degreeVec(-87.5008F, 4.7774F, 5.213F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.6667F, KeyframeAnimations.degreeVec(-87.5095F, -0.2178F, 4.9953F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.8333F, KeyframeAnimations.degreeVec(-87.5095F, -0.2178F, 4.9953F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.9583F, KeyframeAnimations.degreeVec(-87.5214F, 0.3262F, -7.4929F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(1.0833F, KeyframeAnimations.degreeVec(-87.5214F, 0.3262F, -7.4929F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(1.1667F, KeyframeAnimations.degreeVec(-87.5851F, -0.6469F, 14.9864F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(1.4583F, KeyframeAnimations.degreeVec(-87.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("RightArm", new AnimationChannel(AnimationChannel.Targets.POSITION, 
					new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.2083F, KeyframeAnimations.posVec(0.0F, -1.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.5833F, KeyframeAnimations.posVec(0.0F, -1.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.6667F, KeyframeAnimations.posVec(1.0F, -1.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.8333F, KeyframeAnimations.posVec(1.0F, -1.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.9583F, KeyframeAnimations.posVec(0.0F, -1.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(1.0833F, KeyframeAnimations.posVec(0.0F, -1.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(1.1667F, KeyframeAnimations.posVec(1.0F, -1.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(1.3333F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("LeftArm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
					new Keyframe(0.0F, KeyframeAnimations.degreeVec(-80.0F, 32.5F, 45.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.125F, KeyframeAnimations.degreeVec(-75.0F, 17.5F, 55.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.5833F, KeyframeAnimations.degreeVec(-75.0F, 17.5F, 55.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.6667F, KeyframeAnimations.degreeVec(-70.5211F, 12.2725F, 72.2927F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.8333F, KeyframeAnimations.degreeVec(-70.5211F, 12.2725F, 72.2927F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.9583F, KeyframeAnimations.degreeVec(-72.8674F, 15.4213F, 62.5151F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(1.0833F, KeyframeAnimations.degreeVec(-72.8674F, 15.4213F, 62.5151F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(1.1667F, KeyframeAnimations.degreeVec(-69.5712F, 10.5652F, 77.0873F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(1.3333F, KeyframeAnimations.degreeVec(-80.0F, 32.5F, 45.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("LeftArm", new AnimationChannel(AnimationChannel.Targets.POSITION, 
					new Keyframe(0.0F, KeyframeAnimations.posVec(-7.0F, -4.0F, -9.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.2083F, KeyframeAnimations.posVec(-7.0F, -5.0F, -9.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.3333F, KeyframeAnimations.posVec(-8.0F, -5.0F, -9.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.5833F, KeyframeAnimations.posVec(-8.0F, -5.0F, -9.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.6667F, KeyframeAnimations.posVec(-6.0F, -5.0F, -9.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.8333F, KeyframeAnimations.posVec(-6.0F, -5.0F, -9.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.9583F, KeyframeAnimations.posVec(-7.0F, -5.0F, -9.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(1.0833F, KeyframeAnimations.posVec(-7.0F, -5.0F, -9.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(1.1667F, KeyframeAnimations.posVec(-6.0F, -5.0F, -9.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(1.4583F, KeyframeAnimations.posVec(-7.0F, -4.0F, -9.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.build();

			public static final AnimationDefinition RAILGUN_CHARGED = AnimationDefinition.Builder.withLength(0.0F)
				.addAnimation("RightArm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
					new Keyframe(0.0F, KeyframeAnimations.degreeVec(-87.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("RightArm", new AnimationChannel(AnimationChannel.Targets.POSITION, 
					new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("LeftArm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
					new Keyframe(0.0F, KeyframeAnimations.degreeVec(-80.0F, 32.5F, 45.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("LeftArm", new AnimationChannel(AnimationChannel.Targets.POSITION, 
					new Keyframe(0.0F, KeyframeAnimations.posVec(-7.0F, -4.0F, -9.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.build();

			public static final AnimationDefinition RAILGUN_FIRE = AnimationDefinition.Builder.withLength(0.75F)
				.addAnimation("RightArm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
					new Keyframe(0.0F, KeyframeAnimations.degreeVec(-87.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.0417F, KeyframeAnimations.degreeVec(-87.5184F, 0.0F, 7.3847F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.0833F, KeyframeAnimations.degreeVec(-87.5095F, -0.2178F, 4.9953F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.2917F, KeyframeAnimations.degreeVec(-87.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.3333F, KeyframeAnimations.degreeVec(-87.5F, 0.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.4583F, KeyframeAnimations.degreeVec(-87.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("RightArm", new AnimationChannel(AnimationChannel.Targets.POSITION, 
					new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.0833F, KeyframeAnimations.posVec(0.0F, 1.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.125F, KeyframeAnimations.posVec(0.0F, 0.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.2917F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.3333F, KeyframeAnimations.posVec(0.0F, 0.0F, 6.0F), AnimationChannel.Interpolations.LINEAR),
					new Keyframe(0.4583F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("LeftArm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
					new Keyframe(0.0F, KeyframeAnimations.degreeVec(-80.0F, 32.5F, 45.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.0417F, KeyframeAnimations.degreeVec(-80.2603F, 30.037F, 44.4987F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.0833F, KeyframeAnimations.degreeVec(-77.7603F, 30.037F, 44.4987F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.2917F, KeyframeAnimations.degreeVec(-77.5F, 47.5F, 50.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.4167F, KeyframeAnimations.degreeVec(-77.5F, 47.5F, 50.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.4583F, KeyframeAnimations.degreeVec(-80.0F, 32.5F, 45.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("LeftArm", new AnimationChannel(AnimationChannel.Targets.POSITION, 
					new Keyframe(0.0F, KeyframeAnimations.posVec(-7.0F, -4.0F, -9.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.0417F, KeyframeAnimations.posVec(-5.0F, -4.0F, -9.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.0833F, KeyframeAnimations.posVec(-5.0F, -2.0F, -8.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.125F, KeyframeAnimations.posVec(-5.0F, -3.98F, -8.33F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.2917F, KeyframeAnimations.posVec(-5.0F, -5.0F, -11.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.3333F, KeyframeAnimations.posVec(-5.0F, -5.5F, -4.06F), AnimationChannel.Interpolations.LINEAR),
					new Keyframe(0.5F, KeyframeAnimations.posVec(-5.0F, -4.87F, -8.02F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.5833F, KeyframeAnimations.posVec(-7.0F, -4.0F, -9.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.build();

			public static final AnimationDefinition RAILGUN_RUNNING = AnimationDefinition.Builder.withLength(0.5F).looping()
				.addAnimation("RightArm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
					new Keyframe(0.0F, KeyframeAnimations.degreeVec(-160.0F, -72.5F, 60.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.125F, KeyframeAnimations.degreeVec(-165.0F, -65.0F, 65.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.25F, KeyframeAnimations.degreeVec(-165.0F, -62.5F, 65.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.375F, KeyframeAnimations.degreeVec(-160.0F, -70.0F, 62.5F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.5F, KeyframeAnimations.degreeVec(-160.0F, -72.5F, 60.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("RightArm", new AnimationChannel(AnimationChannel.Targets.POSITION, 
					new Keyframe(0.0F, KeyframeAnimations.posVec(-5.0F, -5.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.125F, KeyframeAnimations.posVec(-4.0F, -4.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.25F, KeyframeAnimations.posVec(-4.0F, -5.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.375F, KeyframeAnimations.posVec(-5.0F, -4.05F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.5F, KeyframeAnimations.posVec(-5.0F, -5.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("LeftArm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
					new Keyframe(0.0F, KeyframeAnimations.degreeVec(-112.5F, 25.0F, 100.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("LeftArm", new AnimationChannel(AnimationChannel.Targets.POSITION, 
					new Keyframe(0.0F, KeyframeAnimations.posVec(-2.0F, -6.0F, -2.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.125F, KeyframeAnimations.posVec(-1.0F, -5.0F, -2.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.25F, KeyframeAnimations.posVec(-1.0F, -6.0F, -2.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.375F, KeyframeAnimations.posVec(-2.0F, -6.0F, -2.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.5F, KeyframeAnimations.posVec(-2.0F, -6.0F, -2.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.build();
	}
}
