package com.hust.smart_Shopping.dtos.user;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class TimeResponseDTO {
    @JsonFormat(pattern = "MM/dd/yyyy HH:mm:ss", timezone = "UTC")
    public Instant createdAt;
    @JsonFormat(pattern = "MM/dd/yyyy HH:mm:ss", timezone = "UTC")
    public Instant updatedAt;
}
