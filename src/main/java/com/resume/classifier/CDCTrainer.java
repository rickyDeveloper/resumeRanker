/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.resume.classifier;

import edu.stanford.nlp.classify.ColumnDataClassifier;
import java.io.IOException;

/**
 * Trains and Serializes the ColumnDataClassifier
 * @author Lokesh
 */
public class CDCTrainer {
    /**
     * Entry point for training
     * @param arg 
     */
    public static void main(String[] arg) throws IOException {
        String[] args = {"-prop", "src/main/resources/train/cdcTrainer.prop"};
        ColumnDataClassifier.main(args);
    }
}
