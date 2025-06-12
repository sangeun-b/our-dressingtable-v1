package com.ourdressingtable.community.post.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPost is a Querydsl query type for Post
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPost extends EntityPathBase<Post> {

    private static final long serialVersionUID = -809980533L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPost post = new QPost("post");

    public final com.ourdressingtable.common.util.QBaseTimeEntity _super = new com.ourdressingtable.common.util.QBaseTimeEntity(this);

    public final ListPath<com.ourdressingtable.community.comment.domain.Comment, com.ourdressingtable.community.comment.domain.QComment> comments = this.<com.ourdressingtable.community.comment.domain.Comment, com.ourdressingtable.community.comment.domain.QComment>createList("comments", com.ourdressingtable.community.comment.domain.Comment.class, com.ourdressingtable.community.comment.domain.QComment.class, PathInits.DIRECT2);

    public final com.ourdressingtable.communityCategory.domain.QCommunityCategory communityCategory;

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.sql.Timestamp> createdAt = _super.createdAt;

    public final DateTimePath<java.sql.Timestamp> deletedAt = createDateTime("deletedAt", java.sql.Timestamp.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    public final NumberPath<Integer> likeCount = createNumber("likeCount", Integer.class);

    public final com.ourdressingtable.member.domain.QMember member;

    public final ListPath<PostLike, QPostLike> postLikes = this.<PostLike, QPostLike>createList("postLikes", PostLike.class, QPostLike.class, PathInits.DIRECT2);

    public final StringPath title = createString("title");

    //inherited
    public final DateTimePath<java.sql.Timestamp> updatedAt = _super.updatedAt;

    public final NumberPath<Integer> viewCount = createNumber("viewCount", Integer.class);

    public QPost(String variable) {
        this(Post.class, forVariable(variable), INITS);
    }

    public QPost(Path<? extends Post> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPost(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPost(PathMetadata metadata, PathInits inits) {
        this(Post.class, metadata, inits);
    }

    public QPost(Class<? extends Post> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.communityCategory = inits.isInitialized("communityCategory") ? new com.ourdressingtable.communityCategory.domain.QCommunityCategory(forProperty("communityCategory")) : null;
        this.member = inits.isInitialized("member") ? new com.ourdressingtable.member.domain.QMember(forProperty("member")) : null;
    }

}

