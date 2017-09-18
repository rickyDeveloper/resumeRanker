package com.resume.ranker;

import com.mongodb.*;
import com.mongodb.util.JSON;
import com.rest.util.WordToNumberUtil;
import org.bson.types.ObjectId;

import java.net.UnknownHostException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by vikasnaiyar on 17/09/17.
 */
public class ResumeRanker {

    private String domainName;

    private String languageTag;

    private String frameworkTag;

    public ResumeRanker(String domainName, String languageTag, String frameworkTag) {
        this.domainName = domainName;
        this.frameworkTag= frameworkTag;
        this.languageTag = languageTag;
    }

    public Map<DBObject, Map<Double,DBObject>> rankResume(DBObject targetObject, Collection<DBObject> sourceObjects) {
        double rank = 0.0d;
/*
        b) Exp    -->   40% weight
               < exp  1
               = exp  3
               > exp  4

        c) Skills   -->  50 % weight
                count of matching skills:-
                     % matching
                     languages -- 30 %
                     frameworks -- 20 %

        d) Domain  --> 10 %  weight
                    % matching * 1
*/
        Map<Double, DBObject> matchingScoreMap = new HashMap<>();
        Map<DBObject, Map<Double,DBObject>> scoreMap = new HashMap<>();
        scoreMap.put(targetObject, matchingScoreMap);

        int targetExpInMonths = getExpInMonths(targetObject);

        Collection<String> targetDomainSkills =  getTagEntries(targetObject, "domains", this.domainName);

        Collection<String> targetTechLanguages =  getTagEntries(targetObject, "skills", this.languageTag);

        Collection<String> targetFrameworks =  getTagEntries(targetObject, "skills", this.languageTag);

        for (DBObject sourceObject:sourceObjects) {
            int sourceExpInMonths = getExpInMonths(sourceObject);

            double score = 0.0d;

            score += getExperienceScore(targetExpInMonths, sourceExpInMonths);

            Collection<String> sourceDomainSkills =  getTagEntries(sourceObject, "domains", this.domainName);

            score += getScore(targetDomainSkills, sourceDomainSkills, 1.0d);

            Collection<String> sourceTechLanguages =  getTagEntries(sourceObject, "skills", this.languageTag);

            score += getScore(targetTechLanguages, sourceTechLanguages, 3.0d);

            Collection<String> sourceFrameworks =  getTagEntries(sourceObject, "skills", this.languageTag);

            score += getScore(targetFrameworks, sourceFrameworks, 2.0d);

            System.out.println("Score --> " + score);

            matchingScoreMap.put(score, sourceObject);
        }


        return  scoreMap;
    }


    private double getScore(Collection<String> targetSkills, Collection<String> sourceSkills, double weight) {

        final AtomicInteger matchCount = new AtomicInteger();  // I know this is bad but its 2:30 am in morning.

        sourceSkills.forEach(domainSkill -> {
            if(targetSkills.contains(domainSkill)){
                matchCount.incrementAndGet();
            }
        });

        // simple %
        return (matchCount.intValue()* weight / sourceSkills.size());
    }

    private double getExperienceScore(int targetExp, int sourceExp) {
        double score = 0.0d;
        if(targetExp > sourceExp) {
             if(targetExp - sourceExp > 24) {
                 score = 4.0;
             } else {
                 score = 3.0;
             }
        } else {
            if(sourceExp - targetExp <=2) {
                score = 2.0;
            } else {
                score = 1.0;
            }
        }

        return score;
    }



    private Collection<String> getTagEntries(DBObject object, String mainTagName, String tagName) {

        Collection<String> domainEntries = new HashSet<>();

        BasicDBObject domainObject = (BasicDBObject) object.get(mainTagName);

        if(domainObject != null && domainObject.containsField(tagName)) {
            BasicDBList domainTerms = (BasicDBList) domainObject.get(tagName);
            for (Object domainTerm : domainTerms) {
                domainEntries.add(domainTerm.toString());
            }
        }
        return domainEntries;
    }

    private int getExpInMonths(DBObject object) {
        int expInMonths = 0;

        BasicDBList durations = (BasicDBList) object.get("durations");

        for (Object duration : durations) {
            expInMonths = WordToNumberUtil.getExpInMonths((String) duration) ;
            if(expInMonths > 0) {
                break;
            }
        }

        return expInMonths;
    }

}
