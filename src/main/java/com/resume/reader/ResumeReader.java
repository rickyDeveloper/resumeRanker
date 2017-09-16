/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.resume.reader;

import com.resume.nlp.Nlp;
import com.resume.parser.Sentence;
import com.resume.parser.SentenceParser;

/**
 * ResumeReader contains main method this will serve as starting point of local executions.
 * @author Lokesh
 */
public class ResumeReader {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Nlp.init();
        Sentence sentence = new SentenceParser()//.apply("A minimum of two years commercial software development experience in Java, and four years overall software development experience");
                .apply("Lokesh is good at Object Oriented Programming concepts, he knows typescript and Java");
        System.out.println(sentence.getPosTaggedList());
        System.out.println(sentence.getNerTaggedList());
        System.out.println(sentence.getSemanticGraph());
        System.out.println(sentence.getInterestingWords());
    }
    
}
