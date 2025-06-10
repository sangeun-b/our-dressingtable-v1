package com.ourdressingtable.community.post.repository;

import com.ourdressingtable.community.post.domain.Post;
import com.ourdressingtable.community.post.dto.PostSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom {

    Page<Post> search(PostSearchCondition condition, Pageable pageable);

}
