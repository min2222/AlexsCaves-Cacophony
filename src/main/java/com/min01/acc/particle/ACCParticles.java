package com.min01.acc.particle;

import com.min01.acc.AlexsCavesCacophony;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ACCParticles 
{
	public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, AlexsCavesCacophony.MODID);
	
	public static final RegistryObject<SimpleParticleType> RAILGUN_CHARGE = PARTICLES.register("railgun_charge", () -> new SimpleParticleType(false));
}
