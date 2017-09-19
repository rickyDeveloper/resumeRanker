/****************************************************************************
* Copyright (c) 2017 by Accolite.com. All rights reserved
* Created date :: Sep 17, 2017
*  @author :: Lokesh K
* ***************************************************************************
*/
package com.resume.reader;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

import com.google.common.collect.Lists;

import javafx.util.Pair;

/**
 * The Class ResumeSectionReader.
 */
public class ResumeSectionReader implements Function<InputStream, List<Pair<String, String>>> {

	// Every resume will have a set of sections.  We want to identify the sections
    // and then apply different classifier for each of them.
    private Map<String, List<String>> sectionHeaders = new HashMap<>();
    
    public ResumeSectionReader() {
	    sectionHeaders.put("SUMMARY", Lists.newArrayList(
	        "EXPERIENCE SUMMARY",
	        "Experience Summary",
	        "Summary",
	        "Experience")
	    );
	    sectionHeaders.put("EDUCATION", Lists.newArrayList("Education","EDUCATION"));
	    sectionHeaders.put("WORK", Lists.newArrayList("Work Experience","Professional Experience"));
	    sectionHeaders.put("SKILLS", Lists.newArrayList(
	        "TECHNICAL SKILLS",
	        "Technical skills",
	        "SKILLS",
	        "Skills")
	    );
	    sectionHeaders.put("PERSONAL", Lists.newArrayList(
	        "PERSONAL DETAILS",
	        "Personal Details",
	        "Personal",
	        "Personal")
	    );
    }
    
	/**
	 * Reads given file and extracts the section into a list
	 * @param  file
	 * @return list of section and it's contents
	 */
	public List<Pair<String, String>> apply(InputStream stream) {
		List<Pair<String, String>> resumeSections = Lists.newArrayList();
		try {
            //parse method parameters
            Parser parser = new AutoDetectParser();
            BodyContentHandler handler = new BodyContentHandler();
            Metadata metadata = new Metadata();
            ParseContext context = new ParseContext();
            //parsing the file
            parser.parse(stream, handler, metadata, context);
            String text = handler.toString();
            Map<Integer, String> sectionStartIndexMap = getSectionHeaderIndexMap(text, sectionHeaders);
            if (!sectionStartIndexMap.isEmpty()) {
                Iterator<Integer> indexIterator = sectionStartIndexMap.keySet().iterator();
                int prevStartIndex = -1;
                while (indexIterator.hasNext()) {
                    if(prevStartIndex == -1) {
                        prevStartIndex = indexIterator.next();
                    }
                    int startIndex = prevStartIndex;  //0, 255, 450
                    int endIndex = 0;
                    if(indexIterator.hasNext()) {
                        endIndex = indexIterator.next();  // 255, 450
                        prevStartIndex=endIndex;
//                        System.out.println(sectionStartIndexMap.get(startIndex) + " --> " + text.substring(startIndex,endIndex));
                        resumeSections.add(new Pair<>(sectionStartIndexMap.get(startIndex), text.substring(startIndex,endIndex)));
                    } else {
//                        System.out.println(sectionStartIndexMap.get(startIndex) + " --> " + text.substring(startIndex));
                    	resumeSections.add(new Pair<>(sectionStartIndexMap.get(startIndex), text.substring(startIndex)));
                    }
                }
//                System.out.println(sectionStartIndexMap.get(prevStartIndex) + " --> " + text.substring(prevStartIndex));
                resumeSections.add(new Pair<>(sectionStartIndexMap.get(prevStartIndex), text.substring(prevStartIndex)));
            }
        } catch (TikaException | IOException | SAXException e) {
            e.printStackTrace();
        }
		return resumeSections;
	}
	
	/**
     * Idea is to pick up indexes of individual sections and then give those corresponding sections to various classifier
     * so that we can identity the entities.
     * @param text
     * @param sectionHeaders
     * @return section header index map
     */
    public Map<Integer, String> getSectionHeaderIndexMap(String text, Map<String,List<String>> sectionHeaders) {
        Map<Integer, String> sectionStartIndexMap = new TreeMap<>();

        sectionStartIndexMap.put(0,"FIRST_SECTION"); // this is for header section

        sectionHeaders.keySet().forEach(section -> {
            Iterator<String> sectionHeaderIterator = sectionHeaders.get(section).iterator();

            while (sectionHeaderIterator.hasNext()) {
                String header = sectionHeaderIterator.next();
                int index = text.indexOf(header);
                if (index != -1) { // FOUND AN INDEX
                    sectionStartIndexMap.put(index, section);
                    break;
                }
            };
        });

        return sectionStartIndexMap;
    }

}
