package com.sharkhendrix.util;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * <p>
 * This class is a purpose of a simple and abstract implementation of the
 * factory pattern. The goal is to provide pre-built factories that can be
 * easily modified / extended by users.
 * <p>
 * Given a parameter P, the factory will test different cases and take the
 * appropriate builder to produce the result R. If none of the cases is
 * satisfied for a given parameter, the default builder is used.
 * <p>
 * This is recommended to consider the order of the test of the cases
 * undetermined, and so make exclusive case conditions. In this way, removing or
 * adding cases does not affect each other.
 * 
 * @author Joannick Gardize
 *
 * @param <P> the parameter type, used to test cases and build the result
 * @param <R> the result type of this factory
 */
public class ConditionalFactory<P, R> {

    private static class Case<P, T> {

        Predicate<P> condition;
        Function<P, T> builder;

        public Case(Predicate<P> condition, Function<P, T> factory) {
            this.condition = condition;
            this.builder = factory;
        }
    }

    private Map<String, Case<P, R>> cases = new LinkedHashMap<>();
    private Function<P, R> defaultBuilder;

    /**
     * Build a value using the given parameter. This will uses the builder of any
     * case matching the given parameter, or uses the default builder if no case
     * matches.
     * 
     * @param parameter the parameter to use
     * @return the built value
     */
    public R build(P parameter) {
        for (Case<P, R> currentCase : cases.values()) {
            if (currentCase.condition.test(parameter)) {
                return currentCase.builder.apply(parameter);
            }
        }
        return defaultBuilder.apply(parameter);
    }

    /**
     * Add a case to this factory.
     * 
     * @param caseName  the name of the case
     * @param condition the condition of the case
     * @param builder   the builder to use when the condition matches
     */
    public void addCase(String caseName, Predicate<P> condition, Function<P, R> builder) {
        cases.put(caseName, new Case<>(condition, builder));
    }

    /**
     * Remove a case from this factory. If no case correspond to the given name,
     * nothing happens.
     * 
     * @param caseName the name of the case to remove
     */
    public void removeCase(String caseName) {
        cases.remove(caseName);
    }

    /**
     * The default builder to use when no case matches to a parameter. This must be
     * defined before any call to {@link #build(Object)}, or a
     * {@code NullPointerException} could occurs if no case matches for a given
     * parameter.
     * 
     * @param defaultBuilder the default builder to use when no case matches to a
     *                       parameter
     */
    public void setDefaultBuilder(Function<P, R> defaultBuilder) {
        this.defaultBuilder = defaultBuilder;
    }
}
