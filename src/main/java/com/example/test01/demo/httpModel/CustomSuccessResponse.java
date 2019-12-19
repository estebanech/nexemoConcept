package com.example.test01.demo.httpModel;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CustomSuccessResponse<T> implements CustomResponse<T> {
    private T data;

    @Builder.Default
    private boolean success = false;

    public static <T> CustomSuccessResponse<T> success(final T response){
        return CustomSuccessResponse.<T>builder()
                .data(response)
                .success(true)
                .build();
    }
}
