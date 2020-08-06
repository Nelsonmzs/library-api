package com.nelson.libraryapi.api.exception;

import com.nelson.libraryapi.exception.BusinessException;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ApiErrors {

    List<String> errors;

    public ApiErrors(BindingResult bindResult) {

        this.errors = new ArrayList<>();

        bindResult.getAllErrors().forEach(error -> this.errors.add(error.getDefaultMessage()));

    }

    public ApiErrors(BusinessException exc) {

        this.errors = Collections.singletonList(exc.getMessage());
    }

    public List<String> getErrors() {
        return errors;
    }
}
