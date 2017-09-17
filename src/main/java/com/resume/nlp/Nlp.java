/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.resume.nlp;

import java.util.List;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

/**
 * Nlp, Natural Language processor contains methods utilize StanfordCore NLP 
 * @author Lokesh
 */
public class Nlp {
    private static Properties p=new Properties();
    private static StanfordCoreNLP pipe;
    private static CRFClassifier nerClassifier;
    
    /**
     * Initializes the core NLP
     */
    public static void init(){
        p.put("annotators", "tokenize, ssplit, pos, lemma, parse, ner");
        pipe=new StanfordCoreNLP(p);
        String classifierPath = "edu/stanford/nlp/models/ner/english.muc.7class.distsim.crf.ser.gz";
         nerClassifier = CRFClassifier
                .getClassifierNoExceptions(classifierPath);
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
    public static String getClassification(String text) {
        return "";
    }
}
