package com.springweb.springweb.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springweb.springweb.domain.Post;
import com.springweb.springweb.repository.PostRepository;
import com.springweb.springweb.request.PostCreate;
import com.springweb.springweb.request.PostEdit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
class PostControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void clean() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("/posts 요청시 hello world 출력")
    void test() throws Exception {
        PostCreate request = PostCreate.builder()
                .title("제목입니다")
                .content("내용입니다")
                .build();

        String json = objectMapper.writeValueAsString(request);

        //Expected
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string(""))
                .andDo(print());
        System.out.printf(json);


    }

    @Test
    @DisplayName("/posts 요청시 title값은 필수 출력")
    void test2() throws Exception {


        //Expected
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content("{\"title\": \"\",\"content\" : \"내용입니다.\"}}"))
                .andExpect(status().isBadRequest())
                .andDo(print());


    }

    @Test
    @DisplayName("/posts 요청시 DB에 값이 저장된다.")
    void test3() throws Exception {
        //given


        //when
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content("{\"title\": \"제목입니다\",\"content\" : \"내용입니다\"}"))
                .andExpect(status().isOk())
                .andDo(print());

        //then
        assertEquals(1L,postRepository.count());

        Post post = postRepository.findAll().get(0);
        assertEquals("제목입니다",post.getTitle());
        assertEquals("내용입니다",post.getContent());

    }

    @Test
    @DisplayName("글 하나 조회")
    void test4() throws Exception {
        //given
        Post requestPost = Post.builder()
                .title("제목입니다")
                .content("내용입니다")
                .build();
        postRepository.save(requestPost);

        //when
        mockMvc.perform(get("/posts/{postId}", requestPost.getId())
                 .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(requestPost.getId()))
                .andExpect(jsonPath("$.title").value(requestPost.getTitle()))
                .andExpect(jsonPath("$.content").value("내용입니다"))
                .andDo(print());

        //then

    }

    @Test
    @DisplayName("글 여러개 조회")
    void test5() throws Exception {
        //given
        List<Post> requestPosts = IntStream.range(1,31)
                .mapToObj(i ->
                        Post.builder()
                                .title("호돌맨 제목 " +i)
                                .content("반포자이  " +i)
                                .build()
                ).collect(Collectors.toList());
        postRepository.saveAll(requestPosts);

        //when
        mockMvc.perform(get("/posts?page=0&size=10")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

        //then

    }
    @Test
    @DisplayName("글 제목 수정")
    void test6() throws Exception {
        //given

        Post post =Post.builder()
                .title("호돌맨 제목")
                .content("반포자이 ")
                .build();
        postRepository.save(post);
        PostEdit postEdit = PostEdit.builder()
                .title("호돌걸")
                .content("반포자이")
                .build();

        //when
        mockMvc.perform(patch("/posts/{postId}",post.getId())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postEdit)))
                .andExpect(status().isOk())
                .andDo(print());

        //then

    }

    @Test
    @DisplayName("게시글 삭제")
    void test7() throws Exception {

        Post post = Post.builder()
                .title("호돌맨 제목")
                .content("반포자이 ")
                .build();

        postRepository.save(post);
        //expected
        mockMvc.perform(delete("/posts/{postId}",post.getId())
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

//    @Test
//    @DisplayName("존재하지 않는 게시글 조회")
//    void test9() throws Exception {
//        mockMvc.perform(delete("/posts/{postId}",1L)
//                        .contentType(APPLICATION_JSON))
//                .andExpect(status().isBadRequest())
//                .andDo()
//                .andDo(print());
//    }

    @Test
    @DisplayName("존재하지 않는 게시글 조회")
    void test10() throws Exception {
        PostEdit postEdit = PostEdit.builder()
                .title("호돌걸")
                .content("반포자이")
                .build();

        //when
        mockMvc.perform(patch("/posts/{postId}",1L)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postEdit)))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("게사굴 작성시 바보는 포함 될 수 없다..")
    void test11() throws Exception {
        //given
      Post post= Post.builder()
                .title("나는 바보 입니다. ")
                .content("반포자이  ")
                .build();
        String json = objectMapper.writeValueAsString(post);
        //when
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andDo(print());



    }




}