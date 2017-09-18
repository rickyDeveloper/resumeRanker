package com.resume.reader;

import java.util.Map;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResumeExcerpt {
	
	private Set<String> personNames;
	
	private Set<String> organizations;
	
	private Set<String> durations;
	
	private Map<String, Set<String>> domains;
	
	private Map<String, Set<String>> skills;
	
	private Set<String> interestedTerms;
}
