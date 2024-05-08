package com.cecilireid.springchallenges;

import org.springframework.http.HttpStatus;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.lang.reflect.Field;
import java.util.*;


@RestController
@RequestMapping("cateringJobs")
public class CateringJobController extends WebExceptions{
    private static final String IMAGE_API = "https://foodish-api.herokuapp.com";
    private final CateringJobRepository cateringJobRepository;
    WebClient client;

    public CateringJobController(CateringJobRepository cateringJobRepository, WebClient.Builder webClientBuilder) {
        this.cateringJobRepository = cateringJobRepository;
    }

    @GetMapping
    @ResponseBody
    public List<CateringJob> getCateringJobs() {
        return cateringJobRepository.findAll();
    }

    @GetMapping("/{id}")
    @ResponseBody
    public CateringJob getCateringJobById(@PathVariable Long id) {
        if (cateringJobRepository.existsById(id)) {
            return cateringJobRepository.findById(id).get();
        } else {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/findByStatus")
    @ResponseBody
    public List<CateringJob> getCateringJobsByStatus(@RequestParam String status) {
        try{
            return cateringJobRepository.findByStatus(Status.valueOf(status.toUpperCase()));
        }
        catch (IllegalArgumentException e){
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public CateringJob createCateringJob(@RequestBody CateringJob cateringJob) {
        if(cateringJob.getStatus() == null){
            cateringJob.setStatus(Status.valueOf("NOT_STARTED"));
        }
        return cateringJobRepository.save(cateringJob);
    }

    @PutMapping("/{id}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public CateringJob updateCateringJob(@RequestBody CateringJob cateringJob, @PathVariable Long id) {
        if (!cateringJobRepository.existsById(id)) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
        }
        if(cateringJob.getId() == null){
            cateringJob.setId(id);
        }
        if (!id.equals(cateringJob.getId())){
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
        }
        if(cateringJob.getStatus() == null){
            cateringJob.setStatus(Status.valueOf("NOT_STARTED"));
        }
        return cateringJobRepository.save(cateringJob);
    }

    @PatchMapping("/{id}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public CateringJob patchCateringJob(@RequestBody Map<String, Object> fields, @PathVariable Long id) {
        if(fields.get(id) != null){
            if(fields.get(id) instanceof Long){
                if(Long.parseLong(fields.get(id).toString()) == id){
                    throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
                }
            }
        }

        if (!cateringJobRepository.existsById(id)) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
        }
        CateringJob cateringJob = cateringJobRepository.findById(id).get();
        fields.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(CateringJob.class, key);
            try{field.setAccessible(true);
                ReflectionUtils.setField(field, cateringJob, value);
            }
            catch (NullPointerException|IllegalArgumentException e){
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
            }

        });
        return cateringJobRepository.save(cateringJob);
    }

    public Mono<String> getSurpriseImage() {
        return null;
    }
}
