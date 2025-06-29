package com.ourdressingtable.common.util;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCreatedAtEntity is a Querydsl query type for CreatedAtEntity
 */
@Generated("com.querydsl.codegen.DefaultSupertypeSerializer")
public class QCreatedAtEntity extends EntityPathBase<CreatedAtEntity> {

    private static final long serialVersionUID = -478607989L;

    public static final QCreatedAtEntity createdAtEntity = new QCreatedAtEntity("createdAtEntity");

    public final DateTimePath<java.sql.Timestamp> createdAt = createDateTime("createdAt", java.sql.Timestamp.class);

    public QCreatedAtEntity(String variable) {
        super(CreatedAtEntity.class, forVariable(variable));
    }

    public QCreatedAtEntity(Path<? extends CreatedAtEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCreatedAtEntity(PathMetadata metadata) {
        super(CreatedAtEntity.class, metadata);
    }

}

