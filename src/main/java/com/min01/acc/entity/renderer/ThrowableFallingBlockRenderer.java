package com.min01.acc.entity.renderer;

import org.joml.Vector4f;

import com.github.alexthe666.citadel.client.render.LightningBoltData;
import com.github.alexthe666.citadel.client.render.LightningRender;
import com.min01.acc.entity.projectile.EntityThrowableFallingBlock;
import com.min01.acc.util.ACCClientUtil;
import com.min01.acc.util.ACCUtil;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public class ThrowableFallingBlockRenderer extends EntityRenderer<EntityThrowableFallingBlock>
{
	private final BlockRenderDispatcher dispatcher;
	
	public ThrowableFallingBlockRenderer(Context p_174008_)
	{
		super(p_174008_);
		this.dispatcher = p_174008_.getBlockRenderDispatcher();
	}
	
	@Override
	public void render(EntityThrowableFallingBlock p_114485_, float p_114486_, float p_114487_, PoseStack p_114488_, MultiBufferSource p_114489_, int p_114490_)
	{
		BlockState state = p_114485_.getBlockState();
		if(state.getRenderShape() == RenderShape.MODEL) 
		{
			Level level = p_114485_.level;
			if(state != level.getBlockState(p_114485_.blockPosition()) && state.getRenderShape() != RenderShape.INVISIBLE) 
			{
				p_114488_.pushPose();
				BlockPos pos = BlockPos.containing(p_114485_.getX(), p_114485_.getBoundingBox().maxY, p_114485_.getZ());
				p_114488_.translate(-0.5D, 0.0D, -0.5D);
				BakedModel model = this.dispatcher.getBlockModel(state);
				for(RenderType renderType : model.getRenderTypes(state, RandomSource.create(state.getSeed(p_114485_.blockPosition())), net.minecraftforge.client.model.data.ModelData.EMPTY))
				{
					renderType = net.minecraftforge.client.RenderTypeHelper.getMovingBlockRenderType(renderType);
					this.dispatcher.getModelRenderer().tesselateBlock(level, model, state, pos, p_114488_, p_114489_.getBuffer(renderType), false, RandomSource.create(), state.getSeed(p_114485_.blockPosition()), OverlayTexture.NO_OVERLAY, net.minecraftforge.client.model.data.ModelData.EMPTY, renderType);
				}
				p_114488_.popPose();
			}
			Entity owner = ACCUtil.getOwner(p_114485_);
			if(owner != null)
			{
				Vec3 pos = p_114485_.getEyePosition(p_114487_);
				Vec3 toVec = owner.getEyePosition(p_114487_);
				toVec = ACCUtil.getLookPos(new Vec2(owner.getViewXRot(p_114487_), owner.getViewYRot(p_114487_)), toVec, 0, 0.5F, 2);
				
				p_114488_.pushPose();
				p_114488_.translate(-pos.x, -pos.y, -pos.z);
				pos = pos.add(0.0F, 0.5F, 0.0F);
	            int segCount = Mth.clamp((int) pos.distanceTo(toVec) + 2, 3, 30);
	            float spreadFactor = 0.1F;
	            LightningBoltData.BoltRenderInfo boltData = new LightningBoltData.BoltRenderInfo(0.0F, spreadFactor, 0.0F, 0.0F, new Vector4f(0.1F, 0.1F, 0.9F, 0.8F), 0.1F);
	            LightningBoltData.BoltRenderInfo boltData1 = new LightningBoltData.BoltRenderInfo(0.0F, spreadFactor, 0.0F, 0.0F, new Vector4f(0.9F, 0.1F, 0.1F, 0.8F), 0.1F);
	            LightningBoltData bolt1 = new LightningBoltData(boltData, pos, toVec, segCount).size(0.1F).lifespan(1).spawn(LightningBoltData.SpawnFunction.CONSECUTIVE).fade(LightningBoltData.FadeFunction.NONE);
	            LightningBoltData bolt2 = new LightningBoltData(boltData1, pos, toVec, segCount).size(0.1F).lifespan(1).spawn(LightningBoltData.SpawnFunction.CONSECUTIVE).fade(LightningBoltData.FadeFunction.NONE);
	            LightningRender lightningRender = ACCClientUtil.getLightingRender(p_114485_.getUUID());
	            lightningRender.update(1, bolt1, p_114487_);
	            lightningRender.update(2, bolt2, p_114487_);
	            lightningRender.render(p_114487_, p_114488_, p_114489_);
	            p_114488_.popPose();
			}
			else if(ACCClientUtil.LIGHTNING_MAP.containsKey(p_114485_.getUUID())) 
			{
				ACCClientUtil.LIGHTNING_MAP.remove(p_114485_.getUUID());
	        }
		}
	}

	@Override
	public ResourceLocation getTextureLocation(EntityThrowableFallingBlock p_114482_) 
	{
		return null;
	}
}
