package com.ey.search_service.client;

import com.ey.search_service.dto.JourneyResponse;
import com.ey.search_service.dto.SearchResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
@FeignClient(name = "journey-service")
public interface JourneyClient {

    @GetMapping("/journeys/search")
    public List<JourneyResponse> searchJourneys(
            @RequestParam String source,
            @RequestParam String destination,
            @RequestParam String date
    );
}
