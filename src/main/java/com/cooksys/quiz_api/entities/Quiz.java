package com.cooksys.quiz_api.entities;

import java.util.List;

import javax.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
public class Quiz {

  @Id
  @GeneratedValue
  private Long id;

  private String name;

  @OneToMany(mappedBy = "quiz", fetch = FetchType.EAGER)
  private List<Question> questions;

}
