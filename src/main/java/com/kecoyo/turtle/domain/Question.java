package com.kecoyo.turtle.domain;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "试题")
@TableName("tk_question")
public class Question implements Serializable {

    @Schema(description = "question_id")
    @TableId(value = "question_id", type = IdType.INPUT)
    private Long questionId;

    @TableField("main_id")
    private Long mainId;

    @TableField("stage_name")
    private String stageName;

    @TableField("subject_name")
    private String subjectName;

    @TableField("qst_type")
    private String qstType;

    @TableField("difficulty")
    private Integer difficulty;

    @TableField("knowledges")
    private String knowledges;

    @TableField("status")
    private Integer status;

    @Override
    public String toString() {
        return "Question{" + "questionId=" + questionId + ", mainId=" + mainId + ", stageName='" + stageName + '\''
                + ", subjectName='" + subjectName + '\'' + ", qstType='" + qstType + '\'' + ", difficulty=" + difficulty
                + ", knowledges='" + knowledges + '\'' + ", status=" + status + '}';
    }
}
