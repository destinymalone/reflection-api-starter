package org.basecampcodingacademy.reflections.controllers;

import org.basecampcodingacademy.reflections.db.ReflectionRepository;
import org.basecampcodingacademy.reflections.db.ResponseRepository;
import org.basecampcodingacademy.reflections.domain.Answer;
import org.basecampcodingacademy.reflections.domain.Response;
import org.basecampcodingacademy.reflections.domain.Response;
import org.basecampcodingacademy.reflections.exceptions.ResponseForNonExistingReflection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/reflections/{reflectionId}/responses")
public class ResponseController {
    @Autowired
    public ResponseRepository responses;

    @Autowired
    public ReflectionRepository reflections;

    @GetMapping
    public List<Response> index(Response response, @PathVariable Integer reflectionId) {
        response.reflectionId = reflectionId;
        return (List<Response>) responses.getOne(response);
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Response create(@RequestBody Response response, @PathVariable Integer reflectionId) throws ResponseForNonExistingReflection {
        response.reflectionId = reflectionId;
        if (!Objects.isNull(reflections.find(reflectionId))) {
            return responses.create(response);
        }
        throw new ResponseForNonExistingReflection(response.reflectionId);
    }

//    @GetMapping("/today")
//    public Response today() {
//        var response =  responses;
//        return response;
//    }

    @PatchMapping("/{id}")
    public Response update(@PathVariable Integer reflectionId,@PathVariable Integer id, @RequestBody Response response) {
        response.id = id;
        response.reflectionId = reflectionId;
        return responses.update(response);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        responses.delete(id);
    }

    @ExceptionHandler ({ ResponseForNonExistingReflection.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleResponseForNonExistingReflectionException(ResponseForNonExistingReflection ex) {
        var errorMap = new HashMap<String, String>();
        errorMap.put("error", "Reflection " + ex.reflectionId.toString() + " does not exist");
        return errorMap;
    }
}
