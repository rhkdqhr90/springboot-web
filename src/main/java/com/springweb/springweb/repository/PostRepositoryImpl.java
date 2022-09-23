package com.springweb.springweb.repository;


import com.querydsl.jpa.impl.JPAQueryFactory;
import com.springweb.springweb.domain.Post;
import com.springweb.springweb.domain.QPost;
import com.springweb.springweb.request.PostSearch;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom{
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Post> getList(PostSearch postSearch) {
       return jpaQueryFactory.selectFrom(QPost.post)
                .limit(postSearch.getSize())
                .offset(postSearch.geOffset())
               .orderBy(QPost.post.id.desc())
                .fetch();
    }
}
