package com.cooksys.quiz_api.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.cooksys.quiz_api.dtos.QuestionRequestDto;
import com.cooksys.quiz_api.dtos.QuestionResponseDto;
import com.cooksys.quiz_api.dtos.QuizRequestDto;
import com.cooksys.quiz_api.dtos.QuizResponseDto;
import com.cooksys.quiz_api.entities.Answer;
import com.cooksys.quiz_api.entities.Question;
import com.cooksys.quiz_api.entities.Quiz;
import com.cooksys.quiz_api.mappers.QuestionMapper;
import com.cooksys.quiz_api.mappers.QuizMapper;
import com.cooksys.quiz_api.repositories.AnswerRepository;
import com.cooksys.quiz_api.repositories.QuestionRepository;
import com.cooksys.quiz_api.repositories.QuizRepository;
import com.cooksys.quiz_api.services.QuizService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {

    private final QuizRepository quizRepository;

    private final QuestionRepository questionRepository;
    private final QuizMapper quizMapper;
    private final QuestionMapper questionMapper;

    private final AnswerRepository answerRepository;


    @Override
    public List<QuizResponseDto> getAllQuizzes() {
        return quizMapper.entitiesToDtos(quizRepository.findAll());
    }

    @Override
    public ResponseEntity<QuizResponseDto> createQuiz(QuizRequestDto quizRequestDto) {
        /*if (quizRequestDto.getName() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }*/
        Quiz quizToSave = quizMapper.dtoToEntity(quizRequestDto);
        quizRepository.saveAndFlush(quizToSave);

        List<Question> questionsToSave = quizToSave.getQuestions();

        for (Question question :
                questionsToSave) {
            question.setQuiz(quizToSave);
            questionRepository.saveAndFlush(question);
            List<Answer> answers = question.getAnswers();
            for (Answer answer :
                    answers) {
                answer.setQuestion(question);
                answerRepository.saveAndFlush(answer);
            }
        }

        return new ResponseEntity<>(quizMapper.entityToDto(quizToSave), HttpStatus.OK);

    }

    @Override
    public ResponseEntity<QuizResponseDto> deleteQuizById(Long id) {
        if (!quizRepository.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Quiz quizToDelete = quizRepository.getById(id);
        quizRepository.deleteById(id);
        return new ResponseEntity<>(quizMapper.entityToDto(quizToDelete), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<QuizResponseDto> renameQuizById(Long id, String newName) {
        if (!quizRepository.existsById(id) || newName.equals(quizRepository.getById(id).getName())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Quiz quizToPatch = quizRepository.getById(id);
        quizToPatch.setName(newName);
        return new ResponseEntity<>(quizMapper.entityToDto(quizRepository.saveAndFlush(quizToPatch)), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<QuestionResponseDto> getRandomQuestionFromQuizById(Long id) {
        if (!quizRepository.existsById(id) || quizRepository.getById(id).getQuestions().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Quiz quizById = quizRepository.getById(id);
        List<Question> questionsFromQuiz = quizById.getQuestions();
        Random random = new Random();
        Question randomQuestion = questionsFromQuiz.get(random.nextInt(questionsFromQuiz.size()));

        return new ResponseEntity<>(questionMapper.entityToDto(randomQuestion), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<QuizResponseDto> addQuestionToQuiz(Long id, QuestionRequestDto questionRequestDto) {
        if (questionRequestDto.getText() == null || !quizRepository.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        // Retrieve the quiz, question and its answers
        Quiz quizById = quizRepository.getById(id);
        Question questionToAdd = questionMapper.dtoToEntity(questionRequestDto);
        List<Answer> answersToAdd = questionToAdd.getAnswers();

        // Make necessary changes to them
        List<Question> questions;
        if (quizById.getQuestions() == null) {
            questions = new ArrayList<>();
        } else {
            questions = quizById.getQuestions();
        }
        questions.add(questionToAdd);
        quizById.setQuestions(questions);
        quizRepository.saveAndFlush(quizById);

        questionToAdd.setQuiz(quizById);
        questionRepository.saveAndFlush(questionToAdd);

        for (Answer answer: answersToAdd) {
            answer.setQuestion(questionToAdd);
            answerRepository.saveAndFlush(answer);
        }

        return new ResponseEntity<>(quizMapper.entityToDto(quizById), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<QuestionResponseDto> deleteQuestionFromQuiz(Long id, Long questionID) {
        if (!quizRepository.existsById(id) || !questionRepository.existsById(questionID)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Quiz quizById = quizRepository.getById(id);
        Question questionToDelete = questionRepository.getById(questionID);
        if (!quizById.getQuestions().contains(questionToDelete)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        // Remove the question from the list of the quiz' questions
        List<Question> quizByIdQuestions = quizById.getQuestions();
        quizByIdQuestions.remove(questionToDelete);
        quizById.setQuestions(quizByIdQuestions);
        // Remove the quiz from the question
        questionToDelete.setQuiz(null);

        questionRepository.saveAndFlush(questionToDelete);
        quizRepository.saveAndFlush(quizById);

        return new ResponseEntity<>(questionMapper.entityToDto(questionToDelete), HttpStatus.OK);
    }

}
