package net.sashakyotoz.client.renderers.blocks;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.sashakyotoz.client.models.EffigyPedestalModel;
import net.sashakyotoz.common.blocks.custom.EffigyBlock;
import net.sashakyotoz.common.blocks.custom.entities.EffigyBlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

public class EffigyRenderer implements BlockEntityRenderer<EffigyBlockEntity> {
    private static final float FALLBACK_SHADE = 0.55F;
    private final EntityRenderDispatcher entityRenderer;
    private final ItemRenderer itemRenderer;
    private final EffigyPedestalModel pedestal;
    private final Map<EffigyBlock.Boss, Optional<RenderType>> statueTypes = new EnumMap<>(EffigyBlock.Boss.class);

    public EffigyRenderer(BlockEntityRendererProvider.Context context) {
        this.entityRenderer = context.getEntityRenderer();
        this.itemRenderer = context.getItemRenderer();
        this.pedestal = new EffigyPedestalModel(context.bakeLayer(EffigyPedestalModel.LAYER));
    }

    @Override
    public void render(EffigyBlockEntity effigy, float partialTick, PoseStack poseStack, MultiBufferSource buffers, int packedLight, int packedOverlay) {
        Level level = effigy.getLevel();
        if (level == null) {
            return;
        }
        BlockState state = effigy.getBlockState();
        BlockPos pos = effigy.getBlockPos();
        EffigyBlock.Boss boss = state.getValue(EffigyBlock.BOSS);
        EffigyBlock.Phase phase = state.getValue(EffigyBlock.PHASE);
        float yaw = state.getValue(EffigyBlock.FACING).toYRot();
        int light = LevelRenderer.getLightColor(level, pos.above());
        this.renderPedestal(boss, yaw, poseStack, buffers, light);
        this.renderKeys(effigy, boss, yaw, poseStack, buffers, light, level, pos);
        if (phase == EffigyBlock.Phase.AWAKENED) {
            return;
        }
        Entity statue = effigy.getOrCreateStatue(level);
        if (statue == null) {
            return;
        }
        orient(statue, pos, yaw);
        poseStack.pushPose();
        poseStack.translate(0.5D, 1.0D, 0.5D);
        if (phase == EffigyBlock.Phase.AWAKENING) {
            float time = level.getGameTime() + partialTick;
            poseStack.translate(Mth.sin(time * 2.7F) * 0.03F, 0.0D, Mth.cos(time * 3.1F) * 0.03F);
        }
        EntityRenderer<? super Entity> renderer = this.entityRenderer.getRenderer(statue);
        renderer.render(statue, yaw, 0.0F, poseStack, new StatueBuffers(buffers, this.statueType(boss).orElse(null)), light);
        poseStack.popPose();
    }

    private void renderPedestal(EffigyBlock.Boss boss, float yaw, PoseStack poseStack, MultiBufferSource buffers, int light) {
        poseStack.pushPose();
        poseStack.translate(0.5D, 1.5D, 0.5D);
        poseStack.mulPose(Axis.YP.rotationDegrees(-yaw));
        poseStack.scale(1.0F, -1.0F, -1.0F);
        this.pedestal.render(poseStack, buffers.getBuffer(RenderType.entityCutout(boss.pedestalTexture())), light, OverlayTexture.NO_OVERLAY);
        poseStack.popPose();
    }

    private void renderKeys(EffigyBlockEntity effigy, EffigyBlock.Boss boss, float yaw, PoseStack poseStack, MultiBufferSource buffers, int light, Level level, BlockPos pos) {
        int keys = effigy.getKeys();
        if (keys <= 0) {
            return;
        }
        ItemStack key = new ItemStack(boss.key());
        float step = 360.0F / boss.keysRequired;
        for (int socket = 0; socket < keys; socket++) {
            poseStack.pushPose();
            poseStack.translate(0.5D, 1.25D, 0.5D);
            poseStack.mulPose(Axis.YP.rotationDegrees(-yaw - 45.0F - socket * step));
            poseStack.translate(0.0D, 0.0D, 1.05D);
            poseStack.scale(0.5F, 0.5F, 0.5F);
            this.itemRenderer.renderStatic(key, ItemDisplayContext.FIXED, light, OverlayTexture.NO_OVERLAY, poseStack, buffers, level, (int) pos.asLong() + socket);
            poseStack.popPose();
        }
    }

    private Optional<RenderType> statueType(EffigyBlock.Boss boss) {
        return this.statueTypes.computeIfAbsent(boss, value -> Minecraft.getInstance().getResourceManager()
                .getResource(value.statueTexture())
                .map(resource -> RenderType.entityCutoutNoCull(value.statueTexture())));
    }

    private static void orient(Entity statue, BlockPos pos, float yaw) {
        statue.setPos(pos.getX() + 0.5D, pos.getY() + 1.0D, pos.getZ() + 0.5D);
        statue.setYRot(yaw);
        statue.yRotO = yaw;
        statue.setXRot(0.0F);
        statue.xRotO = 0.0F;
        if (statue instanceof LivingEntity living) {
            living.yBodyRot = yaw;
            living.yBodyRotO = yaw;
            living.yHeadRot = yaw;
            living.yHeadRotO = yaw;
        }
    }

    @Override
    public boolean shouldRenderOffScreen(EffigyBlockEntity effigy) {
        return true;
    }

    @Override
    public int getViewDistance() {
        return 96;
    }

    @Override
    public AABB getRenderBoundingBox(EffigyBlockEntity effigy) {
        BlockPos pos = effigy.getBlockPos();
        return new AABB(pos.getX() - 2.0D, pos.getY(), pos.getZ() - 2.0D, pos.getX() + 3.0D, pos.getY() + 6.0D, pos.getZ() + 3.0D);
    }

    private record StatueBuffers(MultiBufferSource delegate, @Nullable RenderType statue) implements MultiBufferSource {
        @Override
        public VertexConsumer getBuffer(RenderType type) {
            if (isGlint(type)) {
                return DiscardingConsumer.INSTANCE;
            }
            if (this.statue == null || isItemSheet(type)) {
                return new ShadedConsumer(this.delegate.getBuffer(type), FALLBACK_SHADE);
            }
            return this.delegate.getBuffer(this.statue);
        }

        private static boolean isGlint(RenderType type) {
            return type == RenderType.glint() || type == RenderType.glintTranslucent()
                    || type == RenderType.entityGlint() || type == RenderType.entityGlintDirect()
                    || type == RenderType.armorEntityGlint();
        }

        private static boolean isItemSheet(RenderType type) {
            return type == Sheets.cutoutBlockSheet() || type == Sheets.solidBlockSheet()
                    || type == Sheets.translucentCullBlockSheet() || type == Sheets.translucentItemSheet();
        }
    }

    private record ShadedConsumer(VertexConsumer delegate, float shade) implements VertexConsumer {
        @Override
        public VertexConsumer addVertex(float x, float y, float z) {
            this.delegate.addVertex(x, y, z);
            return this;
        }

        @Override
        public VertexConsumer setColor(int red, int green, int blue, int alpha) {
            int grey = (int) ((red * 0.299F + green * 0.587F + blue * 0.114F) * this.shade);
            this.delegate.setColor(grey, grey, grey, alpha);
            return this;
        }

        @Override
        public VertexConsumer setUv(float u, float v) {
            this.delegate.setUv(u, v);
            return this;
        }

        @Override
        public VertexConsumer setUv1(int u, int v) {
            this.delegate.setUv1(u, v);
            return this;
        }

        @Override
        public VertexConsumer setUv2(int u, int v) {
            this.delegate.setUv2(u, v);
            return this;
        }

        @Override
        public VertexConsumer setNormal(float x, float y, float z) {
            this.delegate.setNormal(x, y, z);
            return this;
        }
    }

    private enum DiscardingConsumer implements VertexConsumer {
        INSTANCE;

        @Override
        public VertexConsumer addVertex(float x, float y, float z) {
            return this;
        }

        @Override
        public VertexConsumer setColor(int red, int green, int blue, int alpha) {
            return this;
        }

        @Override
        public VertexConsumer setUv(float u, float v) {
            return this;
        }

        @Override
        public VertexConsumer setUv1(int u, int v) {
            return this;
        }

        @Override
        public VertexConsumer setUv2(int u, int v) {
            return this;
        }

        @Override
        public VertexConsumer setNormal(float x, float y, float z) {
            return this;
        }
    }
}