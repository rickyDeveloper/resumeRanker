/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.resume.reader;

import com.beust.jcommander.internal.Maps;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
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
        ResumeExcerpt resumeExcerpt = ResumeExcerpt.builder()
        		.domains(Maps.newHashMap())
        		.durations(Sets.newHashSet())
        		.interestedTerms(Sets.newHashSet())
        		.organizations(Sets.newHashSet())
        		.personNames(Sets.newHashSet())
        		.skills(Maps.newHashMap())
        		.build();
        new ResumeSectionReader().apply(ResumeReader.class.getResourceAsStream("/resume/John_Doe_cv.docx")).forEach(section -> {
	        Sentence sentence = new SentenceParser().apply(section.getValue());
//	        System.out.println(sentence.getPosTaggedList());
//	        System.out.println("Person List: "+sentence.getPersonSet());
	        resumeExcerpt.getPersonNames().addAll(sentence.getPersonSet());
//	        System.out.println("Organization List: "+sentence.getOrgSet());
	        resumeExcerpt.getOrganizations().addAll(sentence.getOrgSet());
//	        System.out.println("Duration List: "+sentence.getDuractionSet());
	        resumeExcerpt.getDurations().addAll(sentence.getDuractionSet());
//	        System.out.println("Skill set map:"+sentence.getSkillSet());
	        sentence.getSkillSet().entrySet().forEach(entry -> {
	        	if(resumeExcerpt.getSkills().containsKey(entry.getKey())){
	        		resumeExcerpt.getSkills().get(entry.getKey()).addAll(entry.getValue());
				} else {
					resumeExcerpt.getSkills().put(entry.getKey(), entry.getValue());
				}
	        });
//	        System.out.println(sentence.getSemanticGraph());
//	        System.out.println(sentence.getInterestingWords());
	        resumeExcerpt.getInterestedTerms().addAll(sentence.getInterestingWords());
        });
        resumeExcerpt.getSkills().remove("OTHER");
        System.out.println(resumeExcerpt);
    }
    
}
