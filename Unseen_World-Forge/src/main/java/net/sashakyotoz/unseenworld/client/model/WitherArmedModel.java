package net.sashakyotoz.unseenworld.client.model;

import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.sashakyotoz.unseenworld.entity.TheWitherKnightEntity;

import java.util.function.Function;

public abstract class WitherArmedModel<E extends TheWitherKnightEntity> extends HierarchicalModel<E> implements ArmedModel {
    public WitherArmedModel(){
        this(RenderType::entityTranslucent);
    }
    public WitherArmedModel(Function<ResourceLocation, RenderType> function) {
        super(function);
    }
}
