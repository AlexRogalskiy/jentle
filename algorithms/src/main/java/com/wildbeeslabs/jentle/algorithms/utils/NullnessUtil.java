package com.wildbeeslabs.jentle.algorithms.utils;

import java.util.Objects;

/**
 * Utility class for the Nullness Checker.
 *
 * <p>To avoid the need to write the NullnessUtil class name, do:
 *
 * <pre>import static org.checkerframework.checker.nullness.NullnessUtil.castNonNull;</pre>
 * <p>
 * or
 *
 * <pre>import static org.checkerframework.checker.nullness.NullnessUtil.*;</pre>
 *
 * <p><b>Runtime Dependency</b>
 *
 * <p>Please note that using this class introduces a runtime dependency. This means that you need to
 * distribute (or link to) the Checker Framework, along with your binaries.
 *
 * <p>To eliminate this dependency, you can simply copy this class into your own project.
 */
// Nullness utilities are trusted regarding nullness.
// Casts look redundant if Nullness Checker is not run.
@SuppressWarnings({"nullness", "cast"})
public final class NullnessUtil {

    private NullnessUtil() {
        throw new AssertionError("shouldn't be instantiated");
    }

    /**
     * A method that suppresses warnings from the Nullness Checker.
     *
     * <p>The method takes a possibly-null reference, unsafely casts it to have the @NonNull type
     * qualifier, and returns it. The Nullness Checker considers both the return value, and also the
     * argument, to be non-null after the method call. Therefore, the {@code castNonNull} method can
     * be used either as a cast expression or as a statement. The Nullness Checker issues no
     * warnings in any of the following code:
     *
     * <pre><code>
     *   // one way to use as a cast:
     *  {@literal @}NonNull String s = castNonNull(possiblyNull1);
     *
     *   // another way to use as a cast:
     *   castNonNull(possiblyNull2).toString();
     *
     *   // one way to use as a statement:
     *   castNonNull(possiblyNull3);
     *   possiblyNull3.toString();`
     * }</code></pre>
     * <p>
     * The {@code castNonNull} method is intended to be used in situations where the programmer
     * definitively knows that a given reference is not null, but the type system is unable to make
     * this deduction. It is not intended for defensive programming, in which a programmer cannot
     * prove that the value is not null but wishes to have an earlier indication if it is. See the
     * Checker Framework Manual for further discussion.
     *
     * <p>The method throws {@link AssertionError} if Java assertions are enabled and the argument
     * is {@code null}. If the exception is ever thrown, then that indicates that the programmer
     * misused the method by using it in a circumstance where its argument can be null.
     *
     * @param ref a reference of @Nullable type
     * @return the argument, casted to have the type qualifier @NonNull
     */
    public static <T extends Object> @lombok.NonNull T castNonNull(final T ref) {
        assert Objects.nonNull(ref) : "Misuse of castNonNull: called with a null argument";
        return ref;
    }

    /**
     * Like castNonNull, but whereas that method only checks and casts the reference itself, this
     * traverses all levels of the argument array. The array is recursively checked to ensure that
     * all elements at every array level are non-null.
     *
     * @see #castNonNull(Object)
     */
    public static <T extends Object> @lombok.NonNull T @lombok.NonNull [] castNonNullDeep(final T[] arr) {
        return castNonNullArray(arr);
    }

    /**
     * Like castNonNull, but whereas that method only checks and casts the reference itself, this
     * traverses all levels of the argument array. The array is recursively checked to ensure that
     * all elements at every array level are non-null.
     *
     * @see #castNonNull(Object)
     */
    public static <T extends Object> @lombok.NonNull T @lombok.NonNull [][] castNonNullDeep(final T[][] arr) {
        return castNonNullArray(arr);
    }

    /**
     * Like castNonNull, but whereas that method only checks and casts the reference itself, this
     * traverses all levels of the argument array. The array is recursively checked to ensure that
     * all elements at every array level are non-null.
     *
     * @see #castNonNull(Object)
     */
    public static <T extends Object> @lombok.NonNull T @lombok.NonNull [][][] castNonNullDeep(final T[][][] arr) {
        return castNonNullArray(arr);
    }

    /**
     * Like castNonNull, but whereas that method only checks and casts the reference itself, this
     * traverses all levels of the argument array. The array is recursively checked to ensure that
     * all elements at every array level are non-null.
     *
     * @see #castNonNull(Object)
     */
    public static <T extends Object> @lombok.NonNull T @lombok.NonNull [][][][] castNonNullDeep(final T[][][][] arr) {
        return castNonNullArray(arr);
    }

    /**
     * Like castNonNull, but whereas that method only checks and casts the reference itself, this
     * traverses all levels of the argument array. The array is recursively checked to ensure that
     * all elements at every array level are non-null.
     *
     * @see #castNonNull(Object)
     */
    public static <T extends Object> @lombok.NonNull T @lombok.NonNull [][][][][] castNonNullDeep(final T[][][][][] arr) {
        return castNonNullArray(arr);
    }

    private static <T extends Object> @lombok.NonNull T @lombok.NonNull [] castNonNullArray(final T[] arr) {
        assert Objects.nonNull(arr) : "Misuse of castNonNullArray: called with a null array argument";
        for (int i = 0; i < arr.length; ++i) {
            assert Objects.nonNull(arr[i]) : "Misuse of castNonNull: called with a null array element";
            checkIfArray(arr[i]);
        }
        return arr;
    }

    private static void checkIfArray(final @lombok.NonNull Object ref) {
        assert Objects.nonNull(ref) : "Misuse of checkIfArray: called with a null argument";
        final Class<?> comp = ref.getClass().getComponentType();
        if (Objects.nonNull(comp)) {
            if (comp.isPrimitive()) {
            } else {
                castNonNullArray((Object[]) ref);
            }
        }
    }
}
