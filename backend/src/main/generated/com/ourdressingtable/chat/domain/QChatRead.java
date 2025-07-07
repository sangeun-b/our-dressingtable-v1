package com.ourdressingtable.chat.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QChatRead is a Querydsl query type for ChatRead
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QChatRead extends EntityPathBase<ChatRead> {

    private static final long serialVersionUID = -1037315380L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QChatRead chatRead = new QChatRead("chatRead");

    public final QChatroom chatroom;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.time.LocalDateTime> lastReadAt = createDateTime("lastReadAt", java.time.LocalDateTime.class);

    public final com.ourdressingtable.member.domain.QMember member;

    public QChatRead(String variable) {
        this(ChatRead.class, forVariable(variable), INITS);
    }

    public QChatRead(Path<? extends ChatRead> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QChatRead(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QChatRead(PathMetadata metadata, PathInits inits) {
        this(ChatRead.class, metadata, inits);
    }

    public QChatRead(Class<? extends ChatRead> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.chatroom = inits.isInitialized("chatroom") ? new QChatroom(forProperty("chatroom")) : null;
        this.member = inits.isInitialized("member") ? new com.ourdressingtable.member.domain.QMember(forProperty("member")) : null;
    }

}

