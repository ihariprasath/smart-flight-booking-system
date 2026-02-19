package com.ey.pricing_service.client;

import com.ey.pricing_service.dto.JourneyResponse;
import com.ey.pricing_service.dto.JourneySeatResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "journey-service")
public interface JourneyClient {

    @GetMapping("/journeys/internal/{id}")
    JourneyResponse getJourney(@PathVariable Long id);

    @GetMapping("/journeys/{id}/seats")
    List<JourneySeatResponse> getSeats(@PathVariable Long id);
}
