package com.ourdressingtable.cosmeticcategory.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCosmeticCategory is a Querydsl query type for CosmeticCategory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCosmeticCategory extends EntityPathBase<CosmeticCategory> {

    private static final long serialVersionUID = 134884384L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCosmeticCategory cosmeticCategory = new QCosmeticCategory("cosmeticCategory");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final QCosmeticCategory parent;

    public QCosmeticCategory(String variable) {
        this(CosmeticCategory.class, forVariable(variable), INITS);
    }

    public QCosmeticCategory(Path<? extends CosmeticCategory> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCosmeticCategory(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCosmeticCategory(PathMetadata metadata, PathInits inits) {
        this(CosmeticCategory.class, metadata, inits);
    }

    public QCosmeticCategory(Class<? extends CosmeticCategory> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.parent = inits.isInitialized("parent") ? new QCosmeticCategory(forProperty("parent"), inits.get("parent")) : null;
    }

}

