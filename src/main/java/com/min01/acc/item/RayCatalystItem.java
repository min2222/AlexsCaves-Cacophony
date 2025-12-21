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
	public InteractionResult interactLivingEntity(ItemStack pStack, Player pPlayer, LivingEntity pInteractionTarget, InteractionHand pUsedHand)
	{
		if(pInteractionTarget instanceof TremorzillaEntity zilla)
		{
			if(zilla.isTame())
			{
				if(!pPlayer.getAbilities().instabuild)
				{
					pStack.shrink(1);
					pPlayer.getCooldowns().addCooldown(pStack.getItem(), 100);
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
		return super.interactLivingEntity(pStack, pPlayer, pInteractionTarget, pUsedHand);
	}
}
