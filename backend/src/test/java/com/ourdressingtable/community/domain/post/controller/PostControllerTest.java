package com.ourdressingtable.community.domain.post.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ourdressingtable.community.post.controller.PostController;
import com.ourdressingtable.community.post.dto.CreatePostRequest;
import com.ourdressingtable.community.post.service.PostService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@ActiveProfiles("test")
@WebMvcTest(controllers = PostController.class)
@DisplayName("Post 기능 테스트")
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostService postService;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("게시글 작성_성공")
    @Test
    public void createPost_withValidData_ReturnSuccess() throws Exception {
        // given
        CreatePostRequest createPostRequest = CreatePostRequest.builder()
                .title("제목")
                .content("내용")
                .build();



    }
}
