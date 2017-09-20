package com.resume.ranker;

import com.mongodb.*;
import com.mongodb.util.JSON;
import com.rest.json.Rank;
import com.rest.util.WordToNumberUtil;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;

import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by vikasnaiyar on 17/09/17.
 */
@Slf4j
public class ResumeRanker {

    private String domainName;

    private String languageTag;

    private String frameworkTag;

    public ResumeRanker(String domainName, String languageTag, String frameworkTag) {
        this.domainName = domainName;
        this.frameworkTag= frameworkTag;
        this.languageTag = languageTag;
    }

    public List<Rank> rankResume(DBObject targetObject, Collection<DBObject> sourceObjects) {
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
        //Map<Double, DBObject> matchingScoreMap = new HashMap<>();
        //Map<DBObject, Map<Double,DBObject>> scoreMap = new HashMap<>();
        List<Rank> ranks = new ArrayList<>();

        //scoreMap.put(targetObject, matchingScoreMap);

        int targetExpInMonths = getExpInMonths(targetObject);

        Collection<String> targetDomainSkills =  getTagEntries(targetObject, "domains", this.domainName);

        Collection<String> targetTechLanguages =  getTagEntries(targetObject, "skills", this.languageTag);

        Collection<String> targetFrameworks =  getTagEntries(targetObject, "skills", this.frameworkTag);

        for (DBObject sourceObject:sourceObjects) {
            int sourceExpInMonths = getExpInMonths(sourceObject);

            ObjectId id = (ObjectId)sourceObject.get( "_id" );

            double score = 0.0d;

            score += getExperienceScore(targetExpInMonths, sourceExpInMonths);

            Collection<String> sourceDomainSkills =  getTagEntries(sourceObject, "domains", this.domainName);

            double domainPerc = getScore(targetDomainSkills, sourceDomainSkills, 1.0d);

            log.info("domainPerc for object {} is {} ", id.toString(), domainPerc);

            score +=domainPerc;

            Collection<String> sourceTechLanguages =  getTagEntries(sourceObject, "skills", this.languageTag);

            double skillsPerc = getScore(targetTechLanguages, sourceTechLanguages, 3.0d);

            log.info("skillsPerc for object {} is {} ", id.toString(), skillsPerc);

            score += skillsPerc;

            Collection<String> sourceFrameworks =  getTagEntries(sourceObject, "skills", this.frameworkTag);

            skillsPerc = getScore(targetFrameworks, sourceFrameworks, 2.0d);

            log.info("skillsPerc for object {} is {} ", id.toString(), skillsPerc);

            score += skillsPerc;

            log.info("Score wrt to docId {} is {}--> " , id, score);

            ((BasicDBObject) sourceObject).remove("interestedTerms");

           // matchingScoreMap.put(score, sourceObject);
            ranks.add(new Rank(score,sourceObject));
        }

        Collections.sort(ranks);

        return  ranks;
    }


    private double getScore(Collection<String> targetSkills, Collection<String> sourceSkills, double weight) {

        final AtomicInteger matchCount = new AtomicInteger(0);  // I know this is bad but its 2:30 am in morning.

        sourceSkills.forEach(sourceSkill -> {
            targetSkills.forEach(targetSkill -> {
                     if (sourceSkill.toLowerCase().contains(targetSkill.toLowerCase()) || targetSkill.toLowerCase().contains(sourceSkill.toLowerCase())) {
                         matchCount.getAndIncrement();
                     }
            });
        });

        double weightPerc = 0.0d;
        if(sourceSkills.size() > 0 && targetSkills.size() >0) {
            if(sourceSkills.size() > targetSkills.size()) {
                weightPerc = (matchCount.intValue()* weight / sourceSkills.size());
            } else {
                weightPerc = (matchCount.intValue()* weight / targetSkills.size());
            }
        }

        // simple %
        return weightPerc;
    }

    private double getExperienceScore(int targetExp, int sourceExp) {
        double score = 0.0d;
        if(targetExp >= sourceExp) {
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

        log.info("Score for exp is {} for targetExp {}  and sourceExp  {}--> " ,  score, targetExp, sourceExp);

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
        ObjectId id = (ObjectId)object.get( "_id" );

        if(durations != null) {
            for (Object duration : durations) {
                expInMonths = WordToNumberUtil.getExpInMonths((String) duration);
                if (expInMonths > 0) {
                    break;
                }
            }
            log.info("Experience for object {} is {} months", id.toString(), expInMonths);
        } else {
            log.error("Error fetching exp for object {}", id.toString());
        }

        return expInMonths;
    }

}
