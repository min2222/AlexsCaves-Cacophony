package com.min01.acc.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import com.github.alexmodguy.alexscaves.server.entity.living.VallumraptorEntity;
import com.min01.acc.entity.IPainted;

@Mixin(VallumraptorEntity.class)
public class MixinVallumraptorEntity implements IPainted
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
