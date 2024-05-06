package com.cecilireid.springchallenges;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Endpoint(id = "cateringJobs")
public class CateringJobsEndpoint {
    private final CateringJobRepository cateringJobRepository;

    public CateringJobsEndpoint(CateringJobRepository cateringJobRepository) {
        this.cateringJobRepository = cateringJobRepository;
    }

    /*
    @ReadOperation
    @Bean
    public Map<String, List<CateringJob>> cateringJobs() {
        Map<String, List<CateringJob>> cateringJobsPerStatus = new HashMap<>();
        for (Status status: Status.values()) {
            cateringJobsPerStatus.put(status.toString(), cateringJobRepository.findByStatus(status));
        }
        return cateringJobsPerStatus;
    }*/
    @ReadOperation
    @Bean
    public Map<String, Long> cateringJobs() {
        Map<String, Long> cateringJobsPerStatus = new HashMap<>();
        for (Status status: Status.values()) {
            cateringJobsPerStatus.put(status.toString(), (long) cateringJobRepository.findByStatus(status).size());
        }
        return cateringJobsPerStatus;
    }
}
