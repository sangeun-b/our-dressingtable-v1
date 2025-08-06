package com.ourdressingtable.membercosmetic.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMemberCosmetic is a Querydsl query type for MemberCosmetic
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMemberCosmetic extends EntityPathBase<MemberCosmetic> {

    private static final long serialVersionUID = -623584328L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMemberCosmetic memberCosmetic = new QMemberCosmetic("memberCosmetic");

    public final com.ourdressingtable.cosmetic.domain.QCosmetic cosmetic;

    public final com.ourdressingtable.dressingtable.domain.QDressingTable dressingTable;

    public final DatePath<java.time.LocalDate> expiredDate = createDate("expiredDate", java.time.LocalDate.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath imageUrl = createString("imageUrl");

    public final com.ourdressingtable.member.domain.QMember member;

    public final DatePath<java.time.LocalDate> openDate = createDate("openDate", java.time.LocalDate.class);

    public final NumberPath<java.math.BigDecimal> price = createNumber("price", java.math.BigDecimal.class);

    public final BooleanPath setNotification = createBoolean("setNotification");

    public final StringPath store = createString("store");

    public final DatePath<java.time.LocalDate> useByDate = createDate("useByDate", java.time.LocalDate.class);

    public QMemberCosmetic(String variable) {
        this(MemberCosmetic.class, forVariable(variable), INITS);
    }

    public QMemberCosmetic(Path<? extends MemberCosmetic> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMemberCosmetic(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMemberCosmetic(PathMetadata metadata, PathInits inits) {
        this(MemberCosmetic.class, metadata, inits);
    }

    public QMemberCosmetic(Class<? extends MemberCosmetic> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.cosmetic = inits.isInitialized("cosmetic") ? new com.ourdressingtable.cosmetic.domain.QCosmetic(forProperty("cosmetic"), inits.get("cosmetic")) : null;
        this.dressingTable = inits.isInitialized("dressingTable") ? new com.ourdressingtable.dressingtable.domain.QDressingTable(forProperty("dressingTable"), inits.get("dressingTable")) : null;
        this.member = inits.isInitialized("member") ? new com.ourdressingtable.member.domain.QMember(forProperty("member")) : null;
    }

}

