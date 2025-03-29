package com.min01.acc.event;

import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import com.min01.acc.AlexsCavesCacophony;
import com.min01.acc.capabilities.ACCCapabilities;
import com.min01.acc.entity.projectile.EntityFearArrow;
import com.min01.acc.item.ACCItems;
import com.min01.acc.misc.ACCLootTables;
import com.min01.acc.util.ACCUtil;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
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
    }
    
	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent event)
	{
		Player player = event.player;
		ACCUtil.updatePlayerTick(player);
		for(ItemStack stack : player.getInventory().items)
		{
			ACCUtil.updateItemTick(player, stack);
		}
		for(ItemStack stack : player.getInventory().armor)
		{
			ACCUtil.updateItemTick(player, stack);
		}
		for(ItemStack stack : player.getInventory().offhand)
		{
			ACCUtil.updateItemTick(player, stack);
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
	
    @SubscribeEvent
    public static void onProjectileImpact(ProjectileImpactEvent event)
    {
    	Projectile projectile = event.getProjectile();
		HitResult hitResult = event.getRayTraceResult();
		if(hitResult instanceof EntityHitResult entityHit)
		{
			Entity entity = entityHit.getEntity();
	    	if(projectile instanceof EntityFearArrow arrow)
	    	{
				if(arrow.getOwner() != null)
				{
					Entity owner = arrow.getOwner();
	                if(entity instanceof PathfinderMob mob && (!(mob instanceof TamableAnimal) || !((TamableAnimal) mob).isInSittingPose())) 
	                {
	                	if(!mob.getType().is(ACTagRegistry.RESISTS_TREMORSAURUS_ROAR))
	                	{
		                    mob.setTarget(null);
		                    mob.setLastHurtByMob(null);
		                    if(mob.onGround())
		                    {
		                        Vec3 randomShake = new Vec3(owner.level.random.nextFloat() - 0.5F, 0, owner.level.random.nextFloat() - 0.5F).scale(0.1F);
		                        mob.setDeltaMovement(mob.getDeltaMovement().multiply(0.7F, 1, 0.7F).add(randomShake));
		                    }
	                        Vec3 vec = LandRandomPos.getPosAway(mob, 15, 7, owner.position());
	                        if(vec != null)
	                        {
	                            mob.getNavigation().moveTo(vec.x, vec.y, vec.z, 2.0D);
	                        }
	                	}
	                }
				}
	    	}
		}
    }
}
