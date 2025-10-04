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

                // Method Not Found
                if (ex instanceof jakarta.ws.rs.NotFoundException nf) {
                        errorResponse = new ErrorResponse(
                                        Response.Status.NOT_FOUND.getStatusCode(),
                                        "Method Not Found",
                                        Collections.singletonList(nf.getMessage()));
                        return Response.status(Response.Status.NOT_FOUND)
                                        .type(MediaType.APPLICATION_JSON)
                                        .entity(errorResponse)
                                        .build();
                }

                if (ex instanceof jakarta.ws.rs.NotAllowedException na) {
                        errorResponse = new ErrorResponse(
                                        Response.Status.METHOD_NOT_ALLOWED.getStatusCode(),
                                        "Method Not Allowed",
                                        Collections.singletonList(na.getMessage()));
                        return Response.status(Response.Status.METHOD_NOT_ALLOWED)
                                        .type(MediaType.APPLICATION_JSON)
                                        .entity(errorResponse)
                                        .build();
                }

                // Default: log and return 500
                BaseLogger.LOG.error("Processing Failed.", ex);

                errorResponse = new ErrorResponse(
                                Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                                "Internal server error",
                                Collections.singletonList("Processing Failed"));

                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                                .type(MediaType.APPLICATION_JSON)
                                .entity(errorResponse)
                                .build();
        }
}
