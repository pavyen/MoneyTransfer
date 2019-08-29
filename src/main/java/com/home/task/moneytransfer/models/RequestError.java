package com.home.task.moneytransfer.models;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RequestError {
    private String message;
}
