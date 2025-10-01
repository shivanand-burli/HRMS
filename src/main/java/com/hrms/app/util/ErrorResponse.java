package com.hrms.app.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    private int code; // HTTP status code
    private String message; // General error message
    private List<String> details; // Specific validation or exception details
}
