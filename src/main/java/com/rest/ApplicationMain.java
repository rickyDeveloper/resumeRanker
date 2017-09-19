package com.rest;

import com.dao.TagsDao;
import com.dao.TagsDaoImpl;
import com.resume.nlp.Nlp;
import com.resume.ranker.ResumeRanker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.net.UnknownHostException;

/**
 * Created by vikasnaiyar on 17/09/17.
 */
@SpringBootApplication
public class ApplicationMain implements CommandLineRunner{

    public static void main(String[] args) {
    		Nlp.init();
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



    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**");
            }
        };
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
