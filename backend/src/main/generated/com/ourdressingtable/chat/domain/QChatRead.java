package com.ourdressingtable.chat.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QChatRead is a Querydsl query type for ChatRead
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QChatRead extends EntityPathBase<ChatRead> {

    private static final long serialVersionUID = -1037315380L;

    public static final QChatRead chatRead = new QChatRead("chatRead");

    public final StringPath chatroomId = createString("chatroomId");

    public final StringPath id = createString("id");

    public final DateTimePath<java.time.LocalDateTime> lastReadAt = createDateTime("lastReadAt", java.time.LocalDateTime.class);

    public final StringPath memberId = createString("memberId");

    public QChatRead(String variable) {
        super(ChatRead.class, forVariable(variable));
    }

    public QChatRead(Path<? extends ChatRead> path) {
        super(path.getType(), path.getMetadata());
    }

    public QChatRead(PathMetadata metadata) {
        super(ChatRead.class, metadata);
    }

}

