package searchengine.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import searchengine.config.SitesList;
import searchengine.dto.statistics.StatisticsSiteResponse;
import searchengine.exception.StatisticSiteException;
import searchengine.model.EnumForTable;
import searchengine.model.Page;
import searchengine.model.Site;
import searchengine.repository.PageRepository;
import searchengine.repository.SiteRepository;

import javax.annotation.PostConstruct;
import javax.print.DocFlavor;
import javax.transaction.Transactional;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;

@Service
@RequiredArgsConstructor
public class StatisticSiteServiceImpl  {
    private final SiteRepository siteRepository;
    private final PageRepository pageRepository;
    private final SitesList sitesList;
    private ExecutorService executorService;

    public void createEntry() {
        Site site = new Site();
        site.setStatus(EnumForTable.INDEXING);
        siteRepository.save(site);
    }

    public void createDataInTablePage(String url) {

        try {
            controlSite(url);
        }catch (IllegalArgumentException e){
            throw new IllegalArgumentException("Data entered incorrectly");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public void deleteSite(String url) {

        if (!url.isEmpty()) {
            throw new IllegalArgumentException();
        }
        Long deleteDataSite = siteRepository.deleteByUrl(url);
        Long deleteDataPage = pageRepository.deleteByPath(url);
    }

    public StatisticsSiteResponse roundSites() {
        StatisticsSiteResponse siteResponse = new StatisticsSiteResponse();

        if (!executorService.isShutdown()) {
            List<CompletableFuture<Void>> futures = new ArrayList<>();
            sitesList.getSites().forEach(site -> {
                CompletableFuture<Void> future = CompletableFuture.runAsync(() -> roundSite(site.getUrl())
                        ,executorService);
                futures.add(future);
            });
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
            siteResponse.setError("result: ");
            siteResponse.setResult(true);
        } else {
            siteResponse.setError("result: Индексация уже запущена");
            siteResponse.setResult(false);
        }

        return siteResponse;
    }

    @PostConstruct
    private void postConstruct(){
        executorService = Executors.newCachedThreadPool();
    }

    private boolean roundSite(String url) {
        Site site = new Site();
        Page page = new Page();
        Document doc;

        try {
            doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows; Windows NT 6.3; x64) AppleWebKit/537.1 (KHTML, like Gecko)" +
                            "Chrome/47.0.1083.353 Safari/535")
                    .referrer("https://www.google.com")
                    .get();
        } catch (Exception e) {
            throw new StatisticSiteException(e.getMessage());
        }
        String title = doc.title();
        try {
            controlSite(url);
            if (!page.getContent().isEmpty()) {
                siteRepository.save(site);
                pageRepository.save(page);
            }
            throw new StatisticSiteException("HTML code not found");
        } catch (Exception e) {
            site.setStatus(EnumForTable.INDEXING);
        }
        return true;
    }

    public void controlSite(String url) throws IOException {
        Page page = new Page();
        Site site = new Site();
        Document document = Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows; Windows NT 6.3; x64) AppleWebKit/537.1 (KHTML, like Gecko)" +
                        "Chrome/47.0.1083.353 Safari/535")
                .referrer("https://www.google.com")
                .get();
        String title = document.title();
        Element elementSite = document.select("a").first();
        String attribute = Objects.requireNonNull(elementSite).absUrl("href");

        URL urlCode = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) urlCode.openConnection();
        int responseCode = connection.getResponseCode();

        String htmlCode = elementSite.outerHtml();

        System.out.printf("Execute task on thread %s%n", Thread.currentThread());

        if (!attribute.startsWith(url)) {
            throw new StatisticSiteException("Incorrect data entered");
        }
        page.setContent(String.valueOf(attribute.replaceFirst("https://", "").split("/").length));

        site.setStatus_time(Instant.now());
        site.setUrl(attribute);
        site.setNameSite(title);
        site.setStatus(EnumForTable.INDEXED);
        siteRepository.save(site);

        page.setPath(attribute);
        page.setCode(responseCode);
        page.setContent(htmlCode);
        page.setSiteId(site);
        pageRepository.save(page);

    }
    public StatisticsSiteResponse stopIndexing(){
        StatisticsSiteResponse siteResponse = new StatisticsSiteResponse();
        if(!roundSites().isResult()){
            siteResponse.setResult(true);
        }
        siteResponse.setError("Индксация уже запущена");
        return siteResponse;
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
