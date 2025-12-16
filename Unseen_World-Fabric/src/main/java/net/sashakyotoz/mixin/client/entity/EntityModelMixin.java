package net.sashakyotoz.mixin.client.entity;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.Entity;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.api.entity_data.IModelPartsAccessor;
import net.sashakyotoz.utils.ModelPartUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

@Mixin(EntityModel.class)
public class EntityModelMixin<T extends Entity> implements IModelPartsAccessor {
    @Unique
    private List<ModelPart> modelParts;
    @Unique
    private boolean triedCollect = false;

    @Override
    public List<ModelPart> getAllModelParts() {
        if (!triedCollect) {
            triedCollect = true;
            UnseenWorld.log("[ModelParts] Start collecting for %s".formatted(this.getClass().getSimpleName()));
            collectParts();
            UnseenWorld.log("[ModelParts] Done. Path=%s, count=%d".formatted(
                    (modelParts != null && !modelParts.isEmpty()) ? (usedStatic ? ("static:" + staticMethodName + "()") : "fields") : "NONE",
                    modelParts == null ? 0 : modelParts.size()));
        }
        return modelParts != null ? modelParts : List.of();
    }

    @Unique
    private boolean usedStatic;
    @Unique
    private String staticMethodName;

    @Unique
    private void collectParts() {
        Class<?> cls = this.getClass();
        for (Method m : cls.getDeclaredMethods()) {
            if (Modifier.isStatic(m.getModifiers())
                    && m.getParameterCount() == 0
                    && TexturedModelData.class.isAssignableFrom(m.getReturnType())) {
                try {
                    m.setAccessible(true);
                    TexturedModelData data = (TexturedModelData) m.invoke(null);
                    modelParts = ModelPartUtils.collectAllModelParts(data);
                    usedStatic = true;
                    staticMethodName = m.getName();
                    return;
                } catch (IllegalAccessException | InvocationTargetException ignored) {}
            }
        }
        usedStatic = false;
        staticMethodName = null;
        modelParts = new ArrayList<>();
        Class<?> walk = cls;
        while (walk != null && EntityModel.class.isAssignableFrom(walk)) {
            for (Field f : walk.getDeclaredFields()) {
                if (ModelPart.class.isAssignableFrom(f.getType())) {
                    try {
                        f.setAccessible(true);
                        ModelPart p = (ModelPart) f.get(this);
                        if (p != null && !modelParts.contains(p)) {
                            modelParts.add(p);
                        }
                    } catch (IllegalAccessException ignored) {
                    }
                }
            }
            walk = walk.getSuperclass();
        }
    }
}