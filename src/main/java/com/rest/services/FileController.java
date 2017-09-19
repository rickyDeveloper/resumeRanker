package com.rest.services;

import com.dao.TagsDao;
import com.dao.TagsDaoImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import com.rest.json.HelloWorld;
import com.resume.nlp.Nlp;
import com.resume.ranker.ResumeRanker;
import com.resume.reader.ResumeSectionReader;
import com.resume.reader.ResumeSectionToResumeExcerptMapper;

import lombok.extern.slf4j.Slf4j;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import javax.json.Json;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.Map;

/**
 * Created by vikasnaiyar on 18/09/17.
 */
@Controller
@RequestMapping("/files")
@Slf4j
public class FileController {

    @Autowired
    @Qualifier("resumeDao")
    private TagsDao<String,DBObject> resumeDao;

    @Autowired
    @Qualifier("jobDescriptionDao")
    private TagsDao<String,DBObject> jobDescriptionDao;


    @Autowired
    @Qualifier("resumeRanker")
    private ResumeRanker resumeRanker;

    @PostMapping("/resume")
    @ResponseBody
    public String handleResumeUpload(@RequestParam("file") MultipartFile file) throws IOException {
        log.info("Received resume file " + file.getBytes());

        InputStream stream = file.getInputStream();
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> map = objectMapper.convertValue(
        		new ResumeSectionToResumeExcerptMapper()
        		.apply(new ResumeSectionReader().apply(stream)),
        				Map.class);
        return resumeDao.saveTag(map).toString();
    }

    @GetMapping("resume/{id}")
    @ResponseBody
    public String getResume(@PathVariable String id){
        return "Sending the resume";
    }

    @GetMapping("resume/{id}/rank")
    @ResponseBody
    public Map<DBObject, Map<Double,DBObject>> getResumeRank(@PathVariable String id){

        DBObject resumeTag = resumeDao.getTag(id);

        Collection<DBObject> jdTags = jobDescriptionDao.getAllTags();

        return resumeRanker.rankResume(resumeTag,jdTags);
    }

    @GetMapping("jd/{id}")
    @ResponseBody
    public String getJobDescription(@PathVariable String id){
        return "Sending the JD";
    }


    @PostMapping("/jd")
    @ResponseBody
    public String handleJDUpload(@RequestParam("file") MultipartFile file) throws IOException {
        log.info("Received jd file " + file.getBytes());

        InputStream stream = file.getInputStream();
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> map = objectMapper.convertValue(
        		new ResumeSectionToResumeExcerptMapper()
        		.apply(new ResumeSectionReader().apply(stream)),
        				Map.class);
        return jobDescriptionDao.saveTag(map).toString();
    }



}
