package com.ourdressingtable.cosmetic.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCosmeticCategory is a Querydsl query type for CosmeticCategory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCosmeticCategory extends EntityPathBase<CosmeticCategory> {

    private static final long serialVersionUID = 2006528546L;

    public static final QCosmeticCategory cosmeticCategory = new QCosmeticCategory("cosmeticCategory");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final NumberPath<Long> parentId = createNumber("parentId", Long.class);

    public QCosmeticCategory(String variable) {
        super(CosmeticCategory.class, forVariable(variable));
    }

    public QCosmeticCategory(Path<? extends CosmeticCategory> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCosmeticCategory(PathMetadata metadata) {
        super(CosmeticCategory.class, metadata);
    }

}

