package com.min01.acc.util;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector4f;

import com.min01.acc.entity.AbstractAnimatableMonster;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.KeyframeAnimations;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class ACCClientUtil 
{
	//https://github.com/EEEAB/EEEABsMobs/blob/master/src/main/java/com/eeeab/animate/client/util/ModelPartUtils.java#L57
	
    public static Vec3 getWorldPosition(Entity entity, float yaw, ModelPart root, String... modelPartName) 
    {
        PoseStack poseStack = new PoseStack();
        poseStack.translate(entity.getX(), entity.getY(), entity.getZ());
        poseStack.mulPose((new Quaternionf().rotationY((float) ((-yaw + 180.0F) * Math.PI / 180.0F))));
        poseStack.scale(-1.0F, -1.0F, 1.0F);
        ModelPart nextPart = null;
        for(int i = 0; i < modelPartName.length; i++)
        {
            if(i == 0)
            {
                nextPart = root.getChild(modelPartName[0]);
                nextPart.translateAndRotate(poseStack);
            }
            else 
            {
                ModelPart child = nextPart.getChild(modelPartName[i]);
                child.translateAndRotate(poseStack);
                nextPart = child;
            }
        }
        PoseStack.Pose last = poseStack.last();
        Matrix4f matrix4f = last.pose();
        Vector4f vector4f = new Vector4f(0, 0, 0, 1);
        Vector4f mul = vector4f.mul(matrix4f);
        return new Vec3(mul.x, mul.y, mul.z);
    }
    
	public static void animateHead(ModelPart head, float netHeadYaw, float headPitch)
	{
		head.yRot += Math.toRadians(netHeadYaw);
		head.xRot += Math.toRadians(headPitch);
	}
	
	public static void animateWalk(AbstractAnimatableMonster entity, HierarchicalModel<? extends Entity> model, AnimationDefinition animation, float limbSwing, float limbSwingAmount, float p_268138_, float p_268165_) 
	{
		if(entity.getAnimationState() == 0)
		{
			if(ACCUtil.isMoving(entity))
			{
				long i = (long)(limbSwing * 50.0F * p_268138_);
				float f = Math.min(limbSwingAmount * p_268165_, 1.0F);
				KeyframeAnimations.animate(model, animation, i, f, new Vector3f());
			}
		}
	}
}
