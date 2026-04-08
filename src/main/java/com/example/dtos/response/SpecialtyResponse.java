package com.example.dtos.response;

import java.util.List;

public record SpecialtyResponse(
        Long id,
        String name,
        List<GroupResponse> groups
) {}
