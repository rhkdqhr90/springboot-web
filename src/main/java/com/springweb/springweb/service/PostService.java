package com.springweb.springweb.service;

import com.springweb.springweb.domain.Post;
import com.springweb.springweb.domain.PostEditor;
import com.springweb.springweb.exception.PostNotFound;
import com.springweb.springweb.repository.PostRepository;
import com.springweb.springweb.request.PostCreate;
import com.springweb.springweb.request.PostEdit;
import com.springweb.springweb.request.PostSearch;
import com.springweb.springweb.response.PostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

        private final PostRepository postRepository;
    public void write(PostCreate postCreate) {
        //Postcreate -> Entity
        Post post = Post.builder()
                .title(postCreate.getTitle())
                .content(postCreate.getContent())
                .build();
        postRepository.save(post);
    }

    public PostResponse get(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFound());

        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .build();



    }


    public List<PostResponse> getList(PostSearch postSearch) {
//        PageRequest pageable = PageRequest.of(page, 5, Sort.by(Sort.Direction.DESC,"id"));
        return postRepository.getList(postSearch).stream().map(post -> new PostResponse(post))
                .collect(Collectors.toList());
    }

    @Transactional
    public PostResponse edit(Long id, PostEdit postEdit) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFound());

        PostEditor.PostEditorBuilder postEditorBuilder = post.toEditor();
        PostEditor postEditor = postEditorBuilder.title(post.getTitle()).content(post.getContent()).build();

        post.edit(postEdit);

        return new PostResponse(post);


    }

    public void delete(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFound());

        postRepository.delete(post);

    }
}
