package searchengine.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.morphology.LuceneMorphology;
import org.apache.lucene.morphology.russian.RussianLuceneMorphology;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import searchengine.model.Lemma;
import searchengine.model.Site;
import searchengine.repository.LemmaRepository;

import java.io.IOException;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class LemmaFinderService {
    private final LemmaRepository lemmaRepository;

    /**
     * Метод разделяет текст на слова, находит все леммы и считает их количество.
     *
     * @param text текст из которого будут выбираться леммы
     * @return ключ является леммой, а значение количеством найденных лемм
     */
    public Map<String, Integer> collectLemmas(String text) throws IOException {
        LuceneMorphology luceneMorphology = new RussianLuceneMorphology();
        String[] words = arrayContainsRussianWords(text);
        HashMap<String, Integer> lemmas = new HashMap<>();
        Site site =new Site();

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
                } else {
                    lemmas.put(normalWord, 1);
                }
        }
        for(int j = 0; j < 10; j++){
            List<Lemma> lemmaList = new ArrayList<>();
            for(int i =0; i == lemmaList.size(); i++) {
                for(Map.Entry<String, Integer> entry : lemmas.entrySet()){
                    Lemma lemma = new Lemma();
                    String keyLemma = entry.getKey();
                    Integer valueLemma = entry.getValue();
                    lemma.setLemma(keyLemma);
                    lemma.setFrequency(valueLemma);
                    lemma.setSiteId(site);
                    lemmaList.add(lemma);
                }
                lemmaRepository.saveAll(lemmaList);
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

    private boolean isCorrectWordForm(String word) throws IOException {
        LuceneMorphology luceneMorphology = new RussianLuceneMorphology();
        String WORD_TYPE_REGEX = "\\W\\w&&[^а-яА-Я\\s]";
        List<String> wordInfo = luceneMorphology.getMorphInfo(word);
        for (String morphInfo : wordInfo) {
            if (morphInfo.matches(WORD_TYPE_REGEX)) {
                return false;
            }
        }
        return true;
    }

//    public String clearWebSiteFromHtml(String url){
//        Page page = new Page();
//        String htmlCodeWebSite = page.getContent();
//        String cleanCode = Jsoup.clean(htmlCodeWebSite, Safelist.none());
//        return cleanCode;
//    }
}
