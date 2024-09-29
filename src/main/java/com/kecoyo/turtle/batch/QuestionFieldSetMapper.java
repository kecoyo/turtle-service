package com.kecoyo.turtle.batch;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

import com.kecoyo.turtle.domain.Question;

public class QuestionFieldSetMapper implements FieldSetMapper<Question> {

    @Override
    public Question mapFieldSet(FieldSet fs) {

        if (fs == null) {
            return null;
        }

        Question question = new Question();
        question.setQuestionId(fs.readLong("question_id"));
        question.setMainId(fs.readLong("main_id"));
        question.setStageName(fs.readString("stage_name"));
        question.setSubjectName(fs.readString("subject_name"));

        return question;
    }

}
