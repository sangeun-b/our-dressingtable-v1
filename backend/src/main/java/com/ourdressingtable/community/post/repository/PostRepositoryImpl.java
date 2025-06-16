package com.ourdressingtable.community.post.repository;

import com.ourdressingtable.common.exception.ErrorCode;
import com.ourdressingtable.common.exception.OurDressingTableException;
import com.ourdressingtable.community.comment.domain.QComment;
import com.ourdressingtable.community.post.domain.Post;
import com.ourdressingtable.community.post.domain.QPost;
import com.ourdressingtable.community.post.domain.QPostLike;
import com.ourdressingtable.community.post.dto.PostSearchCondition;
import com.ourdressingtable.community.post.util.PostSortUtil;
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
                    builder.andAnyOf(
                            post.title.containsIgnoreCase(condition.getKeyword()),
                            post.content.containsIgnoreCase(condition.getKeyword())
                    );
                    break;
                default:
                    throw new OurDressingTableException(ErrorCode.BAD_REQUEST);
            }
        }

        if(StringUtils.hasText(condition.getCategory())){
            builder.and(post.communityCategory.name.eq(condition.getCategory()));
        }

        OrderSpecifier<?> order = PostSortUtil.getOrderSpecifier(condition.getSortBy(), post);

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

    @Override
    public Page<Post> findMyPosts(Long memberId, Pageable pageable, String sortBy) {
        QPost post = QPost.post;
        List<Post> content = jpaQueryFactory.selectFrom(post)
                .where(post.member.id.eq(memberId))
                .orderBy(PostSortUtil.getOrderSpecifier(sortBy, post))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = jpaQueryFactory
                .select(post.count())
                .from(post)
                .where(post.member.id.eq(memberId))
                .fetchOne();

        return new PageImpl<>(content, pageable, total == null ? 0 : total);
    }

    @Override
    public Page<Post> findLikedPosts(Long memberId, Pageable pageable, String sortBy) {
        QPost post = QPost.post;
        QPostLike postLike = QPostLike.postLike;

        List<Post> content = jpaQueryFactory.selectFrom(post)
                .join(postLike).on(postLike.post.eq(post))
                .where(post.member.id.eq(memberId))
                .orderBy(PostSortUtil.getOrderSpecifier(sortBy,post))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = jpaQueryFactory.select(post.count())
                .from(post)
                .join(postLike).on(postLike.post.eq(post))
                .where(postLike.member.id.eq(memberId))
                .fetchOne();

        return new PageImpl<>(content, pageable, total == null ? 0 : total);
    }

    @Override
    public Page<Post> findCommentedPosts(Long memberId, Pageable pageable, String sortBy) {
        QPost post = QPost.post;
        QComment comment = QComment.comment;

        List<Post> content = jpaQueryFactory.selectFrom(post)
                .join(comment).on(comment.post.eq(post))
                .where(post.member.id.eq(memberId))
                .orderBy(PostSortUtil.getOrderSpecifier(sortBy, post))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = jpaQueryFactory.select(post.count())
                .from(post)
                .join(comment).on(comment.post.eq(post))
                .where(comment.member.id.eq(memberId))
                .fetchOne();

        return new PageImpl<>(content, pageable, total == null ? 0 : total);
    }
}
