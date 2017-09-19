package com.resume.reader;

import java.util.List;
import java.util.function.Function;

import com.beust.jcommander.internal.Maps;
import com.google.common.collect.Sets;
import com.resume.parser.Sentence;
import com.resume.parser.SentenceParser;

import javafx.util.Pair;

public class ResumeSectionToResumeExcerptMapper implements Function<List<Pair<String, String>>, ResumeExcerpt>{

	/**
	 * Gets the resume excerpt from given resume section data
	 * @param resumeSection
	 * @return resumeExcerpt
	 */
	public ResumeExcerpt apply(List<Pair<String, String>> resumeSection) {
		ResumeExcerpt resumeExcerpt = ResumeExcerpt.builder()
        		.domains(Maps.newHashMap())
        		.durations(Sets.newHashSet())
        		.interestedTerms(Sets.newHashSet())
        		.organizations(Sets.newHashSet())
        		.personNames(Sets.newHashSet())
        		.skills(Maps.newHashMap())
        		.build();
		resumeSection.forEach(section -> {
	        Sentence sentence = new SentenceParser().apply(section.getValue());
	        resumeExcerpt.getPersonNames().addAll(sentence.getPersonSet());
	        resumeExcerpt.getOrganizations().addAll(sentence.getOrgSet());
	        resumeExcerpt.getDurations().addAll(sentence.getDuractionSet());
	        sentence.getSkillSet().entrySet().forEach(entry -> {
	        	if(resumeExcerpt.getSkills().containsKey(entry.getKey())){
	        		resumeExcerpt.getSkills().get(entry.getKey()).addAll(entry.getValue());
				} else {
					resumeExcerpt.getSkills().put(entry.getKey(), entry.getValue());
				}
	        });
	        sentence.getDomainSet().entrySet().forEach(entry -> {
	        	if(resumeExcerpt.getDomains().containsKey(entry.getKey())){
	        		resumeExcerpt.getDomains().get(entry.getKey()).addAll(entry.getValue());
				} else {
					resumeExcerpt.getDomains().put(entry.getKey(), entry.getValue());
				}
	        });
	        resumeExcerpt.getInterestedTerms().addAll(sentence.getInterestingWords());
        });
        resumeExcerpt.getSkills().remove("OTHER");
        resumeExcerpt.getDomains().remove("OTHER");
		return resumeExcerpt;
	}

}
