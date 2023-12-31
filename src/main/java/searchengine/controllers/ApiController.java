package searchengine.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import searchengine.dto.statistics.StatisticsResponse;
import searchengine.dto.statistics.StatisticsSiteResponse;
import searchengine.repository.PageRepository;
import searchengine.repository.SiteRepository;
import searchengine.services.StatisticSiteServiceImpl;
import searchengine.services.StatisticsService;

import java.io.IOException;

@RestController
@Configuration
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiController {

    private final StatisticsService statisticsService;
    private final SiteRepository siteRepository;
    private final PageRepository pageRepository;
    private final StatisticSiteServiceImpl siteServiceImpl;
    @GetMapping("/statistics")
    public ResponseEntity<StatisticsResponse> statistics() {
        return ResponseEntity.ok(statisticsService.getStatistics());
    }

    @GetMapping("/startIndexing")
    public ResponseEntity<StatisticsSiteResponse> startIndexing() throws IOException {
        return ResponseEntity.ok(siteServiceImpl.roundSites());
    }

    @GetMapping("/stopIndexing")
    public ResponseEntity<StatisticsSiteResponse> stopIndexing(){
        return ResponseEntity.ok(siteServiceImpl.stopIndexing());
    }

    @PostMapping("/indexPage")
    public ResponseEntity<StatisticsSiteResponse> indexPage(String url) throws IOException {
        return ResponseEntity.ok(siteServiceImpl.addPageToIndex("https://dimonvideo.ru/"));
    }

}

