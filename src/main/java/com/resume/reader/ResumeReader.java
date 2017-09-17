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
        		.apply("Lokesh\nlokesh.noble@gmail.com\nCollege of Engineering Guindy");
//                .apply("Lokesh is good at Object Oriented Programming concepts, Lokesh knows Typescript and Java. Lokesh graduated from College Of Engineering Guindy");
//        			.apply("Developed dynamic and interactive website that ensured high traffic, page views, and User Experience, resulting in 40% increase in sales revenue");//\n"+
//        		.apply("After graduating from Madras Institute of Technology (MIT – Chennai) in 1960, Dr. Abdul Kalam joined Aeronautical Development Establishment of Defence Research and Development Organisation (DRDO) as a scientist.");
//        		.apply("Lokesh\nlokesh.noble@gmail.com\nCollege of Engineering Guindy");
        System.out.println(sentence.getPosTaggedList());
        System.out.println(sentence.getPersonSet());
        System.out.println(sentence.getOrgSet());
        System.out.println(sentence.getDuractionSet());
        System.out.println(sentence.getSemanticGraph());
        System.out.println(sentence.getInterestingWords());
    }
    
}
