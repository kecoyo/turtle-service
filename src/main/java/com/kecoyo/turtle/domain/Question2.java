package com.kecoyo.turtle.domain;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Question2 implements Serializable {
    @JsonProperty("question_id")
    private Long questionId;
    @JsonProperty("main_id")
    private Long mainId;
    @JsonProperty("stage_name")
    private String stageName;
    @JsonProperty("subject_name")
    private String subjectName;
    @JsonProperty("qst_type")
    private String qstType;
    private Integer difficulty;
    private String knowledges;
    private Integer status;
}
