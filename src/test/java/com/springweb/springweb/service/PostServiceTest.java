package com.springweb.springweb.service;

import com.springweb.springweb.domain.Post;
import com.springweb.springweb.exception.PostNotFound;
import com.springweb.springweb.repository.PostRepository;
import com.springweb.springweb.request.PostCreate;
import com.springweb.springweb.request.PostEdit;
import com.springweb.springweb.request.PostSearch;
import com.springweb.springweb.response.PostResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class PostServiceTest {
    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void before() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("글 작성 ")
    void test1() {
        //given
        PostCreate postCreate = PostCreate.builder()
                .title("제목입니다")
                .content("내용입니다")
                .build();

        //when
        postService.write(postCreate);
        //then
        Assertions.assertEquals(1L, postRepository.count());
        Post post = postRepository.findAll().get(0);
        assertEquals("제목입니다",post.getTitle());
        assertEquals("내용입니다",post.getContent());
    }

    @Test
    @DisplayName("글 한개 조회")
    void test2() {
        //given
        Post requestPost = Post.builder()
                .title("제목입니다")
                .content("내용입니다")
                .build();

        //when
        postRepository.save(requestPost);

        //when
        PostResponse response = postService.get(requestPost.getId());
        //when
        Assertions.assertNotNull(response);

        assertEquals("제목입니다",response.getTitle());
        assertEquals("내용입니다",response.getContent());

    }

    @Test
    @DisplayName("글  조회")
    void test3() {
        //given
        List<Post> requestPosts = IntStream.range(0,20)
                .mapToObj(i ->
                    Post.builder()
                            .title("호돌맨 제목 " +i)
                            .content("반포자이  " +i)
                            .build()
                ).collect(Collectors.toList());
        postRepository.saveAll(requestPosts);

        //when
        PostSearch postSearch = PostSearch.builder()
                .page(1)
                .size(10)
                .build();




        List<PostResponse> posts = postService.getList(postSearch);
        //when


        assertEquals(10L,posts.size());
        assertEquals("호돌맨 제목 19",posts.get(0).getTitle());


    }

    @Test
    @DisplayName("글 제목 수정")
    void test4() {
        //given

                        Post post =Post.builder()
                                .title("호돌맨 제목")
                                .content("반포자이 ")
                                .build();

        postRepository.save(post);
        PostEdit postEdit = PostEdit.builder().title("호돌걸").content("반포자이").build();
        //when
        postService.edit(post.getId(),postEdit);
        //when
       Post changePost = postRepository.findById(post.getId())
                        .orElseThrow(() -> new RuntimeException("글이 존재 하지 않습니다 id=" + post.getId()));

        assertEquals("호돌걸",changePost.getTitle());
        assertEquals("반포자이",changePost.getContent());



    }

//    @Test
//    @DisplayName("글 내용 수정")
//    void test5() {
//        //given
//
//        Post post =Post.builder()
//                .title("호돌맨 제목")
//                .content("반포자이 ")
//                .build();
//
//        postRepository.save(post);
//        PostEdit postEdit = PostEdit.builder().title("호돌걸").build();
//        //when
//        postService.edit(post.getId(),postEdit);
//        //when
//        Post changePost = postRepository.findById(post.getId())
//                .orElseThrow(() -> new RuntimeException("글이 존재 하지 않습니다 id=" + post.getId()));
//
//
//        assertEquals("",changePost.getContent());



//    }
    @Test
    @DisplayName("게시글 삭제")
    void test6() {
        //given

        Post post = Post.builder()
                .title("호돌맨 제목")
                .content("반포자이 ")
                .build();

        postRepository.save(post);
        //when
        postService.delete(post.getId());
        //then
        Assertions.assertEquals(0,postRepository.count());

    }

    @Test
    @DisplayName("글 한개 실패 조회")
    void test7() {
        //given
        Post post = Post.builder()
                .title("호돌")
                .content("반포자")
                .build();

        //when
        postRepository.save(post);

        //when

        //when
            assertThrows(PostNotFound.class, () ->{
            postService.get(post.getId()+1L);
        });

    }
    @Test
    @DisplayName("게시글 삭제")
    void test8() {
        //given

        Post post = Post.builder()
                .title("호돌맨 제목")
                .content("반포자이 ")
                .build();

        postRepository.save(post);
        //when

        //then
        assertThrows(PostNotFound.class,()->{
            postService.delete(post.getId() +1);
        });

    }

    @Test
    @DisplayName("글 내용 수정 -존재하지 않는 글")
    void test9() {
        //given

        Post post = Post.builder()
                .title("호돌맨 제목")
                .content("반포자이 ")
                .build();

        postRepository.save(post);
        PostEdit postEdit = PostEdit.builder().title("호돌걸").build();


        assertThrows(PostNotFound.class, () -> {
            postService.edit(post.getId()+1L, postEdit);
        });

    }



}