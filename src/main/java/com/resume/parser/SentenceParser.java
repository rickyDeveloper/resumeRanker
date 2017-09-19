/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.resume.parser;

import com.beust.jcommander.internal.Maps;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

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
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javafx.util.Pair;
import com.resume.nlp.Nlp;

/**
 * SenteceParser takes a line and gives {@link Sentence} object
 * @author Lokesh
 */
public class SentenceParser implements Function<String, Sentence> {

	/** Duration, named entity */
    private String DURATION_ENTITY = "DURATION";
	
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
                .personSet(Sets.newHashSet())
                .orgSet(Sets.newHashSet())
                .duractionSet(Sets.newHashSet())
                .domainSet(Maps.newHashMap())
                .skillSet(Maps.newHashMap())
                .build();
        Nlp.annotate(line).forEach(coreMap -> {
            coreMap.get(CoreAnnotations.TokensAnnotation.class).forEach(token -> {
                //String word = token.get(CoreAnnotations.TextAnnotation.class);
                //this is the POS tag of the token
                String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
                String ner = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);
                String lemma=token.get(CoreAnnotations.LemmaAnnotation.class);
                //if(pos.equals("NNP")||pos.equals("NNPS")||pos.equals("NN")||pos.equals("NNS")){
                    //System.out.println(word);
                //}
                sentence.getLemmas().add(lemma);
                sentence.getPosTaggedList().add(new Pair<>(lemma, pos));
                sentence.getNerTaggedList().add(new Pair<>(lemma, ner));
            });
            // this is the Stanford dependency graph of the current sentence
            SemanticGraph depd = coreMap.get(SemanticGraphCoreAnnotations.EnhancedPlusPlusDependenciesAnnotation.class);
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
        sentence.setInterestingWords(new InterestingWordExtractor().apply(sentence));
        sentence.setNerAnnotatedText(Nlp.annotateNer(line));
        processNerResults(sentence);
        sentence.setSkillSet(getSkillSetMap(sentence));
        return sentence;
    }
    
    /**
     * Process the NER annotated text and updates respective sets
     * @param text
     */
    private void processNerResults(Sentence sentence) {
        if(StringUtils.isNoneBlank(sentence.getNerAnnotatedText())) {
        	sentence.setPersonSet(parseTextForEntity(sentence.getNerAnnotatedText(), "PERSON"));
        	sentence.setOrgSet(parseTextForEntity(sentence.getNerAnnotatedText(), "ORGANIZATION"));
        }
        if(CollectionUtils.isNotEmpty(sentence.getNerTaggedList())) {
        	sentence.setDuractionSet(getInterestingWordsFromNer(sentence.getNerTaggedList()));
        }
    }

    /**
     * Parses the given string and finds the given entity
     * @param text
     * @param entity
     * @return set of entities
     */
	private Set<String> parseTextForEntity(String text, String entity) {
		Set<String> nerSet = Sets.newHashSet();
		Pattern pattern = Pattern
                .compile("([a-zA-Z0-9.%]+(/" + entity
                        + ")[ ]*)*[a-zA-Z0-9.%]+(/" + entity + ")");
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            String inputText = text.substring(start, end);
            inputText = inputText.replaceAll("/" + entity, "");
            nerSet.add(inputText);
       }
       return nerSet;
	}
	
	/**
	 * Extracts interesting words out of NER tagged tokens
	 * @param nerTaggedList
	 * @return set of interesting words
	 */
	private Set<String> getInterestingWordsFromNer(List<Pair<String,String>> nerTaggedList) {
	  Set<String> interestingWords = Sets.newHashSet();
	  int ngramIndex = 0;
	  for(int i =0; i<nerTaggedList.size(); i++) {
	      Pair<String, String> token = nerTaggedList.get(i);
	      if(DURATION_ENTITY.equals(token.getValue())){ //if it is a duration then look for next word
	          ngramIndex = i + 1;
	          String interestingWord = token.getKey();
	          while(ngramIndex < nerTaggedList.size()) {
	              Pair<String, String> nextToken = nerTaggedList.get(ngramIndex);
	              if(!DURATION_ENTITY.equals(nextToken.getValue())) {
	                  i = ngramIndex - 1;
	                  break;
	              }
	              interestingWord += " " + nextToken.getKey();
	              ngramIndex++;
	          }
	          interestingWords.add(interestingWord);
	      }
	  }
	  return interestingWords;
	}
	
	/**
	 * gets the skill set with nlp classifier
	 * @param sentence
	 * @return skill set map
	 */
	private Map<String, Set<String>> getSkillSetMap(Sentence sentence) {
		Map<String, Set<String>> skillSet = Maps.newHashMap();
		sentence.getLemmas().forEach(lemma -> {
			Nlp.getSkillClassification(lemma).entrySet().forEach(entry -> {
				if(skillSet.containsKey(entry.getKey())){
					skillSet.get(entry.getKey()).addAll(entry.getValue());
				} else {
					skillSet.put(entry.getKey(), entry.getValue());
				}
			});
		});
		return skillSet;
	}
}
