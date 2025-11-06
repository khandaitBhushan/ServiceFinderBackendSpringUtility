package com.org.ServiceFinder.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDTO {
    private Long id;
    private Long userId;
    private Long providerId;
    private LocalDateTime date;
    private String status;
}