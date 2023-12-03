package searchengine.indexingSite;

import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import searchengine.model.EnumForTable;
import searchengine.model.Page;
import searchengine.config.*;
import searchengine.model.Site;
import searchengine.repository.PageRepository;
import searchengine.repository.SiteRepository;

import java.io.IOException;
import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class IndexingSite {
    private final SiteRepository siteRepository;
    private final PageRepository pageRepository;
    private final Element elementSite;



    //I am creating a new record in a table SITE
    public void createEntry(){
        Site site = new Site();
        siteRepository.save(site);
        site.setStatus(EnumForTable.INDEXING);
    }

    // I need help!!!
    public void createDataInTablePage(String url) throws IOException {
        roundSite(url);
        Page page = new Page();
        Site site = new Site();
        site.setStatus(EnumForTable.INDEXED);
    }

    //I am deleting data from Tables site and page
    public void deleteSite(String url){
        if(url.isEmpty()){
            siteRepository.deleteAll();
            pageRepository.deleteAll();
        }
    }

    private boolean roundSite(String url) throws IOException {
        Document doc = Jsoup.connect("").get();
        String title = doc.title();
        Site site = new Site();
        Page page = new Page();
        Element element = null;
        System.out.printf("Execute task on thread %s%n", Thread.currentThread());
        String attribute = element.attr("href");
        if (attribute.startsWith("https://www.coursera.org/")) {
            site.setStatus_time(Instant.now());
            page.setPath(attribute);
            page.setContent(String.valueOf(attribute.replaceFirst("https://", "").split("/").length));
            return Map.of(attribute, attribute.replaceFirst("https://", "").split("/").length).isEmpty();
        }
        return Collections.emptyMap().isEmpty();


    }
}
