package searchengine;

import org.apache.lucene.morphology.LuceneMorphology;
import org.apache.lucene.morphology.russian.RussianLuceneMorphology;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Safelist;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import searchengine.model.Page;
import searchengine.services.LemmaFinderService;
import searchengine.services.StatisticSiteServiceImpl;

import java.io.IOException;
import java.util.Objects;

@SpringBootApplication
public class Application {
    public static void main(String[] args) throws IOException {
        SpringApplication.run(Application.class, args);
//
//        Document document = Jsoup.connect("https://radiomv.ru/")
//                .userAgent("Mozilla/5.0 (Windows; Windows NT 6.3; x64) AppleWebKit/537.1 (KHTML, like Gecko)" +
//                        "Chrome/47.0.1083.353 Safari/535")
//                .referrer("https://www.google.com")
//                .get();
//        String title = document.title();
//        Element elementSite = document.select("a").first();
//        String attribute = Objects.requireNonNull(elementSite).absUrl("href");
//
//        String html = document.html();
//        Page page = new Page();
//        LuceneMorphology luceneMorphology = new RussianLuceneMorphology();
//        LemmaFinderService lemmaFinder = new LemmaFinderService(luceneMorphology);
//        System.out.println(lemmaFinder.collectLemmas(html));

    }
}
