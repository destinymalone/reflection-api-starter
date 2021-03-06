package org.basecampcodingacademy.reflections.controllers;

import org.basecampcodingacademy.reflections.db.AnswerRepository;
import org.basecampcodingacademy.reflections.domain.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AnswerController {
    @Autowired
    public AnswerRepository answers;

    @GetMapping("/responses/{responseId}/answers")
    public List<Answer> index(Answer answer, @PathVariable Integer responseId) {
        answer.responseId = responseId;
        return (List<Answer>) answers.find(answer);
    }


    @PostMapping("/responses/{responseId}/answers")
    @ResponseStatus(HttpStatus.CREATED)
    public Answer create(@RequestBody Answer answer, @PathVariable Integer responseId) {
        answer.responseId = responseId;
        return answers.create(answer);
    }

    @PatchMapping("/answers/{id}")
    public Answer update(@PathVariable Integer id, @RequestBody Answer answer) {
        answer.id = id;
        return answers.update(answer);
    }

    @GetMapping("/answers/{id}")
    public Answer reflectionAnswer(Answer answer, @PathVariable Integer id) {
        answer.id = id;
        return answers.singleAnswer(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        answers.delete(id);
    }
}
