package com.resume.reader;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Created by vikasnaiyar on 17/09/17.
 */
public class ResumeSectionReader {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // Every resume will have a set of sections.  We want to identify the sections
        // and then apply different classifier for each of them.
        Map<String, List<String>> sectionHeaders = new HashMap<>();
        sectionHeaders.put("SUMMARY", new ArrayList<String>() {{
            add("EXPERIENCE SUMMARY");
            add("Experience Summary");
            add("Summary");
            add("Experience");
        }});

        sectionHeaders.put("EDUCATION", new ArrayList<String>() {{
            add("Education");
            add("EDUCATION");
        }});

        sectionHeaders.put("WORK", new ArrayList<String>() {{
            add("Work Experience");
            add("Professional Experience");
        }});

        sectionHeaders.put("SKILLS", new ArrayList<String>() {{
            add("TECHNICAL SKILLS");
            add("Technical skills");
            add("SKILLS");
            add("Skills");
        }});

        sectionHeaders.put("PERSONAL", new ArrayList<String>() {{
            add("PERSONAL DETAILS");
            add("Personal Details");
            add("Personal");
            add("Personal");
        }});


        try (InputStream stream = ResumeReader.class.getResourceAsStream("/resume/John_Doe_cv.docx")) {
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
                boolean firstSectionProcessed = false;
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
                        System.out.println(sectionStartIndexMap.get(startIndex) + " --> " + text.substring(startIndex,endIndex));
                    } else {
                        System.out.println(sectionStartIndexMap.get(startIndex) + " --> " + text.substring(startIndex));
                    }
                }

                System.out.println(sectionStartIndexMap.get(prevStartIndex) + " --> " + text.substring(prevStartIndex));
            }

        } catch (TikaException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

    /**
     * Idea is to pick up indexes of individual sections and then give those corresponding sections to various classifier
     * so that we can identity the entities.
     * @param text
     * @param sectionHeaders
     * @return
     */
    public static Map<Integer, String> getSectionHeaderIndexMap(String text, Map<String,List<String>> sectionHeaders) {
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
