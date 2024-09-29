package com.kecoyo.turtle.batch;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kecoyo.turtle.domain.Question;
import com.kecoyo.turtle.service.QuestionService;

// @Component
public class QuestionItemWriter implements ItemWriter<Question> {

    @Autowired
    private QuestionService questionService;

    @Override
    public void write(Chunk<? extends Question> chunk) throws Exception {
        for (Question question : chunk) {
            questionService.saveOrUpdate(question);
        }
    }

}
