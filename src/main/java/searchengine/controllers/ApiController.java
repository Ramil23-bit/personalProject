package searchengine.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import searchengine.dto.statistics.StatisticsResponse;
import searchengine.model.EnumForTable;
import searchengine.model.Page;
import searchengine.model.Site;
import searchengine.repository.PageRepository;
import searchengine.repository.SiteRepository;
import searchengine.services.StatisticsService;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class ApiController {

    private final StatisticsService statisticsService;
    private final SiteRepository siteRepository;
    private final PageRepository pageRepository;

    @GetMapping("/statistics")
    public ResponseEntity<StatisticsResponse> statistics() {
        return ResponseEntity.ok(statisticsService.getStatistics());
    }

    @GetMapping("/startIndexing")
    HashMap<String, Boolean> startIndexing(){
        String url = "";
        HashMap<String, Boolean> arraySite = new HashMap<>();
        //удаляем все данные из site and page
        if(url.isEmpty()){
            siteRepository.deleteAll();
            pageRepository.deleteAll();
        }
        //создаем новую запись в таблице site
        Site site = new Site();
        site.setStatus(EnumForTable.INDEXING);
        siteRepository.save(site);
        arraySite.put(String.valueOf(site), true);

        return arraySite;
    }
}
