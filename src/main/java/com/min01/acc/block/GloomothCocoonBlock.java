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
	public void randomTick(BlockState p_222954_, ServerLevel p_222955_, BlockPos p_222956_, RandomSource p_222957_) 
	{
		if(!(p_222955_.getBlockState(p_222956_.below()).getBlock() instanceof IceBlock))
		{
			GloomothEntity moth = new GloomothEntity(ACEntityRegistry.GLOOMOTH.get(), p_222955_);
			moth.setPos(Vec3.atCenterOf(p_222956_.above()));
			if(p_222954_.hasProperty(WORM_NAME))
			{
				WormName wormName = p_222954_.getValue(WORM_NAME);
				if(wormName == WormName.GONDAL)
				{
					moth.setCustomName(Component.literal(wormName.name));
				}
			}
			p_222955_.addFreshEntity(moth);
			p_222955_.destroyBlock(p_222956_, false);
		}
	}
	
	@Override
	public void stepOn(Level p_152431_, BlockPos p_152432_, BlockState p_152433_, Entity p_152434_) 
	{
		if(!p_152434_.isSteppingCarefully())
		{
			this.destroyAndSpawnWorm(p_152431_, p_152432_, p_152434_, 100);
		}
	}
	
	@Override
	public void fallOn(Level p_152426_, BlockState p_152427_, BlockPos p_152428_, Entity p_152429_, float p_152430_)
	{
		this.destroyAndSpawnWorm(p_152426_, p_152428_, p_152429_, 3);
		super.fallOn(p_152426_, p_152427_, p_152428_, p_152429_, p_152430_);
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
    public void onPlace(BlockState p_277964_, Level p_277827_, BlockPos p_277526_, BlockState p_277618_, boolean p_277819_) 
    {
		int j = HATCH_TIME_TICKS / 3;
        p_277827_.gameEvent(GameEvent.BLOCK_PLACE, p_277526_, GameEvent.Context.of(p_277964_));
        p_277827_.scheduleTick(p_277526_, this, j + p_277827_.random.nextInt(RANDOM_HATCH_OFFSET_TICKS));
    }
	
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_152043_)
    {
		p_152043_.add(WORM_NAME);
    }
	
	@Override
	public boolean isPathfindable(BlockState p_60475_, BlockGetter p_60476_, BlockPos p_60477_, PathComputationType p_60478_)
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
