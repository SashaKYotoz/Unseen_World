package net.sashakyotoz.api.entity_data;

import net.minecraft.client.model.ModelPart;

import java.util.Optional;

public interface IModelPartExtension {
    Optional<ModelPart> findPartByName(String name);

    Optional<ModelPart> findPartBySize(float x, float y, float z);
}