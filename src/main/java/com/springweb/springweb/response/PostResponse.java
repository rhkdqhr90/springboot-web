package com.springweb.springweb.response;

import com.springweb.springweb.domain.Post;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Size;

/**
 * 서비스 정책에 맞는 클래스
 */
@Getter
public class PostResponse {
    private final Long id;
    @Size(min =0 , max = 10)
    private final String title;
    private final String content;
    //생성자 오버로딩
    public PostResponse(Post post){
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
    }
    @Builder
    public PostResponse(Long id, String title, String content) {
        this.id = id;
        this.title = title.substring(0, Math.min(title.length(),10));
        this.content = content;
    }
}
