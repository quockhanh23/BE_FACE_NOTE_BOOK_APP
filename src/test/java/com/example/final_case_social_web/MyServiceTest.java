package com.example.final_case_social_web;

import com.example.final_case_social_web.common.Constants;
import com.example.final_case_social_web.dto.ListAvatarDefault;
import com.example.final_case_social_web.model.AnswerComment;
import com.example.final_case_social_web.model.User;
import com.example.final_case_social_web.repository.UserRepository;
import com.example.final_case_social_web.service.AnswerCommentService;
import com.example.final_case_social_web.service.UserService;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class MyServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @Mock
    private AnswerCommentService answerCommentService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindById() {
        Long id = 1L;
        User user = new User();
        user.setId(id);
        Mockito.when(userService.findById(id)).thenReturn(Optional.of(user));
    }

    @Test
    public void testFindById2() {
        User user = new User();
        user.setId(1L);
        Mockito.when(userService.findById(2L)).thenReturn(null);
    }

    @Test
    public void testCreate() {
        AnswerComment answerComment = new AnswerComment();
        answerComment.setContent("1");
        Mockito.doNothing().when(answerCommentService).create(answerComment);
        assertEquals("1", answerComment.getContent());
    }

    @Test
    public void testCreateUser() {
        User user = new User();
        Mockito.doNothing().when(userService).createDefault(user);
        assertEquals(Constants.ImageDefault.DEFAULT_BACKGROUND_2, user.getAvatar());
    }

    @Test
    public void listImage() {
        List<ListAvatarDefault> listImageDefault = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            ListAvatarDefault avatarDefault = new ListAvatarDefault();
            listImageDefault.add(avatarDefault);
        }
        Mockito.when(userService.listAvatar()).thenReturn(listImageDefault);
    }
}
