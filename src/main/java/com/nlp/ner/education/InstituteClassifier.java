package com.nlp.ner.education;

import edu.stanford.nlp.ie.crf.CRFClassifier;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by vikasnaiyar on 17/09/17.
 */
public class InstituteClassifier {

    public static void main(String[] args) {

        String text = "John Doe graduated from Indian Institute of Tehnology, Bombay in 2007";

        String classifierPath = "edu/stanford/nlp/models/ner/english.muc.7class.distsim.crf.ser.gz";

        CRFClassifier classifier = CRFClassifier
                .getClassifierNoExceptions(classifierPath);


        String output = classifier.classifyToString(text);
        parseText(output);
    }

    public static void parseText(String text) {
        String[] ENTITY_LIST = { "person", "location", "date", "organization",
                "time", "money", "percentage" };
        try {
            if (text != null) {
                for (String entityValue : ENTITY_LIST) {
                    String entity = entityValue.toUpperCase();
                    Pattern pattern = Pattern
                            .compile("([a-zA-Z0-9.%]+(/" + entity
                                    + ")[ ]*)*[a-zA-Z0-9.%]+(/" + entity + ")");
                    Matcher matcher = pattern.matcher(text);
                    while (matcher.find()) {
                        int start = matcher.start();
                        int end = matcher.end();
                        String inputText = text.substring(start, end);
                        inputText = inputText.replaceAll("/" + entity, "");
                        System.out.println(inputText + " : " + entity);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
