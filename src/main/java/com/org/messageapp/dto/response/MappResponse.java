package com.org.messageapp.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MappResponse<T> {

    private String message;
    private String token;
    private T data;
}
