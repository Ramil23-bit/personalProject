package searchengine.services;

import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.morphology.LuceneMorphology;
import org.apache.lucene.morphology.russian.RussianLuceneMorphology;
import searchengine.dto.statistics.SaveWordLemmas;
import searchengine.exception.StatisticLemmaException;
import searchengine.model.Lemma;
import searchengine.repository.LemmaRepository;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Slf4j
public class LemmasForEachWord {

    private final LuceneMorphology luceneMorphology;
    public SaveWordLemmas saveWordLemmas;
    public LemmaRepository lemmaRepository;

    public LemmasForEachWord(LuceneMorphology luceneMorphology) {
        this.luceneMorphology = luceneMorphology;
    }

    public List<String> morphologyForms(String text){
        if(!luceneMorphology.checkString(text)){
            return Collections.emptyList();
        }
        return luceneMorphology.getMorphInfo(text);
    }

    public SaveWordLemmas saveWord(String text){
        if(!luceneMorphology.checkString(text)){
            throw new StatisticLemmaException("Введенное слово содержит недопустимые символы");
        }
        List<String>infoLemma = luceneMorphology.getMorphInfo(text);
        for (int i = 0; i < infoLemma.size(); i++) {
            saveWordLemmas.setWord(infoLemma.toString());
            saveWordLemmas.setNumber(i);
        }
        return saveWordLemmas;
    }

    //Не пойму как мне сравнивать слова на междометия, союза и так далее. в тексте нужно убрать все не слова
    public String checkingWordsForLemmas(String text) throws IOException {
        text = text.replaceAll("[^а-яА-Я]", " ");
        StringBuilder stringBuilder = new StringBuilder();
        List<String>  wordForm = luceneMorphology.getMorphInfo("или");
        String [] newText = text.trim().split("\\s+");
        int i = 0;
        for (String s : newText) {
            if(!(s.length() <= 2) && (s.equals(wordForm.get(i))))
                stringBuilder.append(s).append("\n");
                System.out.println(s);
                i++;
            }
        return stringBuilder.toString().strip();
        }


    private Lemma saveWordToRepository(String text, List<String>morphInfo){
        Lemma lemma = new Lemma();
        for(int i = 0; i < morphInfo.size(); i++) {
            lemma.setLemma(text);
            lemma.setFrequency(i);
        }
        return lemmaRepository.save(lemma);
    }

}
