package net.sashakyotoz.common.registry;

import net.lcc.sollib.api.common.registry.SHolder;
import net.lcc.sollib.api.common.registry.SolModContainer;
import net.lcc.sollib.api.common.registry.SolRegistrar;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class OrderedSolModContainer extends SolModContainer {
    public OrderedSolModContainer(String name, String namespace) {
        super(name, namespace);
    }

    @Override
    public <T, H extends SHolder<T>> SolRegistrar<T, H> getRegistrar(Class<H> clazz) {
        if (!this.registrars.containsKey(clazz)) {
            this.registrars.put(clazz, new OrderedRegistrar<T, H>(this, clazz));
        }
        return super.getRegistrar(clazz);
    }

    public static class OrderedRegistrar<T, H extends SHolder<T>> extends SolRegistrar<T, H> {
        private final List<H> order = new ArrayList<>();

        public OrderedRegistrar(SolModContainer mod, Class<H> clazz) {
            super(mod, clazz);
        }

        @Override
        public H register(String name, Supplier<T> supplier) {
            H holder = super.register(name, supplier);
            if (this.get(name) == holder) {
                this.order.add(holder);
            }
            return holder;
        }

        @Override
        public void iterate(Consumer<H> consumer) {
            for (H holder : List.copyOf(this.order)) {
                consumer.accept(holder);
            }
        }

        @Override
        public H find(Predicate<H> predicate) {
            for (H holder : List.copyOf(this.order)) {
                if (predicate.test(holder)) {
                    return holder;
                }
            }
            return null;
        }
    }
}
