package com.min01.acc.entity.renderer;

import com.min01.acc.entity.projectile.EntityThrowableFallingBlock;
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
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;

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
		}
	}

	@Override
	public ResourceLocation getTextureLocation(EntityThrowableFallingBlock p_114482_) 
	{
		return null;
	}
}
