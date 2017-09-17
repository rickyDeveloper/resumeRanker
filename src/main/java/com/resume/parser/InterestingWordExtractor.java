/****************************************************************************
* Copyright (c) 2017 by Accolite.com. All rights reserved
* Created date :: Sep 17, 2017
*  @author :: Lokesh K
* ***************************************************************************
*/
package com.resume.parser;

import java.util.List;
import java.util.Set;
import java.util.function.Function;

import org.apache.commons.collections4.CollectionUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import javafx.util.Pair;

/**
 * The Class InterestingWordExtractor.
 */
public class InterestingWordExtractor implements Function<Sentence, Set<String>>{

	/** relation between nouns */
    private String NOUN_COMPOUND_MODIFIER = "nn";
    
    /** proper noun, singular */
    private String PROPER_NOUN_SINGULAR = "NNP";
	
	/**
	 * Finds those interesting words from the given sentence
	 * @param sentence
	 * @return list of interesting words
	 */
	public Set<String> apply(Sentence sentence) {
		Set<String> interestingWords = Sets.newHashSet();
		if(sentence != null) {
			if(CollectionUtils.isNotEmpty(sentence.getPosTaggedList())) {
				interestingWords = getInterestingWordsFromPos(sentence.getPosTaggedList());
			}
		}
		return interestingWords;
	}
	
//  private List<String> getInerestingWordsFromSemanticGraph(Map<String,List<Pair<String, String>>> semanticGraph) {
//  List<String> interestingWords = Lists.newArrayList();
//  semanticGraph.get(NOUN_COMPOUND_MODIFIER);
//  return interestingWords;
//}

	/**
	 * Extracts interesting words out of POS tagged tokens
	 * @param posTaggedList
	 * @return set of interesting words
	 */
	private Set<String> getInterestingWordsFromPos(List<Pair<String,String>> posTaggedList) {
	  Set<String> interestingWords = Sets.newHashSet();
	  int ngramIndex = 0;
	  for(int i =0; i<posTaggedList.size(); i++) {
	      Pair<String, String> token = posTaggedList.get(i);
	      if(PROPER_NOUN_SINGULAR.equals(token.getValue())){ //if it is a proper noun then look for compound words
	          ngramIndex = i + 1;
	          String interestingWord = token.getKey();
	          while(ngramIndex < posTaggedList.size()) {
	              Pair<String, String> nextToken = posTaggedList.get(ngramIndex);
	              if(!PROPER_NOUN_SINGULAR.equals(nextToken.getValue())) {
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

}
