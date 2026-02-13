package com.ey.search_service.controller;


import com.ey.search_service.dto.SearchResponse;
import com.ey.search_service.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService service;

    @GetMapping
    public List<SearchResponse> search(
            @RequestParam String source,
            @RequestParam String destination,
            @RequestParam String date,
            @RequestParam int passengers){
        return service.search(source, destination,date,passengers);
    }

}
