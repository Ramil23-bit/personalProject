package searchengine.indexingSite;

import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import searchengine.model.EnumForTable;
import searchengine.model.Page;
import searchengine.model.Site;
import searchengine.repository.PageRepository;
import searchengine.repository.SiteRepository;

import java.io.IOException;
import java.time.Instant;

@Configuration
@RequiredArgsConstructor
public class IndexingSite {
    private final SiteRepository siteRepository;
    private final PageRepository pageRepository;

    //I am creating a new record in a table SITE
    public void createEntry(){
        Site site = new Site();
        site.setStatus(EnumForTable.INDEXING);
        siteRepository.save(site);
    }

    // I need help!!!
    public void createDataInTablePage(String url){

        Site site = new Site();
        try {
            if (!roundSite(url)) {
                site.setStatus(EnumForTable.FAILED);
            }
        } catch (IOException e) {
            site.setLast_error(e.getMessage());
        }
    }

    //I am deleting data from Tables site and page
    public void deleteSite(String url){
        try {
            if(url.isEmpty()) {
                siteRepository.deleteAll();
                pageRepository.deleteAll();
            }
        }catch (RuntimeException e){
            e.getMessage();
        }
    }

    private boolean roundSite(String url) throws IOException {
        Element elementSite = new Element(url);
        Document doc = Jsoup.connect(url).get();
        String title = doc.title();
        Site site = new Site();
        Page page = new Page();
        System.out.printf("Execute task on thread %s%n", Thread.currentThread());
        String attribute = doc.attr("href");
        if (attribute.startsWith(url)) {
            page.setContent(String.valueOf(attribute.replaceFirst("https://", "").split("/").length));
            site.setStatus_time(Instant.now());
            page.setPath(attribute);
            site.setStatus(EnumForTable.INDEXING);
            return false;
        }
        return true;


    }
}
