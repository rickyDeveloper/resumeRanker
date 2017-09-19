package com.rest;

import com.dao.TagsDao;
import com.dao.TagsDaoImpl;
import com.resume.ranker.ResumeRanker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.net.UnknownHostException;

/**
 * Created by vikasnaiyar on 17/09/17.
 */
@SpringBootApplication
public class ApplicationMain implements CommandLineRunner{

    public static void main(String[] args) {
            SpringApplication.run(ApplicationMain.class, args);
    }

    @Bean
    @Qualifier("resumeDao")
    public TagsDao getResumeDao() throws UnknownHostException{
        return new TagsDaoImpl("test","resume");
    }


    @Bean
    @Qualifier("jobDescriptionDao")
    public TagsDao getJobDescriptionDao() throws UnknownHostException{
        return new TagsDaoImpl("test","jd");
    }


    @Bean
    @Qualifier("resumeRanker")
    public ResumeRanker getResumeRanker() throws UnknownHostException{
        return new ResumeRanker("FINANCE", "LANG", "FRAMEWORKS");
    }


    /**
     * Callback used to run the bean.
     *
     * @param args incoming main method arguments
     * @throws Exception on error
     */
    @Override
    public void run(String... args) throws Exception {

    }
}
