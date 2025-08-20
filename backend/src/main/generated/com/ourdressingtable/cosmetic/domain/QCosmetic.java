package com.ourdressingtable.cosmetic.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCosmetic is a Querydsl query type for Cosmetic
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCosmetic extends EntityPathBase<Cosmetic> {

    private static final long serialVersionUID = -1513138940L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCosmetic cosmetic = new QCosmetic("cosmetic");

    public final StringPath allergens = createString("allergens");

    public final StringPath barcode = createString("barcode");

    public final com.ourdressingtable.cosmeticbrand.domain.QCosmeticBrand brand;

    public final StringPath certifications = createString("certifications");

    public final com.ourdressingtable.cosmeticcategory.domain.QCosmeticCategory cosmeticCategory;

    public final StringPath countryOfOrigin = createString("countryOfOrigin");

    public final StringPath description = createString("description");

    public final StringPath expirationType = createString("expirationType");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath imageUrl = createString("imageUrl");

    public final StringPath ingredients = createString("ingredients");

    public final StringPath manufacturer = createString("manufacturer");

    public final StringPath name = createString("name");

    public final StringPath officialUrl = createString("officialUrl");

    public final StringPath productType = createString("productType");

    public final StringPath releasedAt = createString("releasedAt");

    public final StringPath scent = createString("scent");

    public final StringPath skinType = createString("skinType");

    public final StringPath texture = createString("texture");

    public final StringPath volume = createString("volume");

    public QCosmetic(String variable) {
        this(Cosmetic.class, forVariable(variable), INITS);
    }

    public QCosmetic(Path<? extends Cosmetic> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCosmetic(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCosmetic(PathMetadata metadata, PathInits inits) {
        this(Cosmetic.class, metadata, inits);
    }

    public QCosmetic(Class<? extends Cosmetic> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.brand = inits.isInitialized("brand") ? new com.ourdressingtable.cosmeticbrand.domain.QCosmeticBrand(forProperty("brand")) : null;
        this.cosmeticCategory = inits.isInitialized("cosmeticCategory") ? new com.ourdressingtable.cosmeticcategory.domain.QCosmeticCategory(forProperty("cosmeticCategory"), inits.get("cosmeticCategory")) : null;
    }

}

