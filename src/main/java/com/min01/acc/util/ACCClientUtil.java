package com.min01.acc.util;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector4f;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class ACCClientUtil 
{
	public static final Minecraft MC = Minecraft.getInstance();
	
	public static Vector3f scaleVec(double p_253806_, double p_253647_, double p_254396_) 
	{
		return new Vector3f((float)((p_253806_ - 1.0D) / 180 * Math.PI), (float)((p_253647_ - 1.0D) / 180 * Math.PI), (float)((p_254396_ - 1.0D) / 180 * Math.PI));
	}
	
	public static Vector3f degreeVec(double p_254402_, double p_253917_, double p_254397_) 
	{
		return new Vector3f((float)(p_254402_ / 180 * Math.PI), (float)(p_253917_ / 180 * Math.PI), (float)(p_254397_ / 180 * Math.PI));
	}
	
	public static float getElapsedSeconds(boolean looping, float lengthInSeconds, long time) 
	{
		float f = (float) time / 1000.0F;
		return looping ? f % lengthInSeconds : f;
	}
	
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
}
