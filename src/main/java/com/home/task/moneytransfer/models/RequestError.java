package com.home.task.moneytransfer.models;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *  Object to wrap operations errors.
 */
@Data
@AllArgsConstructor
public class RequestError {
    private String message;
}
