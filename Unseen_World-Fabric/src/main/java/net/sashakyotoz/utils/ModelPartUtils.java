package net.sashakyotoz.utils;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.TexturedModelData;
import net.sashakyotoz.api.entity_data.IModelPartExtension;
import org.apache.commons.lang3.tuple.Triple;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ModelPartUtils {
    public static List<ModelPart> collectAllModelParts(TexturedModelData texturedData) {
        ModelPart root = texturedData.createModel();
        List<ModelPart> parts = new ArrayList<>();
        collectRecursive(root, parts);
        return parts;
    }

    @Nullable
    public static ModelPart getPart(Triple<Float, Float, Float> partSize, List<ModelPart> parts) {
        for (ModelPart part : parts) {
            if (((IModelPartExtension) (Object) part).findPartBySize(partSize.getLeft(), partSize.getMiddle(), partSize.getRight()).isPresent())
                return ((IModelPartExtension) (Object) part).findPartBySize(partSize.getLeft(), partSize.getMiddle(), partSize.getRight()).get();
        }
        return parts.get(0);
    }

    private static void collectRecursive(ModelPart current, List<ModelPart> out) {
        out.add(current);
        for (ModelPart child : current.children.values())
            collectRecursive(child, out);
    }
}