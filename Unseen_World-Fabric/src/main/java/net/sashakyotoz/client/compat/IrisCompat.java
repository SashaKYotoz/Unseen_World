package net.sashakyotoz.client.compat;

import net.fabricmc.loader.api.FabricLoader;
import net.sashakyotoz.UnseenWorld;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public final class IrisCompat {
    private static final boolean LOADED = FabricLoader.getInstance().isModLoaded("iris");
    private static final String[] VANILLA_PIPELINE_CLASSES = {
            "net.irisshaders.iris.pipeline.VanillaRenderingPipeline",
            "net.irisshaders.iris.pipeline.FixedFunctionWorldRenderingPipeline"
    };
    private static final MethodHandle SHADER_PACK_IN_USE;
    private static final MethodHandle PIPELINE_MANAGER;
    private static final MethodHandle PIPELINE;
    private static final MethodHandle SET_PHASE;
    private static final Object CUSTOM_SKY;
    private static final Object NONE;
    private static final Class<?> VANILLA_PIPELINE;
    private static boolean phaseFailureLogged;
    private static boolean vanillaFailureLogged;

    static {
        MethodHandle shaderPackInUse = null;
        MethodHandle pipelineManager = null;
        MethodHandle pipeline = null;
        MethodHandle setPhase = null;
        Object customSky = null;
        Object none = null;
        Class<?> vanillaPipeline = null;
        if (LOADED) {
            MethodHandles.Lookup lookup = MethodHandles.publicLookup();
            try {
                Class<?> api = Class.forName("net.irisshaders.iris.api.v0.IrisApi");
                Object instance = api.getMethod("getInstance").invoke(null);
                shaderPackInUse = lookup.findVirtual(api, "isShaderPackInUse", MethodType.methodType(boolean.class)).bindTo(instance);
            } catch (ReflectiveOperationException | LinkageError exception) {
                UnseenWorld.log().warn("Iris API not available, shaderpack detection disabled: " + exception);
            }
            try {
                Class<?> iris = Class.forName("net.irisshaders.iris.Iris");
                Class<?> phase = Class.forName("net.irisshaders.iris.pipeline.WorldRenderingPhase");
                Class<?> renderingPipeline = Class.forName("net.irisshaders.iris.pipeline.WorldRenderingPipeline");
                pipelineManager = lookup.unreflect(iris.getMethod("getPipelineManager"));
                pipeline = lookup.unreflect(pipelineManager.type().returnType().getMethod("getPipelineNullable"));
                setPhase = lookup.findVirtual(renderingPipeline, "setPhase", MethodType.methodType(void.class, phase));
                customSky = constant(phase, "CUSTOM_SKY");
                none = constant(phase, "NONE");
            } catch (ReflectiveOperationException | LinkageError exception) {
                UnseenWorld.log().warn("Iris pipeline hooks not available: " + exception);
                pipelineManager = null;
                pipeline = null;
                setPhase = null;
                customSky = null;
                none = null;
            }
            for (String name : VANILLA_PIPELINE_CLASSES) {
                try {
                    vanillaPipeline = Class.forName(name);
                    break;
                } catch (ClassNotFoundException | LinkageError ignored) {
                    vanillaPipeline = null;
                }
            }
        }
        SHADER_PACK_IN_USE = shaderPackInUse;
        PIPELINE_MANAGER = pipelineManager;
        PIPELINE = pipeline;
        SET_PHASE = setPhase;
        CUSTOM_SKY = customSky;
        NONE = none;
        VANILLA_PIPELINE = vanillaPipeline;
    }

    private IrisCompat() {
    }

    public static boolean isShaderPackInUse() {
        if (SHADER_PACK_IN_USE == null) {
            return false;
        }
        try {
            return (boolean) SHADER_PACK_IN_USE.invoke();
        } catch (Throwable throwable) {
            return false;
        }
    }

    public static boolean isShaderPipelineActive() {
        if (!isShaderPackInUse()) {
            return false;
        }
        Object pipeline = currentPipeline();
        return pipeline == null || VANILLA_PIPELINE == null || !VANILLA_PIPELINE.isInstance(pipeline);
    }

    public static Object createVanillaPipeline() {
        if (VANILLA_PIPELINE == null) {
            logVanillaFailure("no vanilla pipeline class found in this Iris version");
            return null;
        }
        try {
            return VANILLA_PIPELINE.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException | LinkageError exception) {
            logVanillaFailure(exception.toString());
            return null;
        }
    }

    public static void beginCustomSky() {
        setPhase(CUSTOM_SKY);
    }

    public static void endCustomSky() {
        setPhase(NONE);
    }

    private static Object currentPipeline() {
        if (PIPELINE_MANAGER == null || PIPELINE == null) {
            return null;
        }
        try {
            return PIPELINE.invoke(PIPELINE_MANAGER.invoke());
        } catch (Throwable throwable) {
            return null;
        }
    }

    private static void setPhase(Object phase) {
        if (SET_PHASE == null || phase == null) {
            return;
        }
        Object pipeline = currentPipeline();
        if (pipeline == null) {
            return;
        }
        try {
            SET_PHASE.invoke(pipeline, phase);
        } catch (Throwable throwable) {
            if (!phaseFailureLogged) {
                phaseFailureLogged = true;
                UnseenWorld.log().warn("Failed to set Iris render phase: " + throwable);
            }
        }
    }

    private static void logVanillaFailure(String reason) {
        if (!vanillaFailureLogged) {
            vanillaFailureLogged = true;
            UnseenWorld.log().warn("Could not create Iris vanilla pipeline, shaderpack stays active in Chimeric Darkness: " + reason);
        }
    }

    private static Object constant(Class<?> enumClass, String name) throws NoSuchFieldException {
        for (Object value : enumClass.getEnumConstants()) {
            if (((Enum<?>) value).name().equals(name)) {
                return value;
            }
        }
        throw new NoSuchFieldException(name);
    }
}