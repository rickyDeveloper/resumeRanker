package com.resume.classifier;

import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.util.Triple;
import org.apache.commons.codec.binary.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 * Created by vikasnaiyar on 16/09/17.
 */
public class ConceptsClassifier {

    public static void main(String[] args) throws IOException {
        Set<String> conceptLabels = new HashSet<>();
        conceptLabels.add("CONCEPT_OOPS");
        conceptLabels.add("DESIGN_PATTERNS");

        File file = new File("src/main/resources/train/conceptsTags.prop");
        FileInputStream fileInput = new FileInputStream(file);
        Properties properties = new Properties();
        properties.load(fileInput);
        fileInput.close();

        final CRFClassifier crfClassifier = new CRFClassifier(properties);
        crfClassifier.train("src/main/resources/train/conceptTags.train");

        List<List<CoreLabel>> coreLabels = crfClassifier.classifyFile("src/main/resources/train/concepts.txt");

        for (List<CoreLabel> sentence : coreLabels) {
            for (int index=0; index < sentence.size(); index++) {
                CoreLabel word = sentence.get(index);
                String label = word.get(CoreAnnotations.AnswerAnnotation.class);
                if (conceptLabels.contains(label)) {
                    if(index+1 <sentence.size()) {
                        CoreLabel nextWord = sentence.get(index+1);
                        if (StringUtils.equals(nextWord.get(CoreAnnotations.AnswerAnnotation.class), label)) {
                            String answer = word.get(CoreAnnotations.AnswerAnnotation.class);
                            System.out.println(word.word() + " " + nextWord + "\t" + answer);
                        }
                    }
                }
            }
        }
    }
}
