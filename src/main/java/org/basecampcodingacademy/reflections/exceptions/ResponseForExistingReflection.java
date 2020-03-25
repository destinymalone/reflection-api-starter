package org.basecampcodingacademy.reflections.exceptions;

import org.basecampcodingacademy.reflections.domain.Response;

import java.time.LocalDate;

public class ResponseForExistingReflection extends Exception {
    public Integer reflectionId;
    public ResponseForExistingReflection(Integer reflectionId) {
        this.reflectionId = reflectionId;
    }
}
