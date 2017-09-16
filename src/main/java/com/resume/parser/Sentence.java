/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.resume.parser;

import java.util.List;
import java.util.Map;
import javafx.util.Pair;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Sentence, contains all the information about sentence
 * the line, tokens, lemma, pos, ner, compound words.
 * @author Lokesh
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Sentence {
    /** actual sentence */
    private String line;
    
    /** the list of lemmas */
    private List<String> lemmas;
    
    /** pos tagged list */
    private List<Pair<String, String>> posTaggedList;
    
    /** ner tagged list */
    private List<Pair<String, String>> nerTaggedList;
       
    /** sematic graph edges */
    private Map<String,List<Pair<String,String>>> semanticGraph;
    
    /** interesting words */
    private List<String> interestingWords;
}
