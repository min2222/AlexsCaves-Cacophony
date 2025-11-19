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
    
	public MagneticRailgunBeamRenderer(Context p_174008_) 
	{
		super(p_174008_);
	}
	
	@Override
	public void render(EntityMagneticRailgunBeam p_114485_, float p_114486_, float p_114487_, PoseStack p_114488_, MultiBufferSource p_114489_, int p_114490_) 
	{
		if(p_114485_.getOwner() != null)
		{
            LightningRender lightningRender = ACCClientUtil.getLightingRender(p_114485_.getUUID());
            Vec3 position = p_114485_.getPosition(p_114487_);

            p_114488_.pushPose();
            p_114488_.translate(-position.x, -position.y, -position.z);
            lightningRender.render(p_114487_, p_114488_, p_114489_);
            p_114488_.popPose();

            Vec3 pos = position.add(0.0F, 0.5F, 0.0F);
            Vec3 lookPos = ACCUtil.getLookPos(p_114485_.getRotationVector(), pos, 0, 0, 30);
            int segCount = Mth.clamp((int) Math.ceil(0.3F * pos.horizontalDistance()), 3, 30);
            LightningBoltData bolt1 = new LightningBoltData(LIGHTNING_BOLT_INFO, pos, lookPos, segCount).size(0.3F).lifespan(1).spawn(LightningBoltData.SpawnFunction.CONSECUTIVE).fade(LightningBoltData.FadeFunction.NONE);
            lightningRender.update(1, bolt1, p_114487_);
		}
	}

	@Override
	public ResourceLocation getTextureLocation(EntityMagneticRailgunBeam p_114482_) 
	{
		return null;
	}
}
