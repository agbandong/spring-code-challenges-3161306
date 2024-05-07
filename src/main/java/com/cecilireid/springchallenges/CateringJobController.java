package com.cecilireid.springchallenges;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.NoSuchElementException;


@RestController
@RequestMapping("cateringJobs")
public class CateringJobController {
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
    public CateringJob patchCateringJob(@RequestBody JsonPatch json, @PathVariable Long id) {
        try{CateringJob cateringJob =
                cateringJobRepository.findById(id).orElseThrow();
            CateringJob cateringJobPatched = applyPatchToJob(json, cateringJob);
            return cateringJobRepository.save(cateringJobPatched);
        }
        catch (JsonPatchException | JsonProcessingException e) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);}
        catch (NoSuchElementException e){
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
        }
    }

    private CateringJob applyPatchToJob(
            JsonPatch patch, CateringJob targetCateringJob) throws JsonPatchException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode patched = patch.apply(objectMapper.convertValue(targetCateringJob, JsonNode.class));
        return objectMapper.treeToValue(patched, CateringJob.class);
    }

    public Mono<String> getSurpriseImage() {
        return null;
    }
}
