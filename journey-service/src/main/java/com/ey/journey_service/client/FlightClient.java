package com.ey.journey_service.client;

import com.ey.journey_service.dto.FlightResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
@FeignClient(name = "flight-service")
public interface FlightClient {

    @GetMapping("/flights/{id}")
    FlightResponse getById(@PathVariable Long id);
}
