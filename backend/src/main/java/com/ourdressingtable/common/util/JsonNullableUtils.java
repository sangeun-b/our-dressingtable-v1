package com.ourdressingtable.common.util;

import java.util.function.Consumer;
import org.openapitools.jackson.nullable.JsonNullable;

public final class JsonNullableUtils {

    private JsonNullableUtils() {}

    /** 요청에서 아예 빠진 상태인지 (undefined) */
    public static boolean isUndefined(JsonNullable<?> c) {
        return c == null || c == JsonNullable.undefined();
    }

    /** 명시적으로 null이 들어온 상태인지 (삭제 의도) */
    public static boolean isExplicitNull(JsonNullable<?> c) {
        return c != null && c != JsonNullable.undefined() && !c.isPresent();
    }

    /**
     * 3-state 적용:
     * - undefined(미전달)  -> 유지
     * - present(값 전달)   -> presentSetter.accept(value)
     * - explicit null      -> nullSetter.run()
     */

    public static <T> void applyTriState(
            JsonNullable<T> candidate,
            Consumer<T> presentSetter,
            Runnable nullSetter
    ) {
        if(isUndefined(candidate)) return; // 유지
        if(candidate.isPresent()) {
            presentSetter.accept(candidate.get()); // 갱신
        } else {
            nullSetter.run(); // 삭제
        }
    }




}
