package com.min01.acc.block;

import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import com.github.alexmodguy.alexscaves.server.entity.living.GloomothEntity;
import com.min01.acc.entity.ACCEntities;
import com.min01.acc.entity.living.EntityGloomworm;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.IceBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;

public class GloomothCocoonBlock extends Block
{
    public static final int HATCH_TIME_TICKS = 24000;
    public static final int RANDOM_HATCH_OFFSET_TICKS = 300;
    public static final EnumProperty<WormName> WORM_NAME = EnumProperty.create("worm_name", WormName.class);
    
	public GloomothCocoonBlock() 
	{
		super(BlockBehaviour.Properties.copy(Blocks.WHITE_WOOL).randomTicks());
		this.registerDefaultState(this.stateDefinition.any().setValue(WORM_NAME, WormName.NONE));
	}
	
	@Override
	public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) 
	{
		if(!(pLevel.getBlockState(pPos.below()).getBlock() instanceof IceBlock))
		{
			GloomothEntity moth = new GloomothEntity(ACEntityRegistry.GLOOMOTH.get(), pLevel);
			moth.setPos(Vec3.atCenterOf(pPos.above()));
			if(pState.hasProperty(WORM_NAME))
			{
				WormName wormName = pState.getValue(WORM_NAME);
				if(wormName == WormName.GONDAL)
				{
					moth.setCustomName(Component.literal(wormName.name));
				}
			}
			pLevel.addFreshEntity(moth);
			pLevel.destroyBlock(pPos, false);
		}
	}
	
	@Override
	public void stepOn(Level pLevel, BlockPos pPos, BlockState pState, Entity pEntity) 
	{
		if(!pEntity.isSteppingCarefully())
		{
			this.destroyAndSpawnWorm(pLevel, pPos, pEntity, 100);
		}
	}
	
	@Override
	public void fallOn(Level pLevel, BlockState pState, BlockPos pPos, Entity pEntity, float pFallDistance)
	{
		this.destroyAndSpawnWorm(pLevel, pPos, pEntity, 3);
		super.fallOn(pLevel, pState, pPos, pEntity, pFallDistance);
	}
	
	@Override
	public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) 
	{
		this.spawnWorm(level, pos);
		return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
	}
	
	public void destroyAndSpawnWorm(Level level, BlockPos pos, Entity entity, int chance)
	{
		if(entity instanceof LivingEntity living && ForgeEventFactory.getMobGriefingEvent(level, entity))
		{
			if(!(living instanceof EntityGloomworm) && !(living instanceof GloomothEntity) && !(living instanceof Bat))
			{
				if(level.random.nextInt() == chance)
				{
					this.spawnWorm(level, pos);
					level.destroyBlock(pos, false);
				}
			}
		}
	}
	
	public void spawnWorm(Level level, BlockPos pos)
	{
		BlockState state = level.getBlockState(pos);
		EntityGloomworm worm = new EntityGloomworm(ACCEntities.GLOOMWORM.get(), level);
		if(state.hasProperty(WORM_NAME))
		{
			WormName wormName = state.getValue(WORM_NAME);
			if(wormName == WormName.GONDAL)
			{
				worm.setCustomName(Component.literal(wormName.name));
			}
		}
		worm.setPos(Vec3.atCenterOf(pos));
		level.addFreshEntity(worm);
	}
	
	@Override
    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pMovedByPiston) 
    {
		int j = HATCH_TIME_TICKS / 3;
		pLevel.gameEvent(GameEvent.BLOCK_PLACE, pPos, GameEvent.Context.of(pState));
		pLevel.scheduleTick(pPos, this, j + pLevel.random.nextInt(RANDOM_HATCH_OFFSET_TICKS));
    }
	
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder)
    {
    	pBuilder.add(WORM_NAME);
    }
	
	@Override
	public boolean isPathfindable(BlockState pState, BlockGetter pLevel, BlockPos pPos, PathComputationType pType)
	{
		return false;
	}
	
	public static enum WormName implements StringRepresentable
	{
		NONE("None"),
		GONDAL("Gondal");
		
		private final String name;
		
		private WormName(String name) 
		{
			this.name = name;
		}

		@Override
		public String getSerializedName()
		{
			return this.name.toLowerCase();
		}
	}
}
