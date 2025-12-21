package com.min01.acc.entity;

import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import com.min01.acc.AlexsCavesCacophony;
import com.min01.acc.entity.living.EntityGloomworm;
import com.min01.acc.entity.living.EntityOvivenator;
import com.min01.acc.entity.projectile.EntityFearArrow;
import com.min01.acc.entity.projectile.EntityMagneticRailgunBeam;
import com.min01.acc.entity.projectile.EntityNeodymiumShackle;
import com.min01.acc.entity.projectile.EntityRadrifleBeam;
import com.min01.acc.entity.projectile.EntityThrowableFallingBlock;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ACCEntities
{
	public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, AlexsCavesCacophony.MODID);
	
	public static final RegistryObject<EntityType<EntityFearArrow>> FEAR_ARROW = registerEntity("arrow_of_fear", EntityType.Builder.<EntityFearArrow>of(EntityFearArrow::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20));
	public static final RegistryObject<EntityType<EntityNeodymiumShackle>> NEODYMIUM_SHACKLE = registerEntity("neodymium_shackle", EntityType.Builder.<EntityNeodymiumShackle>of(EntityNeodymiumShackle::new, MobCategory.MISC).fireImmune().sized(0.5F, 0.5F));
	public static final RegistryObject<EntityType<EntityRadrifleBeam>> RADRIFLE_BEAM = registerEntity("radrifle_beam", createBuilder(EntityRadrifleBeam::new, MobCategory.MISC).fireImmune().sized(0.5F, 0.5F).clientTrackingRange(100));
	public static final RegistryObject<EntityType<EntityThrowableFallingBlock>> THROWABLE_FALLING_BLOCK = registerEntity("throwable_falling_block", EntityType.Builder.<EntityThrowableFallingBlock>of(EntityThrowableFallingBlock::new, MobCategory.MISC).sized(1.0F, 1.0F));
	public static final RegistryObject<EntityType<EntityMagneticRailgunBeam>> MAGNETIC_RAILGUN_BEAM = registerEntity("magnetic_railgun_beam", createBuilder(EntityMagneticRailgunBeam::new, MobCategory.MISC).fireImmune().sized(0.5F, 0.5F));
	
	public static final RegistryObject<EntityType<EntityGloomworm>> GLOOMWORM = registerEntity("gloomworm", createBuilder(EntityGloomworm::new, MobCategory.CREATURE).sized(0.5F, 0.3F));
	public static final RegistryObject<EntityType<EntityOvivenator>> OVIVENATOR = registerEntity("ovivenator", createBuilder(EntityOvivenator::new, ACEntityRegistry.CAVE_CREATURE).sized(0.875F, 2.625F));
	
	public static <T extends Entity> EntityType.Builder<T> createBuilder(EntityType.EntityFactory<T> factory, MobCategory category)
	{
		return EntityType.Builder.<T>of(factory, category);
	}
	
	public static <T extends Entity> RegistryObject<EntityType<T>> registerEntity(String name, EntityType.Builder<T> builder) 
	{
		return ENTITY_TYPES.register(name, () -> builder.build(ResourceLocation.fromNamespaceAndPath(AlexsCavesCacophony.MODID, name).toString()));
	}
}
