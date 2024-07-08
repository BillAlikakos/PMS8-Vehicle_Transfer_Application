package com.example.demo;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
@EntityScan(basePackages = {"com.example.demo.datatypes","com.example.demo.db"})
@EnableJpaRepositories(basePackages =  {"com.example.demo.datatypes","com.example.demo.db"})
@ComponentScan(basePackages = {"com.example.demo.service","com.example.demo"})
public class DemoApplication implements ApplicationListener<ApplicationReadyEvent>
{

	public static void main(String[] args)
	{
		TimeZone.setDefault(TimeZone.getTimeZone("Europe/Athens"));
		SpringApplication.run(DemoApplication.class, args);
	}
	
    @PostConstruct
    public void init(){
    }
    
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent)
    {
    	objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.setDateFormat(new SimpleDateFormat("dd-MM-yyyy"));
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Europe/Athens"));
        objectMapper.setDateFormat(dateFormat);
        objectMapper.setTimeZone(TimeZone.getTimeZone("Europe/Athens"));
    }

}
