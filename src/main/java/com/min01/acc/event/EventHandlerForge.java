package com.min01.acc.event;

import com.github.alexmodguy.alexscaves.server.entity.living.DinosaurEntity;
import com.min01.acc.AlexsCavesCacophony;
import com.min01.acc.capabilities.ACCCapabilities;
import com.min01.acc.entity.living.EntityOvivenator;
import com.min01.acc.item.ACCItems;
import com.min01.acc.item.animation.IAnimatableItem;
import com.min01.acc.misc.ACCLootTables;
import com.min01.acc.misc.ACCTags;
import com.min01.acc.util.ACCUtil;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AlexsCavesCacophony.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EventHandlerForge
{
    @SubscribeEvent
    public static void onLootTableLoad(LootTableLoadEvent event)
    {
        if(event.getName().toString().matches("alexscaves:entities/hullbreaker")) 
        {
        	event.getTable().addPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootTableReference.lootTableReference(ACCLootTables.HULLBREAKER_TOOTH)).build());
        }
        if(event.getName().toString().matches("alexscaves:entities/vallumraptor")) 
        {
        	event.getTable().addPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootTableReference.lootTableReference(ACCLootTables.RAW_DINO_DRUMSTICK)).build());
        }
        if(event.getName().toString().matches("alexscaves:entities/subterranodon")) 
        {
        	event.getTable().addPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootTableReference.lootTableReference(ACCLootTables.RAW_DINO_DRUMSTICK)).build());
        }
        if(event.getName().toString().matches("alexscaves:entities/grottoceratops")) 
        {
        	event.getTable().addPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootTableReference.lootTableReference(ACCLootTables.RAW_DINO_DRUMSTICK)).build());
        }
        if(event.getName().toString().matches("alexscaves:entities/tremorsaurus")) 
        {
        	event.getTable().addPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootTableReference.lootTableReference(ACCLootTables.TREMORSAURUS_TOOTH)).build());
        }
        if(event.getName().toString().matches("alexscaves:entities/watcher") || event.getName().toString().matches("alexscaves:entities/gloomoth")) 
        {
        	event.getTable().addPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootTableReference.lootTableReference(ACCLootTables.GLOOMOTH_EGGS)).build());
        }
    }
    
	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent event)
	{
		for(ItemStack stack : event.player.getAllSlots())
		{
			if(stack.getItem() instanceof IAnimatableItem)
			{
				stack.getCapability(ACCCapabilities.ITEM_ANIMATION).ifPresent(t -> 
				{
					t.setEntity(event.player);
					t.update();
				});
			}
		}
	}
	
	@SubscribeEvent
	public static void onEntityJoinLevel(EntityJoinLevelEvent event)
	{
		Entity entity = event.getEntity();
		if(entity instanceof DinosaurEntity dino)
		{
			dino.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(dino, EntityOvivenator.class, false, t -> !t.isBaby() && !((EntityOvivenator) t).isTame() && ((EntityOvivenator) t).isHoldingEgg()));
		}
		if(entity.getType().is(ACCTags.ACCEntity.PAINTABLE) && Math.random() <= 0.1F)
		{
			entity.getCapability(ACCCapabilities.PAINTED).ifPresent(t -> 
			{
				if(!t.isPainted())
				{
					t.setPainted(true);
				}
			});
		}
	}
	
	@SubscribeEvent
	public static void onLivingTick(LivingTickEvent event)
	{
		LivingEntity living = event.getEntity();
		living.getCapability(ACCCapabilities.OWNER).ifPresent(t -> 
		{
			Entity owner = t.getOwner();
			if(owner instanceof Player player)
			{
	    		if(player.isHolding(ACCItems.MAGNETIC_RAILGUN.get()))
	    		{
	    			Vec3 lookPos = ACCUtil.getLookPos(new Vec2(player.getXRot(), player.getYHeadRot()), player.getEyePosition(), 0.0F, 0.0F, 5.0F);
	    			HitResult hitResult = living.level.clip(new ClipContext(player.getEyePosition(), lookPos, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));
	    			Vec3 pos = hitResult.getLocation();
	    			living.teleportTo(pos.x, pos.y, pos.z);
	    			living.setOnGround(false);
	    		}
	    		else
	    		{
	    			living.setNoGravity(false);
	    			t.setOwner(null);
	    		}
			}
		});
	}
}
