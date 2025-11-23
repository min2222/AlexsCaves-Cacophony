package com.min01.acc.item.animation;

import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;

public class MagneticRailgunAnimation 
{
	public static final AnimationDefinition RAILGUN_RELOAD = AnimationDefinition.Builder.withLength(3.5F)
			.addAnimation("cartridge", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
				new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.7083F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.875F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -90.0F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(1.2917F, KeyframeAnimations.degreeVec(0.0F, 30.0F, -90.0F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(1.5F, KeyframeAnimations.degreeVec(0.0F, 57.4594F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(1.5833F, KeyframeAnimations.degreeVec(-14.9749F, -26.046F, -1.3668F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(1.75F, KeyframeAnimations.degreeVec(-9.9164F, 2.919F, -8.8278F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(1.9167F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
			))
			.addAnimation("cartridge", new AnimationChannel(AnimationChannel.Targets.POSITION, 
				new Keyframe(0.0833F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.1667F, KeyframeAnimations.posVec(0.0F, 1.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, 1.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.7083F, KeyframeAnimations.posVec(0.0F, 4.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.875F, KeyframeAnimations.posVec(0.0F, 4.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(1.0F, KeyframeAnimations.posVec(15.0F, -1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(1.2917F, KeyframeAnimations.posVec(17.0F, 13.0F, -2.0F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(1.5F, KeyframeAnimations.posVec(5.0F, 6.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(1.5833F, KeyframeAnimations.posVec(2.38F, 5.79F, -8.82F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(1.625F, KeyframeAnimations.posVec(3.15F, 4.28F, -5.12F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(1.75F, KeyframeAnimations.posVec(5.0F, 4.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(1.9167F, KeyframeAnimations.posVec(0.0F, 3.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(2.0F, KeyframeAnimations.posVec(0.0F, 1.94F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(2.0833F, KeyframeAnimations.posVec(0.0F, 1.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(2.3333F, KeyframeAnimations.posVec(0.0F, 1.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(2.4167F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
			))
			.addAnimation("cartridge", new AnimationChannel(AnimationChannel.Targets.SCALE, 
				new Keyframe(0.9583F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.0407F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.0417F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.2907F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.2917F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR)
			))
			.addAnimation("ammo", new AnimationChannel(AnimationChannel.Targets.SCALE, 
				new Keyframe(1.0F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.2907F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.2917F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR)
			))
			.build();

		public static final AnimationDefinition RAILGUN_CHARGE = AnimationDefinition.Builder.withLength(1.5833F)
			.addAnimation("left_charger", new AnimationChannel(AnimationChannel.Targets.POSITION, 
				new Keyframe(0.8333F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.9583F, KeyframeAnimations.posVec(2.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
			))
			.addAnimation("right_charger", new AnimationChannel(AnimationChannel.Targets.POSITION, 
				new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.125F, KeyframeAnimations.posVec(-2.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
			))
			.addAnimation("right_charger2", new AnimationChannel(AnimationChannel.Targets.POSITION, 
				new Keyframe(0.625F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.75F, KeyframeAnimations.posVec(-2.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
			))
			.addAnimation("left_charger2", new AnimationChannel(AnimationChannel.Targets.POSITION, 
				new Keyframe(0.3333F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.4583F, KeyframeAnimations.posVec(2.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
			))
			.build();

		public static final AnimationDefinition RAILGUN_CHARGED = AnimationDefinition.Builder.withLength(0.0F)
			.addAnimation("left_charger", new AnimationChannel(AnimationChannel.Targets.POSITION, 
				new Keyframe(0.0F, KeyframeAnimations.posVec(2.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
			))
			.addAnimation("right_charger", new AnimationChannel(AnimationChannel.Targets.POSITION, 
				new Keyframe(0.0F, KeyframeAnimations.posVec(-2.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
			))
			.addAnimation("right_charger2", new AnimationChannel(AnimationChannel.Targets.POSITION, 
				new Keyframe(0.0F, KeyframeAnimations.posVec(-2.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
			))
			.addAnimation("left_charger2", new AnimationChannel(AnimationChannel.Targets.POSITION, 
				new Keyframe(0.0F, KeyframeAnimations.posVec(2.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
			))
			.build();

		public static final AnimationDefinition RAILGUN_FIRE = AnimationDefinition.Builder.withLength(0.75F)
			.addAnimation("left_charger", new AnimationChannel(AnimationChannel.Targets.POSITION, 
				new Keyframe(0.0833F, KeyframeAnimations.posVec(2.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.1667F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
			))
			.addAnimation("right_charger", new AnimationChannel(AnimationChannel.Targets.POSITION, 
				new Keyframe(0.125F, KeyframeAnimations.posVec(-2.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.2083F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
			))
			.addAnimation("right_charger2", new AnimationChannel(AnimationChannel.Targets.POSITION, 
				new Keyframe(0.0417F, KeyframeAnimations.posVec(-2.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.125F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
			))
			.addAnimation("left_charger2", new AnimationChannel(AnimationChannel.Targets.POSITION, 
				new Keyframe(0.0F, KeyframeAnimations.posVec(2.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.0833F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
			))
			.addAnimation("rail", new AnimationChannel(AnimationChannel.Targets.POSITION, 
				new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.3333F, KeyframeAnimations.posVec(0.0F, 0.0F, 4.0F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.4583F, KeyframeAnimations.posVec(0.0F, 0.0F, 4.0F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.5417F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
			))
			.build();
}
