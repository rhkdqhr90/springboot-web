package com.springweb.springweb.repository;

import com.springweb.springweb.domain.Post;
import com.springweb.springweb.request.PostSearch;

import java.util.List;

public interface PostRepositoryCustom {
    List<Post> getList(PostSearch postSearch);
}
