package com.min01.acc.event;

import java.lang.reflect.Method;

import com.github.alexmodguy.alexscaves.server.entity.living.DinosaurEntity;
import com.min01.acc.AlexsCavesCacophony;
import com.min01.acc.capabilities.PaintedCapabilityImpl;
import com.min01.acc.entity.living.EntityOvivenator;
import com.min01.acc.misc.ACCLootTables;
import com.min01.acc.misc.ACCTags;
import com.min01.acc.util.ACCUtil;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

@Mod.EventBusSubscriber(modid = AlexsCavesCacophony.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EventHandlerForge
{
	public static final Method EXPLODE_CREEPER = ObfuscationReflectionHelper.findMethod(Creeper.class, "m_32315_");
	
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
		ACCUtil.tickItemAnimation(event.player);
		ACCUtil.tickPlayerAnimation(event.player);
		ACCUtil.tickOverlayProgress(event.player);
	}
	
	@SubscribeEvent
	public static void onLivingTick(LivingTickEvent event)
	{
		LivingEntity living = event.getEntity();
		
		if(living instanceof Creeper creeper && creeper.getPersistentData().contains("FromRailgun"))
		{
			if(creeper.verticalCollision || creeper.horizontalCollision)
			{
				try
				{
					EXPLODE_CREEPER.invoke(creeper);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void onEntityJoinLevel(EntityJoinLevelEvent event)
	{
		Entity entity = event.getEntity();
		if(entity instanceof DinosaurEntity dino)
		{
			dino.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(dino, EntityOvivenator.class, false, t -> canTargetOvivenator(dino, t)));
		}
		if(entity.getType().is(ACCTags.ACCEntity.PAINTABLE) && Math.random() <= 0.1F)
		{
			entity.getCapability(PaintedCapabilityImpl.PAINTED).ifPresent(t -> 
			{
				if(!t.isPainted())
				{
					t.setPainted(true);
				}
			});
		}
	}
	
	public static boolean canTargetOvivenator(DinosaurEntity dino, LivingEntity living)
	{
		if(living instanceof EntityOvivenator ovivenator)
		{
			if(!ovivenator.isBaby() && !ovivenator.isTame() && ovivenator.isHoldingEgg() && !ovivenator.getCurrentEgg().isEmpty())
			{
				ItemStack stack = ovivenator.getCurrentEgg();
				return stack.is(Item.BY_BLOCK.getOrDefault(dino.createEggBlockState().getBlock(), Items.AIR));
			}
		}
		return false;
	}
}
