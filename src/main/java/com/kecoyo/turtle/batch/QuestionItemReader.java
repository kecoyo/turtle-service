package com.kecoyo.turtle.batch;

import org.springframework.batch.item.database.JdbcCursorItemReader;

import com.kecoyo.turtle.domain.Question;

public class QuestionItemReader extends JdbcCursorItemReader<Question> {

}
