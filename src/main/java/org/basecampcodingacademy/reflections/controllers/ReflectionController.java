package org.basecampcodingacademy.reflections.controllers;

import org.basecampcodingacademy.reflections.db.QuestionRepository;
import org.basecampcodingacademy.reflections.db.ReflectionRepository;
import org.basecampcodingacademy.reflections.domain.Reflection;
import org.basecampcodingacademy.reflections.exceptions.OneReflectionPerDay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reflections")
public class ReflectionController {
    @Autowired
    public ReflectionRepository reflections;

    @Autowired
    public QuestionRepository questions;

    @GetMapping
    public List<Reflection> index() {
        return reflections.all();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Reflection create(@RequestBody Reflection reflection) throws OneReflectionPerDay {
        if (reflection_exists(reflection)) {
            throw new OneReflectionPerDay(reflection.date);
        }
        return reflections.create(reflection);
    }

    public boolean reflection_exists(Reflection reflection) throws OneReflectionPerDay {
        if (reflection.date == reflection.date) {
            return true;
        } else {
//            System.out.println("Something didn't work");
            return false;
        }
    }
//    @GetMapping
//    public static boolean reflection_exists(Reflection reflection, LocalDate date){
//        var reflections = Reflection(reflection, date) return Reflection reflection);
//        return Boolean.parseBoolean(reflections);
//    }


    @GetMapping("/today")
    public Reflection today(@RequestParam(defaultValue= "") String include) {
        var reflection =  reflections.find(LocalDate.now());
        if (include.equals("questions")) {
            reflection.questions = questions.forReflection(reflection.id);
        }
        return reflection;
    }

    @PatchMapping("/{id}")
    public Reflection update(@PathVariable Integer id, @RequestBody Reflection reflection) {
        reflection.id = id;
        return reflections.update(reflection);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        reflections.delete(id);
    }

    @ExceptionHandler ({ OneReflectionPerDay.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleOneReflectionPerDayException(OneReflectionPerDay ex) {
        var errorMap = new HashMap<String, String>();
        errorMap.put("error", "Reflection for " + ex.date.toString() + " already exists");
        return errorMap;
    }
}
