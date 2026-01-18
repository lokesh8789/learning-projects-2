package com.sse.util;

import java.util.function.Consumer;
import java.util.function.Function;

public final class Try {

    private Try() {}

    @FunctionalInterface
    public interface ThrowingSupplier<T> {
        T get() throws Exception;
    }

    @FunctionalInterface
    public interface ThrowingRunnable {
        void run() throws Exception;
    }

    @FunctionalInterface
    public interface ThrowingConsumer<T> {
        void accept(T t) throws Exception;
    }

    public static <T> T withException(ThrowingSupplier<T> supplier) {
        try {
            return supplier.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void withException(ThrowingRunnable runnable) {
        try {
            runnable.run();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T withException(
            ThrowingSupplier<T> supplier,
            Function<Exception, T> mapper) {
        try {
            return supplier.get();
        } catch (Exception e) {
            return mapper.apply(e);
        }
    }

    public static void withException(
            ThrowingRunnable action,
            Consumer<Exception> handler) {
        try {
            action.run();
        } catch (Exception e) {
            handler.accept(e);
        }
    }

    public static <T extends AutoCloseable, U> U withResource(
            ThrowingSupplier<T> supplier,
            Function<T, U> mapper) {
        try(T resource = supplier.get()) {
            return mapper.apply(resource);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T extends AutoCloseable> void withResource(
            ThrowingSupplier<T> supplier,
            ThrowingConsumer<T> consumer) {

        try (T resource = supplier.get()) {
            consumer.accept(resource);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

