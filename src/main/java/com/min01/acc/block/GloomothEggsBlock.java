package com.min01.acc.block;

import com.min01.acc.entity.ACCEntities;
import com.min01.acc.entity.living.EntityGloomworm;
import com.min01.acc.item.ACCItems;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.MultifaceSpreader;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class GloomothEggsBlock extends MultifaceBlock implements SimpleWaterloggedBlock
{
	public static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 1.5D, 16.0D);
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	public final MultifaceSpreader spreader = new MultifaceSpreader(this);

	public GloomothEggsBlock()
	{
		super(BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_WHITE).randomTicks().instabreak().noOcclusion().replaceable().noCollission().strength(0.2F).sound(SoundType.FROGSPAWN).pushReaction(PushReaction.DESTROY));
		this.registerDefaultState(this.defaultBlockState().setValue(WATERLOGGED, Boolean.valueOf(false)));
	}
	
	@Override
	public VoxelShape getShape(BlockState p_221199_, BlockGetter p_221200_, BlockPos p_221201_, CollisionContext p_221202_) 
	{
		return SHAPE;
	}
	
	@Override
	public void onPlace(BlockState p_221227_, Level p_221228_, BlockPos p_221229_, BlockState p_221230_, boolean p_221231_)
	{
		p_221228_.scheduleTick(p_221229_, this, p_221228_.random.nextInt(1200, 9600));
	}
	
	@Override
	public void randomTick(BlockState p_222954_, ServerLevel p_222955_, BlockPos p_222956_, RandomSource p_222957_) 
	{
		EntityGloomworm worm = new EntityGloomworm(ACCEntities.GLOOMWORM.get(), p_222955_);
		worm.setPos(Vec3.atCenterOf(p_222956_));
		p_222955_.addFreshEntity(worm);
		p_222955_.destroyBlock(p_222956_, false);
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_153309_)
	{
		super.createBlockStateDefinition(p_153309_);
		p_153309_.add(WATERLOGGED);
	}
	
	@Override
	public BlockState updateShape(BlockState p_153302_, Direction p_153303_, BlockState p_153304_, LevelAccessor p_153305_, BlockPos p_153306_, BlockPos p_153307_)
	{
		if(p_153302_.getValue(WATERLOGGED)) 
		{
			p_153305_.scheduleTick(p_153306_, Fluids.WATER, Fluids.WATER.getTickDelay(p_153305_));
		}
		return super.updateShape(p_153302_, p_153303_, p_153304_, p_153305_, p_153306_, p_153307_);
	}

	@Override
	public boolean canBeReplaced(BlockState p_153299_, BlockPlaceContext p_153300_)
	{
		return !p_153300_.getItemInHand().is(ACCItems.GLOOMOTH_EGGS.get()) || super.canBeReplaced(p_153299_, p_153300_);
	}

	@Override
	public FluidState getFluidState(BlockState p_153311_) 
	{
		return p_153311_.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : Fluids.EMPTY.defaultFluidState();
	}

	@Override
	public boolean propagatesSkylightDown(BlockState p_181225_, BlockGetter p_181226_, BlockPos p_181227_) 
	{
		return p_181225_.getFluidState().isEmpty();
	}

	@Override
	public MultifaceSpreader getSpreader() 
	{
		return this.spreader;
	}
}
