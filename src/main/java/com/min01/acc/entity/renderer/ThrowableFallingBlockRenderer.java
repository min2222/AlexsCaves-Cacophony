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
	
	public ThrowableFallingBlockRenderer(Context pContext)
	{
		super(pContext);
		this.dispatcher = pContext.getBlockRenderDispatcher();
	}
	
	@Override
	public void render(EntityThrowableFallingBlock pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight)
	{
		BlockState state = pEntity.getBlockState();
		if(state.getRenderShape() == RenderShape.MODEL) 
		{
			Level level = pEntity.level;
			if(state != level.getBlockState(pEntity.blockPosition()) && state.getRenderShape() != RenderShape.INVISIBLE) 
			{
				pPoseStack.pushPose();
				BlockPos pos = BlockPos.containing(pEntity.getX(), pEntity.getBoundingBox().maxY, pEntity.getZ());
				pPoseStack.translate(-0.5D, 0.0D, -0.5D);
				BakedModel model = this.dispatcher.getBlockModel(state);
				for(RenderType renderType : model.getRenderTypes(state, RandomSource.create(state.getSeed(pEntity.blockPosition())), net.minecraftforge.client.model.data.ModelData.EMPTY))
				{
					renderType = net.minecraftforge.client.RenderTypeHelper.getMovingBlockRenderType(renderType);
					this.dispatcher.getModelRenderer().tesselateBlock(level, model, state, pos, pPoseStack, pBuffer.getBuffer(renderType), false, RandomSource.create(), state.getSeed(pEntity.blockPosition()), OverlayTexture.NO_OVERLAY, net.minecraftforge.client.model.data.ModelData.EMPTY, renderType);
				}
				pPoseStack.popPose();
			}
			Entity owner = ACCUtil.getOwner(pEntity);
			if(owner != null)
			{
				Vec3 pos = pEntity.getEyePosition(pPartialTick);
				Vec3 toVec = owner.getEyePosition(pPartialTick);
				toVec = ACCUtil.getLookPos(new Vec2(owner.getViewXRot(pPartialTick), owner.getViewYRot(pPartialTick)), toVec, -0.5F, 0.25F, 2.0F);
				
				pPoseStack.pushPose();
				pPoseStack.translate(-pos.x, -pos.y, -pos.z);
				pos = pos.add(0.0F, 0.5F, 0.0F);
	            int segCount = Mth.clamp((int) pos.distanceTo(toVec) + 2, 3, 30);
	            float spreadFactor = 0.1F;
	            LightningBoltData.BoltRenderInfo boltData = new LightningBoltData.BoltRenderInfo(0.0F, spreadFactor, 0.0F, 0.0F, new Vector4f(0.9F, 0.1F, 0.1F, 0.8F), 0.1F);
	            LightningBoltData.BoltRenderInfo boltData1 = new LightningBoltData.BoltRenderInfo(0.0F, spreadFactor, 0.0F, 0.0F, new Vector4f(0.9F, 0.1F, 0.1F, 0.8F), 0.1F);
	            LightningBoltData bolt1 = new LightningBoltData(boltData, pos, toVec, segCount).size(0.1F).lifespan(1).spawn(LightningBoltData.SpawnFunction.CONSECUTIVE).fade(LightningBoltData.FadeFunction.NONE);
	            LightningBoltData bolt2 = new LightningBoltData(boltData1, pos, toVec, segCount).size(0.1F).lifespan(1).spawn(LightningBoltData.SpawnFunction.CONSECUTIVE).fade(LightningBoltData.FadeFunction.NONE);
	            LightningRender lightningRender = ACCClientUtil.getLightingRender(pEntity.getUUID());
	            lightningRender.update(1, bolt1, pPartialTick);
	            lightningRender.update(2, bolt2, pPartialTick);
	            lightningRender.render(pPartialTick, pPoseStack, pBuffer);
	            pPoseStack.popPose();
			}
			else if(ACCClientUtil.LIGHTNING_MAP.containsKey(pEntity.getUUID())) 
			{
				ACCClientUtil.LIGHTNING_MAP.remove(pEntity.getUUID());
	        }
		}
	}

	@Override
	public ResourceLocation getTextureLocation(EntityThrowableFallingBlock pEntity) 
	{
		return null;
	}
}
