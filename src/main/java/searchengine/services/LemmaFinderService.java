package searchengine.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.morphology.LuceneMorphology;
import org.apache.lucene.morphology.russian.RussianLuceneMorphology;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Safelist;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import searchengine.dto.statistics.SaveWordLemmas;
import searchengine.exception.StatisticLemmaException;
import searchengine.model.Lemma;
import searchengine.model.Page;
import searchengine.model.Search;
import searchengine.model.Site;
import searchengine.repository.LemmaRepository;
import searchengine.repository.SearchRepository;

import java.io.IOException;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class LemmaFinderService {
    private LuceneMorphology luceneMorphology;
    private LemmaRepository lemmaRepository;
    private SearchRepository searchRepository;

    public static LemmaFinderService getInstance() throws IOException {
        LuceneMorphology morphology= new RussianLuceneMorphology();
        return new LemmaFinderService(morphology);
    }

    public LemmaFinderService(LuceneMorphology luceneMorphology) {
        this.luceneMorphology = luceneMorphology;
    }


    /**
     * Метод разделяет текст на слова, находит все леммы и считает их количество.
     *
     * @param text текст из которого будут выбираться леммы
     * @return ключ является леммой, а значение количеством найденных лемм
     */
    public Map<String, Integer> collectLemmas(String text) {
        String[] words = arrayContainsRussianWords(text);
        HashMap<String, Integer> lemmas = new HashMap<>();
        Lemma lemma = new Lemma();
        Site site = new Site();

        for (String word : words) {
                if (word.isBlank()) {
                    continue;
                }

                List<String> wordBaseForms = luceneMorphology.getMorphInfo(word);
                if (anyWordBaseBelongToParticle(wordBaseForms)) {
                    continue;
                }

                List<String> normalForms = luceneMorphology.getNormalForms(word);
                if (normalForms.isEmpty()) {
                    continue;
                }

                String normalWord = normalForms.get(0);

                if (lemmas.containsKey(normalWord)) {
                    lemmas.put(normalWord, lemmas.get(normalWord) + 1);
                    for(Map.Entry<String, Integer> entry : lemmas.entrySet()){
                        String keyLemma = entry.getKey();
                        Integer valueLemma = entry.getValue();
                        lemma.setLemma(keyLemma);
                        lemma.setFrequency(valueLemma);
                        lemma.setSiteId(site);
                        lemmaRepository.save(lemma);
                    }
                } else {
                    lemmas.put(normalWord, 1);
                }
        }

        return lemmas;
    }

    private boolean anyWordBaseBelongToParticle(List<String> wordBaseForms) {
        return wordBaseForms.stream().anyMatch(this::hasParticleProperty);
    }

    private boolean hasParticleProperty(String wordBase) {
        String[] particlesNames = new String[]{"МЕЖД", "ПРЕДЛ", "СОЮЗ"};
        for (String property : particlesNames) {
            if (wordBase.toUpperCase().contains(property)) {
                return true;
            }
        }
        return false;
    }

    private String[] arrayContainsRussianWords(String text) {
        return text.toLowerCase(Locale.ROOT)
                .replaceAll("([^а-я\\s])", " ")
                .trim()
                .split("\\s+");
    }

    private boolean isCorrectWordForm(String word) {
        String WORD_TYPE_REGEX = "\\W\\w&&[^а-яА-Я\\s]";
        List<String> wordInfo = luceneMorphology.getMorphInfo(word);
        for (String morphInfo : wordInfo) {
            if (morphInfo.matches(WORD_TYPE_REGEX)) {
                return false;
            }
        }
        return true;
    }

    private String clearWebSiteFromHtml(String htmlCodeWebSite){
        Page page = new Page();
        htmlCodeWebSite = page.getContent();
        return Jsoup.clean(htmlCodeWebSite, Safelist.none());
    }
}
