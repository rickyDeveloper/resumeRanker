package com.resume.classifier;

import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.io.EncodingPrintWriter;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.util.SystemUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

/**
 * Created by vikasnaiyar on 16/09/17.
 */
public class CustomClassifier {
    public static void main(String[] args) throws IOException {
        File file = new File("src/main/resources/train/customTags.prop");
        FileInputStream fileInput = new FileInputStream(file);
        Properties properties = new Properties();
        properties.load(fileInput);
        fileInput.close();

        final CRFClassifier crfClassifier = new CRFClassifier(properties);
        crfClassifier.train("src/main/resources/train/customTags.train");

        List<List<CoreLabel>> coreLabels = crfClassifier.classifyFile("src/main/resources/train/resume.txt");

        coreLabels.forEach(labels -> {
            labels.forEach(label -> {
                String answer = label.get(CoreAnnotations.AnswerAnnotation.class);
                System.out.println(label.word() + "\t" + answer);
            });
        });
    }
}