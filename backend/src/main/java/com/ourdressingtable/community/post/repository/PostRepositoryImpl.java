package com.ourdressingtable.community.post.repository;

import com.ourdressingtable.community.post.domain.Post;
import com.ourdressingtable.community.post.domain.QPost;
import com.ourdressingtable.community.post.dto.PostSearchCondition;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Post> search(PostSearchCondition condition, Pageable pageable) {
        QPost post = QPost.post;

        BooleanBuilder builder = new BooleanBuilder();

        if(StringUtils.hasText(condition.getKeyword())){
            switch (condition.getSearchType()) {
                case "title" :
                    builder.and(post.title.containsIgnoreCase(condition.getKeyword()));
                    break;
                case "content" :
                    builder.and(post.content.containsIgnoreCase(condition.getKeyword()));
                    break;
                case "author" :
                    builder.and(post.member.nickname.containsIgnoreCase(condition.getKeyword()));
                    break;
                case "title_content":
                default:
                    builder.and(post.title.containsIgnoreCase(condition.getKeyword()))
                            .or(post.content.containsIgnoreCase(condition.getKeyword()));
                    break;
            }
        }

        if(StringUtils.hasText(condition.getCategory())){
            builder.and(post.communityCategory.name.eq(condition.getCategory()));
        }

        OrderSpecifier<?> order = getOrderSpecifier(condition.getSortBy(), post);

        List<Post> content = jpaQueryFactory
                .selectFrom(post)
                .where(builder)
                .orderBy(order)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = jpaQueryFactory
                .select(post.count())
                .from(post)
                .where(builder)
                .fetchOne();

        return new PageImpl<>(content, pageable, total == null ? 0 : total);

    }

    private OrderSpecifier<?> getOrderSpecifier(String sortBy, QPost post) {
        if("likes".equalsIgnoreCase(sortBy)){
            return post.likeCount.desc();
        } else if("views".equalsIgnoreCase(sortBy)){
            return post.viewCount.desc();
        } else {
            return post.createdAt.desc();
        }
    }
}
