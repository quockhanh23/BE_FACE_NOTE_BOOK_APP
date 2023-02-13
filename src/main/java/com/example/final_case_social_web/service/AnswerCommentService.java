package com.example.final_case_social_web.service;

import com.example.final_case_social_web.model.AnswerComment;

import java.util.List;

public interface AnswerCommentService extends GeneralService<AnswerComment> {

    void delete(AnswerComment entity);

    void create(AnswerComment answerComment);

    void saveAll(List<AnswerComment> answerComments);

    List<AnswerComment> findAllByCommentIdAndDeleteAtIsNull(Long commentId);

    List<AnswerComment> findAllByDeleteAtIsNull();

    List<AnswerComment> findAllByCommentIdIn(List<Long> comment_id);

}
