package com.min01.acc.item;

import com.github.alexmodguy.alexscaves.server.entity.living.TremorzillaEntity;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.github.alexmodguy.alexscaves.server.item.RadioactiveItem;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

public class RayCatalystItem extends RadioactiveItem
{
	public RayCatalystItem() 
	{
		super(new Item.Properties().stacksTo(1).rarity(ACItemRegistry.RARITY_NUCLEAR), 0.001F);
	}
	
	@Override
	public InteractionResult interactLivingEntity(ItemStack p_41398_, Player p_41399_, LivingEntity p_41400_, InteractionHand p_41401_)
	{
		if(p_41400_ instanceof TremorzillaEntity zilla)
		{
			if(zilla.isTame())
			{
				if(!p_41399_.getAbilities().instabuild)
				{
					p_41398_.shrink(1);
					p_41399_.getCooldowns().addCooldown(p_41398_.getItem(), 100);
				}
				if(!zilla.isFiring())
				{
					zilla.setCharge(Math.min(zilla.getCharge() + 100, 1000));
					zilla.heal(zilla.getMaxHealth() - zilla.getHealth());
				}
				else
				{
					int beamTime = ObfuscationReflectionHelper.getPrivateValue(TremorzillaEntity.class, zilla, "beamTime");
					ObfuscationReflectionHelper.setPrivateValue(TremorzillaEntity.class, zilla, Math.max(beamTime - 20, 0), "beamTime");
				}
				if(zilla.getAnimation() == IAnimatedEntity.NO_ANIMATION)
				{
					zilla.setAnimation(TremorzillaEntity.ANIMATION_CHEW);
				}
	            return InteractionResult.SUCCESS;
			}
		}
		return super.interactLivingEntity(p_41398_, p_41399_, p_41400_, p_41401_);
	}
}
