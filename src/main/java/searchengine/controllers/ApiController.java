package searchengine.controllers;

import lombok.AllArgsConstructor;
import org.jsoup.nodes.Element;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import searchengine.dto.statistics.StatisticsResponse;
import searchengine.model.EnumForTable;
import searchengine.model.Site;
import searchengine.repository.PageRepository;
import searchengine.repository.SiteRepository;
import searchengine.services.StatisticsService;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class ApiController {

    private final StatisticsService statisticsService;
    private final SiteRepository siteRepository;
    private final PageRepository pageRepository;

    private final Element elementSite;

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

        //обход страниц
        roundSite(elementSite);


        //меняем статус по завершению обхода и выдаем правильную ошибку
        site.setStatus(EnumForTable.INDEXED);
        if(!(roundSite(elementSite))){
            site.setStatus(EnumForTable.FAILED);
            // как сделать чтобы каждая ситуация с ошибкой выдавала правильное сообщение об ошибке?
            site.setLast_error();
        }



        return arraySite;
    }
/*
    я вынес обход страниц в отдельный метод, но не пойму как сохранить полученные данные в таблицу site?
    я не до конца уверен что этот код верный, посмотрите пожалуйста его
 */
    private boolean roundSite(Element element){
        System.out.println(String.format("Execute task on thread %s", Thread.currentThread()));
        String attribute = element.attr("href");
        return !attribute.startsWith("http://www.playback.ru/");
    }


}

/*
    @UpdateTimestamp

    @Override
    protected Map<String, Integer> compute() {
        return parse(lines);
    }

    private Map<String, Integer> parse(Element lines) {
        System.out.println(String.format("Execute task on thread %s", Thread.currentThread()));
        String attribute = lines.attr("href");
        if (attribute.startsWith("https://www.coursera.org/")) {
            return Map.of(attribute, attribute.replaceFirst("https://", "").split("/").length);
        }
        return Collections.emptyMap();
    }

 */
