package org.icannt.netherendingores.client;

import org.icannt.netherendingores.NetherendingOres;
import org.icannt.netherendingores.Registration;
import org.icannt.netherendingores.common.entity.ArmedNetherOreEntity;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = NetherendingOres.MOD_ID, value = Dist.CLIENT)
public final class ClientEvents {

    private ClientEvents() {
    }

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(Registration.ARMED_NETHER_ORE.get(), NoopRenderer::new);
    }

    /**
     * Armed ores are intentionally invisible, but the entity type still needs a
     * renderer registered on the client.
     */
    private static final class NoopRenderer extends EntityRenderer<ArmedNetherOreEntity> {

        private static final ResourceLocation DUMMY_TEXTURE =
                ResourceLocation.withDefaultNamespace("textures/entity/experience_orb.png");

        private NoopRenderer(EntityRendererProvider.Context context) {
            super(context);
        }

        @Override
        public void render(ArmedNetherOreEntity entity, float entityYaw, float partialTick, PoseStack poseStack,
                           MultiBufferSource buffer, int packedLight) {
            // Deliberately invisible.
        }

        @Override
        public ResourceLocation getTextureLocation(ArmedNetherOreEntity entity) {
            return DUMMY_TEXTURE;
        }
    }
}
