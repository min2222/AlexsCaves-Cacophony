package com.min01.acc.event;

import com.min01.acc.AlexsCavesCacophony;
import com.min01.acc.entity.ACCEntities;
import com.min01.acc.entity.model.ModelFearArrow;
import com.min01.acc.entity.model.ModelGloomworm;
import com.min01.acc.entity.model.ModelNeodymiumShackle;
import com.min01.acc.entity.model.ModelOvivenator;
import com.min01.acc.entity.model.ModelRadrifleBeam;
import com.min01.acc.entity.model.ModelRadrifleBeamEnd;
import com.min01.acc.entity.renderer.FearArrowRenderer;
import com.min01.acc.entity.renderer.GloomwormRenderer;
import com.min01.acc.entity.renderer.NeodymiumShackleRenderer;
import com.min01.acc.entity.renderer.OvivenatorRenderer;
import com.min01.acc.entity.renderer.RadrifleBeamRenderer;
import com.min01.acc.item.model.ModelMagneticRailgun;
import com.min01.acc.item.model.ModelRadrifle;
import com.min01.acc.item.model.ModelRayblade;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AlexsCavesCacophony.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEventHandler 
{
    @SubscribeEvent
    public static void onRegisterLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event)
    {
    	event.registerLayerDefinition(ModelRayblade.LAYER_LOCATION, ModelRayblade::createBodyLayer);
    	event.registerLayerDefinition(ModelFearArrow.LAYER_LOCATION, ModelFearArrow::createBodyLayer);
    	event.registerLayerDefinition(ModelRadrifle.LAYER_LOCATION, ModelRadrifle::createBodyLayer);
    	event.registerLayerDefinition(ModelGloomworm.LAYER_LOCATION, ModelGloomworm::createBodyLayer);
    	event.registerLayerDefinition(ModelNeodymiumShackle.LAYER_LOCATION, ModelNeodymiumShackle::createBodyLayer);
    	event.registerLayerDefinition(ModelMagneticRailgun.LAYER_LOCATION, ModelMagneticRailgun::createBodyLayer);
    	event.registerLayerDefinition(ModelOvivenator.LAYER_LOCATION, ModelOvivenator::createBodyLayer);
    	event.registerLayerDefinition(ModelRadrifleBeam.LAYER_LOCATION, ModelRadrifleBeam::createBodyLayer);
    	event.registerLayerDefinition(ModelRadrifleBeamEnd.LAYER_LOCATION, ModelRadrifleBeamEnd::createBodyLayer);
    }
    
    @SubscribeEvent
    public static void onRegisterEntityRenderers(EntityRenderersEvent.RegisterRenderers event)
    {
    	event.registerEntityRenderer(ACCEntities.FEAR_ARROW.get(), FearArrowRenderer::new);
    	event.registerEntityRenderer(ACCEntities.GLOOMWORM.get(), GloomwormRenderer::new);
    	event.registerEntityRenderer(ACCEntities.NEODYMIUM_SHACKLE.get(), NeodymiumShackleRenderer::new);
    	event.registerEntityRenderer(ACCEntities.OVIVENATOR.get(), OvivenatorRenderer::new);
    	event.registerEntityRenderer(ACCEntities.RADRIFLE_BEAM.get(), RadrifleBeamRenderer::new);
    }
}
