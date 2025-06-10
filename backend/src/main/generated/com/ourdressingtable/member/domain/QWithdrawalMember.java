package com.ourdressingtable.member.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QWithdrawalMember is a Querydsl query type for WithdrawalMember
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QWithdrawalMember extends EntityPathBase<WithdrawalMember> {

    private static final long serialVersionUID = 1507655215L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QWithdrawalMember withdrawalMember = new QWithdrawalMember("withdrawalMember");

    public final StringPath hashedEmail = createString("hashedEmail");

    public final StringPath hashedPhone = createString("hashedPhone");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isBlock = createBoolean("isBlock");

    public final StringPath maskedEmail = createString("maskedEmail");

    public final StringPath maskedPhone = createString("maskedPhone");

    public final QMember member;

    public final StringPath reason = createString("reason");

    public final DateTimePath<java.sql.Timestamp> withdrewAt = createDateTime("withdrewAt", java.sql.Timestamp.class);

    public QWithdrawalMember(String variable) {
        this(WithdrawalMember.class, forVariable(variable), INITS);
    }

    public QWithdrawalMember(Path<? extends WithdrawalMember> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QWithdrawalMember(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QWithdrawalMember(PathMetadata metadata, PathInits inits) {
        this(WithdrawalMember.class, metadata, inits);
    }

    public QWithdrawalMember(Class<? extends WithdrawalMember> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member")) : null;
    }

}

