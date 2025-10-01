package com.hrms.app.config;

import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.hrms.app.util.ErrorResponse;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Exception> {

        @Override
        public Response toResponse(Exception ex) {
                ErrorResponse errorResponse;

                // Handle custom validation error
                if (ex instanceof ValidationError ve) {
                        errorResponse = new ErrorResponse(
                                        Response.Status.BAD_REQUEST.getStatusCode(),
                                        "Validation failed",
                                        Collections.singletonList(ve.getMessage()));
                        return Response.status(Response.Status.BAD_REQUEST)
                                        .type(MediaType.APPLICATION_JSON)
                                        .entity(errorResponse)
                                        .build();
                }

                // Handle bean validation constraints (Jakarta Validation)
                if (ex instanceof ConstraintViolationException cve) {
                        List<String> violations = cve.getConstraintViolations()
                                        .stream()
                                        .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                                        .collect(Collectors.toList());

                        errorResponse = new ErrorResponse(
                                        Response.Status.BAD_REQUEST.getStatusCode(),
                                        "Validation failed",
                                        violations);
                        return Response.status(Response.Status.BAD_REQUEST)
                                        .type(MediaType.APPLICATION_JSON)
                                        .entity(errorResponse)
                                        .build();
                }

                // Default: log and return 500
                BaseLogger.LOG.error("API processing failed.", ex);

                errorResponse = new ErrorResponse(
                                Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                                "Internal server error",
                                Collections.singletonList("API processing failed"));

                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                                .type(MediaType.APPLICATION_JSON)
                                .entity(errorResponse)
                                .build();
        }
}
