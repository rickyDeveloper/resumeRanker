/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.resume.nlp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.beust.jcommander.internal.Maps;
import com.google.common.collect.Sets;

import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.Triple;

/**
 * Nlp, Natural Language processor contains methods utilize StanfordCore NLP 
 * @author Lokesh
 */
public class Nlp {
    private static Properties p=new Properties();
    private static StanfordCoreNLP pipe;
    private static CRFClassifier nerClassifier;
    private static CRFClassifier skillClassifier;
    
    /**
     * Initializes the core NLP
     */
    public static void init(){
        p.put("annotators", "tokenize, ssplit, pos, lemma, parse, ner");
        pipe=new StanfordCoreNLP(p);
        String classifierPath = "edu/stanford/nlp/models/ner/english.muc.7class.distsim.crf.ser.gz";
        nerClassifier = CRFClassifier
                .getClassifierNoExceptions(classifierPath);
        File file = new File("src/main/resources/train/skillClassifier.prop");
        Properties properties = new Properties();
        try (FileInputStream fileInput = new FileInputStream(file)){
			properties.load(fileInput);
		} catch (IOException e) {
			e.printStackTrace();
		}
        skillClassifier = new CRFClassifier(properties);
        skillClassifier.train("src/main/resources/train/skillClassModel.train"); 
    }
    
    /**
     * Annotates the given sentence
     * @param text
     * @return coreMap
     */
    public static List<CoreMap> annotate(String text){
        Annotation a=new Annotation(text);
        pipe.annotate(a);
        return a.get(CoreAnnotations.SentencesAnnotation.class);
    }
    
    /**
     * Annotates the given text with appropriate NER tags 
     * @param text
     * @return NER annotated text
     */
    public static String annotateNer(String text) {
    	String output = "";
    	if(StringUtils.isNotBlank(text)) {
    		output = nerClassifier.classifyToString(text);
    	}
    	return output;
    }
    
    /**
     * Gets the appropriate classification for given string
     * @param text
     * @return class of the given string
     */
    public static Map<String, Set<String>> getSkillClassification(String text) {
        Map<String, Set<String>> skillClassification = Maps.newHashMap();
        List<Triple<String, Integer, Integer>> results = skillClassifier.classifyToCharacterOffsets(text.toLowerCase());
        results.forEach(triple -> {
        	if(skillClassification.containsKey(triple.first)) {
        		skillClassification.get(triple.first).add(text.substring(triple.second, triple.third));
        	} else {
        		skillClassification.put(triple.first, Sets.newHashSet(text.substring(triple.second, triple.third)));
        	}
        });
        return skillClassification;
    }
}
