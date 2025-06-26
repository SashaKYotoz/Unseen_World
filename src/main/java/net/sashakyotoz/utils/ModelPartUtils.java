package net.sashakyotoz.utils;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.TexturedModelData;

import java.util.ArrayList;
import java.util.List;

public class ModelPartUtils {
    public static List<ModelPart> collectAllModelParts(TexturedModelData texturedData) {
        ModelPart root = texturedData.createModel();
        List<ModelPart> parts = new ArrayList<>();
        collectRecursive(root, parts);
        return parts;
    }

    private static void collectRecursive(ModelPart current, List<ModelPart> out) {
        out.add(current);
        for (ModelPart child : current.children.values())
            collectRecursive(child, out);
    }
}