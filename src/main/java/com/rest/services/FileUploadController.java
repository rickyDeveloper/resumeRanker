package com.rest.services;

import com.rest.json.HelloWorld;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by vikasnaiyar on 18/09/17.
 */
@Controller
@RequestMapping("/files")
@Slf4j
public class FileUploadController {

    @PostMapping("/resume")
    @ResponseBody
    public HelloWorld handleResumeUpload(@RequestParam("file") MultipartFile file) throws IOException {
        log.info("Received file " + file.getBytes());

        InputStream stream = file.getInputStream();  //new FileInputStream(convFile);
        //parse method parameters
        Parser parser = new AutoDetectParser();
        BodyContentHandler handler = new BodyContentHandler();
        Metadata metadata = new Metadata();
        ParseContext context = new ParseContext();
        //parsing the file
        try {
            parser.parse(stream, handler, metadata, context);
            log.info(handler.toString());
        } catch (TikaException | IOException | SAXException e) {
                e.printStackTrace();
        }

        return new HelloWorld(1, "Received file");
    }

}
