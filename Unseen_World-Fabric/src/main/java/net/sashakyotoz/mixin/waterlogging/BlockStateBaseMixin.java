package net.sashakyotoz.mixin.waterlogging;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SupportType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.sashakyotoz.common.blocks.custom.fluids.DarkWaterlogging;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class BlockStateBaseMixin {
    @Unique
    private boolean unseen_world$darkFlag;
    @Unique
    private boolean unseen_world$darkLogged;
    @Unique
    private BlockState unseen_world$twin;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void unseen_world$detectDarkWater(Block owner, ImmutableMap<Property<?>, Comparable<?>> values, MapCodec<BlockState> codec, CallbackInfo ci) {
        if (values == null || values.isEmpty()) {
            return;
        }
        this.unseen_world$darkFlag = Boolean.TRUE.equals(values.get(DarkWaterlogging.DARK_WATER));
        this.unseen_world$darkLogged = this.unseen_world$darkFlag && Boolean.TRUE.equals(values.get(BlockStateProperties.WATERLOGGED));
    }

    @Unique
    private BlockState unseen_world$twin() {
        BlockState twin = this.unseen_world$twin;
        if (twin == null) {
            BlockState self = (BlockState) (Object) this;
            twin = this.unseen_world$darkFlag ? self.setValue(DarkWaterlogging.DARK_WATER, false) : self;
            this.unseen_world$twin = twin;
        }
        return twin;
    }

    @ModifyArg(method = "initCache", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/state/BlockBehaviour$BlockStateBase$Cache;<init>(Lnet/minecraft/world/level/block/state/BlockState;)V"))
    private BlockState unseen_world$cacheFromTwin(BlockState state) {
        return this.unseen_world$darkFlag ? this.unseen_world$twin() : state;
    }

    @Inject(method = "getFluidState", at = @At("HEAD"), cancellable = true)
    private void unseen_world$darkFluid(CallbackInfoReturnable<FluidState> cir) {
        if (this.unseen_world$darkLogged) {
            cir.setReturnValue(DarkWaterlogging.source());
        }
    }

    @Inject(method = "getShape(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/phys/shapes/CollisionContext;)Lnet/minecraft/world/phys/shapes/VoxelShape;",
            at = @At("HEAD"), cancellable = true)
    private void unseen_world$twinShape(BlockGetter level, BlockPos pos, CollisionContext context, CallbackInfoReturnable<VoxelShape> cir) {
        if (this.unseen_world$darkFlag) {
            cir.setReturnValue(this.unseen_world$twin().getShape(level, pos, context));
        }
    }

    @Inject(method = "getCollisionShape(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/phys/shapes/CollisionContext;)Lnet/minecraft/world/phys/shapes/VoxelShape;",
            at = @At("HEAD"), cancellable = true)
    private void unseen_world$twinCollision(BlockGetter level, BlockPos pos, CollisionContext context, CallbackInfoReturnable<VoxelShape> cir) {
        if (this.unseen_world$darkFlag) {
            cir.setReturnValue(this.unseen_world$twin().getCollisionShape(level, pos, context));
        }
    }

    @Inject(method = "getVisualShape", at = @At("HEAD"), cancellable = true)
    private void unseen_world$twinVisual(BlockGetter level, BlockPos pos, CollisionContext context, CallbackInfoReturnable<VoxelShape> cir) {
        if (this.unseen_world$darkFlag) {
            cir.setReturnValue(this.unseen_world$twin().getVisualShape(level, pos, context));
        }
    }

    @Inject(method = "getBlockSupportShape", at = @At("HEAD"), cancellable = true)
    private void unseen_world$twinSupport(BlockGetter level, BlockPos pos, CallbackInfoReturnable<VoxelShape> cir) {
        if (this.unseen_world$darkFlag) {
            cir.setReturnValue(this.unseen_world$twin().getBlockSupportShape(level, pos));
        }
    }

    @Inject(method = "getInteractionShape", at = @At("HEAD"), cancellable = true)
    private void unseen_world$twinInteraction(BlockGetter level, BlockPos pos, CallbackInfoReturnable<VoxelShape> cir) {
        if (this.unseen_world$darkFlag) {
            cir.setReturnValue(this.unseen_world$twin().getInteractionShape(level, pos));
        }
    }

    @Inject(method = "getOcclusionShape", at = @At("HEAD"), cancellable = true)
    private void unseen_world$twinOcclusion(BlockGetter level, BlockPos pos, CallbackInfoReturnable<VoxelShape> cir) {
        if (this.unseen_world$darkFlag) {
            cir.setReturnValue(this.unseen_world$twin().getOcclusionShape(level, pos));
        }
    }

    @Inject(method = "getFaceOcclusionShape", at = @At("HEAD"), cancellable = true)
    private void unseen_world$twinFaceOcclusion(BlockGetter level, BlockPos pos, Direction direction, CallbackInfoReturnable<VoxelShape> cir) {
        if (this.unseen_world$darkFlag) {
            cir.setReturnValue(this.unseen_world$twin().getFaceOcclusionShape(level, pos, direction));
        }
    }

    @Inject(method = "isSolidRender", at = @At("HEAD"), cancellable = true)
    private void unseen_world$twinSolidRender(BlockGetter level, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (this.unseen_world$darkFlag) {
            cir.setReturnValue(this.unseen_world$twin().isSolidRender(level, pos));
        }
    }

    @Inject(method = "isFaceSturdy(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;Lnet/minecraft/world/level/block/SupportType;)Z",
            at = @At("HEAD"), cancellable = true)
    private void unseen_world$twinSturdy(BlockGetter level, BlockPos pos, Direction direction, SupportType type, CallbackInfoReturnable<Boolean> cir) {
        if (this.unseen_world$darkFlag) {
            cir.setReturnValue(this.unseen_world$twin().isFaceSturdy(level, pos, direction, type));
        }
    }
}