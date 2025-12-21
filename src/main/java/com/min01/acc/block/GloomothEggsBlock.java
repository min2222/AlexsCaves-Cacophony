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
	public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) 
	{
		return SHAPE;
	}
	
	@Override
	public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pMovedByPiston)
	{
		pLevel.scheduleTick(pPos, this, pLevel.random.nextInt(1200, 9600));
	}
	
	@Override
	public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) 
	{
		EntityGloomworm worm = new EntityGloomworm(ACCEntities.GLOOMWORM.get(), pLevel);
		worm.setPos(Vec3.atCenterOf(pPos));
		pLevel.addFreshEntity(worm);
		pLevel.destroyBlock(pPos, false);
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder)
	{
		super.createBlockStateDefinition(pBuilder);
		pBuilder.add(WATERLOGGED);
	}
	
	@Override
	public BlockState updateShape(BlockState pState, Direction pDirection, BlockState pNeighborState, LevelAccessor pLevel, BlockPos pPos, BlockPos pNeighborPos)
	{
		if(pState.getValue(WATERLOGGED)) 
		{
			pLevel.scheduleTick(pPos, Fluids.WATER, Fluids.WATER.getTickDelay(pLevel));
		}
		return super.updateShape(pState, pDirection, pNeighborState, pLevel, pPos, pNeighborPos);
	}

	@Override
	public boolean canBeReplaced(BlockState pState, BlockPlaceContext pUseContext)
	{
		return !pUseContext.getItemInHand().is(ACCItems.GLOOMOTH_EGGS.get()) || super.canBeReplaced(pState, pUseContext);
	}

	@Override
	public FluidState getFluidState(BlockState pState) 
	{
		return pState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : Fluids.EMPTY.defaultFluidState();
	}

	@Override
	public boolean propagatesSkylightDown(BlockState pState, BlockGetter pLevel, BlockPos pPos) 
	{
		return pState.getFluidState().isEmpty();
	}

	@Override
	public MultifaceSpreader getSpreader() 
	{
		return this.spreader;
	}
}
