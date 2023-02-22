package com.cooksys.quiz_api.services;

import java.util.List;

import com.cooksys.quiz_api.dtos.QuestionRequestDto;
import com.cooksys.quiz_api.dtos.QuestionResponseDto;
import com.cooksys.quiz_api.dtos.QuizRequestDto;
import com.cooksys.quiz_api.dtos.QuizResponseDto;
import org.springframework.http.ResponseEntity;

public interface QuizService {

  List<QuizResponseDto> getAllQuizzes();

  ResponseEntity<QuizResponseDto> createQuiz(QuizRequestDto quizRequestDto);

  ResponseEntity<QuizResponseDto> deleteQuizById(Long id);

  ResponseEntity<QuizResponseDto> renameQuizById(Long id, String newName);

  ResponseEntity<QuestionResponseDto> getRandomQuestionFromQuizById(Long id);

  ResponseEntity<QuizResponseDto> addQuestionToQuiz(Long id, QuestionRequestDto questionRequestDto);

  ResponseEntity<QuestionResponseDto> deleteQuestionFromQuiz(Long id, Long questionID);
}
