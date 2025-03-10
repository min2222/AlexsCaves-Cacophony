package com.min01.acc.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import com.github.alexmodguy.alexscaves.server.entity.living.RelicheirusEntity;
import com.min01.acc.entity.IPainted;

@Mixin(RelicheirusEntity.class)
public class MixinRelicheirusEntity implements IPainted
{
	@Unique
	private boolean isPainted;
	
	@Override
	public void setPainted(boolean value)
	{
		this.isPainted = value;
	}

	@Override
	public boolean isPainted()
	{
		return this.isPainted;
	}
}
