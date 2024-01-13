package searchengine;

import org.apache.lucene.morphology.LuceneMorphology;
import org.apache.lucene.morphology.russian.RussianLuceneMorphology;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import searchengine.repository.SiteRepository;
import searchengine.services.LemmasForEachWord;
import searchengine.services.StatisticSiteServiceImpl;

import java.io.IOException;
import java.util.List;

@SpringBootApplication
public class Application {
    public static void main(String[] args) throws IOException {
        SpringApplication.run(Application.class, args);

    }
}
