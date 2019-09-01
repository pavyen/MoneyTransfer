package com.home.task.moneytransfer.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * Abstract Controller to unify routs initialization and initialize ObjectMapper with JavaTimeModule.
 */
public abstract class AbstractController {

    private final ObjectMapper mapper;

    AbstractController() {
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JavaTimeModule());
    }

    protected ObjectMapper getMapper() {
        return mapper;
    }

    /**
     * Initialization routs.
     */
    public abstract void initRouts();
}
