package com.example.dtos.response;

public record GroupResponse(
        Long id,
        String groupNumber,
        Long specialtyId,
        String specialtyName
) {}
