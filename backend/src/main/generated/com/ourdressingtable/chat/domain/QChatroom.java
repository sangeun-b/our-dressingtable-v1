package com.ourdressingtable.chat.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QChatroom is a Querydsl query type for Chatroom
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QChatroom extends EntityPathBase<Chatroom> {

    private static final long serialVersionUID = -1036352015L;

    public static final QChatroom chatroom = new QChatroom("chatroom");

    public final com.ourdressingtable.common.util.QCreatedAtEntity _super = new com.ourdressingtable.common.util.QCreatedAtEntity(this);

    public final ListPath<Chat, QChat> chats = this.<Chat, QChat>createList("chats", Chat.class, QChat.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final DateTimePath<java.time.LocalDateTime> deletedAt = createDateTime("deletedAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    public final ListPath<Message, QMessage> messages = this.<Message, QMessage>createList("messages", Message.class, QMessage.class, PathInits.DIRECT2);

    public final StringPath name = createString("name");

    public final EnumPath<ChatroomType> type = createEnum("type", ChatroomType.class);

    public QChatroom(String variable) {
        super(Chatroom.class, forVariable(variable));
    }

    public QChatroom(Path<? extends Chatroom> path) {
        super(path.getType(), path.getMetadata());
    }

    public QChatroom(PathMetadata metadata) {
        super(Chatroom.class, metadata);
    }

}

