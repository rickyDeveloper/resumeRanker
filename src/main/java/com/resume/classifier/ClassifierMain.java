/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.resume.classifier;

import edu.stanford.nlp.classify.ColumnDataClassifier;
import edu.stanford.nlp.ling.Datum;

/**
 * Created by vikasnaiyar on 10/09/17.
 */
public class ClassifierMain {
    public static void main(String[] args) {
        ColumnDataClassifier cdc = new ColumnDataClassifier("src/main/resources/train/customEntityClassifier.prop");
        String line = "\txxxxxx";
        Datum<String,String> d = cdc.makeDatumFromLine(line);
        System.out.println(line + "  ==>  " + cdc.classOf(d));
    }
}