package com.example.test01.demo.httpModel;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomErrorResponse <T> implements CustomResponse<T> {
    private T data;

    @Builder.Default
    private boolean success = false;

    private String errorMessage;

    public static <T> CustomErrorResponse <T> fail(String message, Class<T> clazz){
        return CustomErrorResponse.
                <T>builder()
                .errorMessage(message)
                .build();
    }
}
