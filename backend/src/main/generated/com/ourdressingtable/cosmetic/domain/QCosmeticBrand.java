package com.ourdressingtable.cosmetic.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCosmeticBrand is a Querydsl query type for CosmeticBrand
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCosmeticBrand extends EntityPathBase<CosmeticBrand> {

    private static final long serialVersionUID = -1171748637L;

    public static final QCosmeticBrand cosmeticBrand = new QCosmeticBrand("cosmeticBrand");

    public final StringPath description = createString("description");

    public final StringPath homepageUrl = createString("homepageUrl");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath logoUrl = createString("logoUrl");

    public final StringPath name = createString("name");

    public QCosmeticBrand(String variable) {
        super(CosmeticBrand.class, forVariable(variable));
    }

    public QCosmeticBrand(Path<? extends CosmeticBrand> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCosmeticBrand(PathMetadata metadata) {
        super(CosmeticBrand.class, metadata);
    }

}

