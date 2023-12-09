package searchengine.services;

import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;
import searchengine.model.EnumForTable;
import searchengine.model.Page;
import searchengine.model.Site;
import searchengine.repository.PageRepository;
import searchengine.repository.SiteRepository;

import javax.transaction.Transactional;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class StatisticSiteServiceImpl extends RuntimeException {
    private final SiteRepository siteRepository;
    private final PageRepository pageRepository;

    public void createEntry() {
        Site site = new Site();
        site.setStatus(EnumForTable.INDEXING);
        siteRepository.save(site);
    }

    public void createDataInTablePage(String url) {

        if (!roundSite(url)) {
            throw new IllegalArgumentException("Data entered incorrectly");
        }
    }

    @Transactional
    public void deleteSite(String url) {
        try {
            if (url.isEmpty()) {
                siteRepository.deleteAll();
                pageRepository.deleteAll();
            }
        } catch (RuntimeException e) {
            e.getMessage();
        }
    }

    public void connectionSite(String url){
        Document doc = null;

        try {
            doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 6.2;) AppleWebKit/602.40 " +
                            "(KHTML, like Gecko) Chrome/52.0.1538.324 Safari/602")
                    .referrer("http://www.google.com")
                    .get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        String title = doc.title();
    }

    private boolean roundSite(String url) {
        Element elementSite = new Element(url);

        Site site = new Site();
        Page page = new Page();
        System.out.printf("Execute task on thread %s%n", Thread.currentThread());
        String attribute = elementSite.attr("href");

        if (attribute.startsWith(url)) {
            try {
                page.setContent(String.valueOf(attribute.replaceFirst("https://", "").split("/").length));
                site.setStatus_time(Instant.now());
                page.setPath(attribute);
                if(!page.getContent().isEmpty()){
                    siteRepository.save(site);
                    pageRepository.save(page);
                }
            } catch (Exception e) {
                site.setStatus(EnumForTable.INDEXING);
            }
        }
        return true;
    }
}

/*
Document doc = Jsoup.connect("http://example.com")
  .data("query", "Java")
  .userAgent("Mozilla")
  .cookie("auth", "token")
  .timeout(3000)
  .post();
 */
