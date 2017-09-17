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
        new ResumeSectionReader().apply("/resume/John_Doe_cv.docx").forEach(section -> {
	        Sentence sentence = new SentenceParser().apply(section.getValue());
//	        System.out.println(sentence.getPosTaggedList());
	        System.out.println("Person List: "+sentence.getPersonSet());
	        System.out.println("Organization List: "+sentence.getOrgSet());
	        System.out.println("Duration List: "+sentence.getDuractionSet());
//	        System.out.println(sentence.getSemanticGraph());
//	        System.out.println(sentence.getInterestingWords());
        });
    }
    
}
