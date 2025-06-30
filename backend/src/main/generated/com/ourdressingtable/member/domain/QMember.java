package com.ourdressingtable.member.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = 1810157850L;

    public static final QMember member = new QMember("member1");

    public final com.ourdressingtable.common.util.QBaseTimeEntity _super = new com.ourdressingtable.common.util.QBaseTimeEntity(this);

    public final EnumPath<AuthType> authType = createEnum("authType", AuthType.class);

    public final DateTimePath<java.util.Date> birthDate = createDateTime("birthDate", java.util.Date.class);

    public final EnumPath<ColorType> colorType = createEnum("colorType", ColorType.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath imageUrl = createString("imageUrl");

    public final NumberPath<Integer> lockedCount = createNumber("lockedCount", Integer.class);

    public final StringPath name = createString("name");

    public final StringPath nickname = createString("nickname");

    public final StringPath password = createString("password");

    public final StringPath phoneNumber = createString("phoneNumber");

    public final ListPath<com.ourdressingtable.community.post.domain.PostLike, com.ourdressingtable.community.post.domain.QPostLike> postLikes = this.<com.ourdressingtable.community.post.domain.PostLike, com.ourdressingtable.community.post.domain.QPostLike>createList("postLikes", com.ourdressingtable.community.post.domain.PostLike.class, com.ourdressingtable.community.post.domain.QPostLike.class, PathInits.DIRECT2);

    public final ListPath<com.ourdressingtable.community.post.domain.Post, com.ourdressingtable.community.post.domain.QPost> posts = this.<com.ourdressingtable.community.post.domain.Post, com.ourdressingtable.community.post.domain.QPost>createList("posts", com.ourdressingtable.community.post.domain.Post.class, com.ourdressingtable.community.post.domain.QPost.class, PathInits.DIRECT2);

    public final EnumPath<Role> role = createEnum("role", Role.class);

    public final EnumPath<SkinType> skinType = createEnum("skinType", SkinType.class);

    public final EnumPath<Status> status = createEnum("status", Status.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QMember(String variable) {
        super(Member.class, forVariable(variable));
    }

    public QMember(Path<? extends Member> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMember(PathMetadata metadata) {
        super(Member.class, metadata);
    }

}

