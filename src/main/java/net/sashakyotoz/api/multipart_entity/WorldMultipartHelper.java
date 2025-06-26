package net.sashakyotoz.api.multipart_entity;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;

import java.util.Collection;

public interface WorldMultipartHelper {
    default Collection<EntityPart> getPMEParts() {
        return getPMEPartMap().values();
    }

    Int2ObjectMap<EntityPart> getPMEPartMap();
}
