package com.example.final_case_social_web.service.impl;

import com.example.final_case_social_web.model.AnswerComment;
import com.example.final_case_social_web.repository.AnswerCommentRepository;
import com.example.final_case_social_web.service.AnswerCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AnswerCommentServiceImpl implements AnswerCommentService {
    @Autowired
    private AnswerCommentRepository answerCommentRepository;

    @Override
    public Iterable<AnswerComment> findAll() {
        return answerCommentRepository.findAll();
    }

    @Override
    public Optional<AnswerComment> findById(Long id) {
        return answerCommentRepository.findById(id);
    }

    @Override
    public AnswerComment save(AnswerComment answerComment) {
        return answerCommentRepository.save(answerComment);
    }

    @Override
    public void delete(AnswerComment entity) {
        answerCommentRepository.delete(entity);
    }

    @Override
    public void create(AnswerComment answerComment) {
        answerComment.setCreateAt(LocalDateTime.now());
        answerComment.setDelete(false);
        answerComment.setEditAt(null);
    }

    @Override
    public void saveAll(List<AnswerComment> answerComments) {
        answerCommentRepository.saveAll(answerComments);
    }

    @Override
    public List<AnswerComment> findAllByCommentIdAndDeleteAtIsNull(Long commentId) {
        return answerCommentRepository.findAllByCommentIdAndDeleteAtIsNull(commentId);
    }

    @Override
    public List<AnswerComment> findAllByDeleteAtIsNull() {
        return answerCommentRepository.findAllByDeleteAtIsNull();
    }

    @Override
    public List<AnswerComment> findAllByCommentIdIn(List<Long> comment_id) {
        return answerCommentRepository.findAllByCommentIdInAndDeleteAtIsNull(comment_id);
    }
}
