package searchengine.controllers;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.jsoup.nodes.Element;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import searchengine.dto.statistics.StatisticsResponse;
import searchengine.model.EnumForTable;
import searchengine.model.Site;
import searchengine.repository.PageRepository;
import searchengine.repository.SiteRepository;
import searchengine.services.StatisticsService;

import java.util.HashMap;

@RestController
@Configuration
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiController {

    private final StatisticsService statisticsService;
    private final SiteRepository siteRepository;
    private final PageRepository pageRepository;


    @GetMapping("/statistics")
    public ResponseEntity<StatisticsResponse> statistics() {
        return ResponseEntity.ok(statisticsService.getStatistics());
    }

    @GetMapping("/startIndexing")
    public HashMap<String, Boolean> startIndexing(){
        String url = "";
        HashMap<String, Boolean> arraySite = new HashMap<>();

        return arraySite;
    }


}

