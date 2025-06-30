package com.ourdressingtable.dressingTable.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDressingTable is a Querydsl query type for DressingTable
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDressingTable extends EntityPathBase<DressingTable> {

    private static final long serialVersionUID = -986332658L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDressingTable dressingTable = new QDressingTable("dressingTable");

    public final com.ourdressingtable.common.util.QBaseTimeEntity _super = new com.ourdressingtable.common.util.QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final DateTimePath<java.time.LocalDateTime> deletedAt = createDateTime("deletedAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath imageUrl = createString("imageUrl");

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    public final com.ourdressingtable.member.domain.QMember member;

    public final ListPath<com.ourdressingtable.memberCosmetic.domain.MemberCosmetic, com.ourdressingtable.memberCosmetic.domain.QMemberCosmetic> memberCosmetics = this.<com.ourdressingtable.memberCosmetic.domain.MemberCosmetic, com.ourdressingtable.memberCosmetic.domain.QMemberCosmetic>createList("memberCosmetics", com.ourdressingtable.memberCosmetic.domain.MemberCosmetic.class, com.ourdressingtable.memberCosmetic.domain.QMemberCosmetic.class, PathInits.DIRECT2);

    public final StringPath name = createString("name");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QDressingTable(String variable) {
        this(DressingTable.class, forVariable(variable), INITS);
    }

    public QDressingTable(Path<? extends DressingTable> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QDressingTable(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QDressingTable(PathMetadata metadata, PathInits inits) {
        this(DressingTable.class, metadata, inits);
    }

    public QDressingTable(Class<? extends DressingTable> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.ourdressingtable.member.domain.QMember(forProperty("member")) : null;
    }

}

