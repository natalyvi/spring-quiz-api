package com.cooksys.quiz_api.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class QuizRequestDto {
    private String name;

    private List<QuestionRequestDto> questions;
}
