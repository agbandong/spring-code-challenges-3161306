package com.cecilireid.springchallenges;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ScheduledTasks {
    private final CateringJobRepository cateringJobRepository;
    public ScheduledTasks(CateringJobRepository cateringJobRepository){
        this.cateringJobRepository = cateringJobRepository;
    }

    private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);
    @Scheduled(fixedRate = 10000)
    public void reportOrderStats() {
        List<CateringJob> jobs= cateringJobRepository.findAll();
        logger.info("We have {} jobs", jobs.size());
        //logger.info("Jobs:{}{}", System.lineSeparator(), jobs);
    }
}
