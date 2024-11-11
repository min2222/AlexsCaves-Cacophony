package com.min01.acc.block;

import com.min01.acc.AlexsCavesCacophony;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ACCBlocks
{
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, AlexsCavesCacophony.MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, AlexsCavesCacophony.MODID);
    
    public static final RegistryObject<Block> GLOOMOTH_COCOON = BLOCKS.register("gloomoth_cocoon", () -> new GloomothCocoonBlock());
    public static final RegistryObject<Block> GLOOMOTH_EGGS = BLOCKS.register("gloomoth_eggs", () -> new GloomothEggsBlock());
}
