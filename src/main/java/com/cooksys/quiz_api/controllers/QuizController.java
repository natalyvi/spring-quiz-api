package com.cooksys.quiz_api.controllers;

import java.util.List;

import com.cooksys.quiz_api.dtos.QuestionRequestDto;
import com.cooksys.quiz_api.dtos.QuestionResponseDto;
import com.cooksys.quiz_api.dtos.QuizRequestDto;
import com.cooksys.quiz_api.dtos.QuizResponseDto;
import com.cooksys.quiz_api.services.QuizService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/quiz")
public class QuizController {

    private final QuizService quizService;

    @GetMapping
    public List<QuizResponseDto> getAllQuizzes() {
        return quizService.getAllQuizzes();
    }

    @PostMapping
    public ResponseEntity<QuizResponseDto> createQuiz(@RequestBody QuizRequestDto quizRequestDto) {
        return quizService.createQuiz(quizRequestDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<QuizResponseDto> deleteQuiz(@PathVariable String id) {
        return quizService.deleteQuizById(Long.parseLong(id));
    }

    @PatchMapping("/{id}/rename/{newName}")
    public ResponseEntity<QuizResponseDto> renameQuiz(@PathVariable String id, @PathVariable String newName) {
        return quizService.renameQuizById(Long.parseLong(id), newName);
    }

    @GetMapping("/{id}/random")
    public ResponseEntity<QuestionResponseDto> getRandomQuestionFromQuiz(@PathVariable String id) {
        return quizService.getRandomQuestionFromQuizById(Long.parseLong(id));
    }

    @PatchMapping("/{id}/add")
    public ResponseEntity<QuizResponseDto> addQuestionToQuiz(
            @PathVariable String id,
            @RequestBody QuestionRequestDto questionRequestDto
    ) {
        return quizService.addQuestionToQuiz(Long.parseLong(id), questionRequestDto);
    }

    @DeleteMapping("/{id}/delete/{questionID}")
    public ResponseEntity<QuestionResponseDto> deleteQuestionFromQuiz(
            @PathVariable String id,
            @PathVariable String questionID
    ){
        return quizService.deleteQuestionFromQuiz(Long.parseLong(id), Long.parseLong(questionID));
    }


    // TODO: Implement the remaining 6 endpoints from the documentation.


}
