/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.resume.parser;

import com.beust.jcommander.internal.Maps;
import com.google.common.collect.Lists;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.semgraph.SemanticGraphEdge;
import edu.stanford.nlp.util.CoreMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import javafx.util.Pair;
import com.resume.nlp.Nlp;

/**
 * SenteceParser takes a line and gives {@link Sentence} object
 * @author Lokesh
 */
public class SentenceParser implements Function<String, Sentence> {
    
    /** relation between nouns */
    private String NOUN_COMPOUND_MODIFIER = "nn";
    
    /** proper noun, singular */
    private String PROPER_NOUN_SINGULAR = "NNP";

    /**
     * Annotates the given string with appropriate tags and produces {@link Sentence}
     * 
     * @param line
     * @return sentence
     */
    public Sentence apply(String line) {
        Sentence sentence = Sentence.builder()
                .line(line)
                .lemmas(Lists.newArrayList())
                .posTaggedList(Lists.newArrayList())
                .nerTaggedList(Lists.newArrayList())
                .semanticGraph(Maps.newHashMap())
                .build();
        Nlp.annotate(line).forEach(coreMap -> {
            coreMap.get(CoreAnnotations.TokensAnnotation.class).forEach(token -> {
                //String word = token.get(CoreAnnotations.TextAnnotation.class);
                //this is the POS tag of the token
                String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
                //this is the NER label of the token
                String ner = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);
                String lemma=token.get(CoreAnnotations.LemmaAnnotation.class);
                //if(pos.equals("NNP")||pos.equals("NNPS")||pos.equals("NN")||pos.equals("NNS")){
                    //System.out.println(word);
                //}
                //if(!ner.equals("O")) {
                    //System.out.println("NER "+ner+": "+word);
                //}
                sentence.getLemmas().add(lemma);
                sentence.getPosTaggedList().add(new Pair<>(lemma, pos));
                sentence.getNerTaggedList().add(new Pair<>(lemma, ner));
            });
            // this is the Stanford dependency graph of the current sentence
            SemanticGraph depd = coreMap.get(SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation.class);
            depd.edgeIterable().forEach(semanticGraphEdge -> {
                String relation = semanticGraphEdge.getRelation().toString();
                if(sentence.getSemanticGraph().containsKey(relation)){
                    sentence.getSemanticGraph().get(relation).add(new Pair<>(semanticGraphEdge.getTarget().word(),semanticGraphEdge.getSource().word()));
                } else {
                    sentence.getSemanticGraph().put(relation, Lists.newArrayList(new Pair<>(semanticGraphEdge.getTarget().word(),semanticGraphEdge.getSource().word())));
                }
//                if(relation.equals("nn")||relation.equals("appos")||relation.toString().equals("poss")){
//                    sentence.getCompoundWords().add(semanticGraphEdge.getTarget().word()+" "+semanticGraphEdge.getSource().word());
//                }
            });
       
        });
        sentence.setInterestingWords(getInterestingWordsFromPos(sentence.getPosTaggedList()));
        return sentence;
    }
    
//    private List<String> getInerestingWordsFromSemanticGraph(Map<String,List<Pair<String, String>>> semanticGraph) {
//        List<String> interestingWords = Lists.newArrayList();
//        semanticGraph.get(NOUN_COMPOUND_MODIFIER);
//        return interestingWords;
//    }
    
    private List<String> getInterestingWordsFromPos(List<Pair<String,String>> posTaggedList) {
        List<String> interestingWords = Lists.newArrayList();
        int ngramIndex = 0;
        for(int i =0; i<posTaggedList.size(); i++) {
            Pair<String, String> token = posTaggedList.get(i);
            if(PROPER_NOUN_SINGULAR.equals(token.getValue())){
                ngramIndex = i + 1;
                String interestingWord = token.getKey();
                while(ngramIndex < posTaggedList.size()) {
                    Pair<String, String> nextToken = posTaggedList.get(ngramIndex);
                    if(!PROPER_NOUN_SINGULAR.equals(nextToken.getValue())) {
                        i = ngramIndex - 1;
                        break;
                    }
                    interestingWord += " " + nextToken.getKey();
                }
                interestingWords.add(interestingWord);
            }
        }
        return interestingWords;
    }
    
}
