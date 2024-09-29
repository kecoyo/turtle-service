package com.kecoyo.turtle.batch;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.kecoyo.turtle.domain.Question;

@Component
public class QuestionItemProcessor implements ItemProcessor<Question, Question> {

    @Override
    public Question process(Question question) throws Exception {
        final Integer questionId = question.getQuestionId();
        final Integer mainId = question.getMainId();

        final Question transformedQuestion = new Question();
        transformedQuestion.setQuestionId(questionId);
        transformedQuestion.setMainId(mainId);

        return transformedQuestion;
    }
}
