package com.ourdressingtable.communitycategory.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCommunityCategory is a Querydsl query type for CommunityCategory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCommunityCategory extends EntityPathBase<CommunityCategory> {

    private static final long serialVersionUID = -1627163602L;

    public static final QCommunityCategory communityCategory = new QCommunityCategory("communityCategory");

    public final com.ourdressingtable.common.util.QBaseTimeEntity _super = new com.ourdressingtable.common.util.QBaseTimeEntity(this);

    public final StringPath code = createString("code");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath description = createString("description");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isActive = createBoolean("isActive");

    public final StringPath name = createString("name");

    public final NumberPath<Integer> sort_order = createNumber("sort_order", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QCommunityCategory(String variable) {
        super(CommunityCategory.class, forVariable(variable));
    }

    public QCommunityCategory(Path<? extends CommunityCategory> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCommunityCategory(PathMetadata metadata) {
        super(CommunityCategory.class, metadata);
    }

}

