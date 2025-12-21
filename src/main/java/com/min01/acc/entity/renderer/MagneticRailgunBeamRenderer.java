package com.min01.acc.entity.renderer;

import org.joml.Vector4f;

import com.github.alexthe666.citadel.client.render.LightningBoltData;
import com.github.alexthe666.citadel.client.render.LightningRender;
import com.min01.acc.entity.projectile.EntityMagneticRailgunBeam;
import com.min01.acc.util.ACCClientUtil;
import com.min01.acc.util.ACCUtil;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class MagneticRailgunBeamRenderer extends EntityRenderer<EntityMagneticRailgunBeam>
{
    private static final LightningBoltData.BoltRenderInfo LIGHTNING_BOLT_INFO = new LightningBoltData.BoltRenderInfo(0.0F, 0.01F, 0.3F, 0.6F, new Vector4f(0.1F, 0.1F, 0.9F, 0.3F), 0);
    
	public MagneticRailgunBeamRenderer(Context pContext) 
	{
		super(pContext);
	}
	
	@Override
	public void render(EntityMagneticRailgunBeam pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) 
	{
		if(pEntity.getOwner() != null)
		{
            LightningRender lightningRender = ACCClientUtil.getLightingRender(pEntity.getUUID());
            Vec3 position = pEntity.getPosition(pPartialTick);

            pPoseStack.pushPose();
            pPoseStack.translate(-position.x, -position.y, -position.z);
            lightningRender.render(pPartialTick, pPoseStack, pBuffer);
            pPoseStack.popPose();

            Vec3 pos = position.add(0.0F, 0.5F, 0.0F);
            Vec3 lookPos = ACCUtil.getLookPos(pEntity.getRotationVector(), pos, 0, 0, 30);
            int segCount = Mth.clamp((int) Math.ceil(0.3F * pos.horizontalDistance()), 3, 30);
            LightningBoltData bolt1 = new LightningBoltData(LIGHTNING_BOLT_INFO, pos, lookPos, segCount).size(0.3F).lifespan(1).spawn(LightningBoltData.SpawnFunction.CONSECUTIVE).fade(LightningBoltData.FadeFunction.NONE);
            lightningRender.update(1, bolt1, pPartialTick);
		}
	}

	@Override
	public ResourceLocation getTextureLocation(EntityMagneticRailgunBeam pEntity) 
	{
		return null;
	}
}
