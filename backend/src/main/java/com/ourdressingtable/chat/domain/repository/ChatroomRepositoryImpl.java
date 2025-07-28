package com.ourdressingtable.chat.domain.repository;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;

import com.ourdressingtable.chat.domain.Chat;
import com.ourdressingtable.chat.domain.Chatroom;
import com.ourdressingtable.chat.domain.ChatroomType;
import com.ourdressingtable.chat.domain.Message;

import com.ourdressingtable.chat.dto.ChatroomIdResponse;
import com.ourdressingtable.chat.dto.OneToOneChatroomSummaryResponse;

import com.ourdressingtable.member.domain.Member;
import com.ourdressingtable.member.repository.MemberRepository;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ChatroomRepositoryImpl implements ChatroomRepositoryCustom{

    private final MongoTemplate mongoTemplate;
    private final MemberRepository memberRepository;

    @Override
    public List<OneToOneChatroomSummaryResponse> findOneToOneChatroomsByMemberId(String memberId) {
        
        // 1:1 채팅방 ID
        Aggregation aggregation = newAggregation(
                match(Criteria.where("isActive").is(true)),
                group("chatroomId").count().as("count"),
                match(Criteria.where("count").is(2)),
                project("_id").and("_id").as("chatroomId")
        );

        List<String> oneToOneChatroomIds = mongoTemplate
                .aggregate(aggregation, "chats", ChatroomIdResponse.class)
                .getMappedResults()
                .stream()
                .map(ChatroomIdResponse::chatroomId)
                .toList();

        if(oneToOneChatroomIds.isEmpty()){
            return List.of();
        }
        
        // 내가 참여 중인 채팅방
        Query myActiveChatQuery = new Query(Criteria.where("memberId").is(memberId)
                .and("isActive").is(true)
                .and("chatroomId").in(oneToOneChatroomIds));

        List<Chat> myChats = mongoTemplate.find(myActiveChatQuery, Chat.class,"chats");
        List<String> myChatroomIds = myChats.stream()
                .map(Chat::getChatroomId)
                .toList();

        if(myChatroomIds.isEmpty()){
            return List.of();
        }
        
        // chatrooms 정보 조회 
        Query chatroomQuery = new  Query(Criteria.where("_id").in(myChatroomIds)
                .and("type").is(ChatroomType.ONE_TO_ONE));
        List<Chatroom> chatrooms = mongoTemplate.find(chatroomQuery, Chatroom.class,"chatrooms");
        
        // 상대방 정보 조회
        Query targetChatQuery = new Query(Criteria.where("chatroomId").in(myChatroomIds)
                .and("isActive").is(true)
                .and("memberId").ne(memberId));
        List<Chat> targetChats = mongoTemplate.find(targetChatQuery, Chat.class,"chats");

        Map<String, String> chatroomToTargetMemberMap = targetChats.stream()
                .collect(Collectors.toMap(Chat::getChatroomId, Chat::getMemberId));

        // 마지막 메시지 조회
        List<Message> lastMessages = chatrooms.stream().map(cr -> {
            Query messageQuery = new Query(Criteria.where("chatroomId").is(cr.getId()))
                    .with(Sort.by(Direction.DESC, "createdAt"))
                    .limit(1);
            return mongoTemplate.findOne(messageQuery, Message.class, "messages");

        }).filter(Objects::nonNull).toList();


        Map<String, Message> chatroomToLastMessageMap = lastMessages.stream()
                .collect(Collectors.toMap(Message::getChatroomId, Function.identity()));

        return chatrooms.stream().map(cr -> {
            String targetId = chatroomToTargetMemberMap.get(cr.getId());
            Message last = chatroomToLastMessageMap.get(cr.getId());
            Member member = memberRepository.findById(Long.valueOf(targetId)).orElse(null);
            return new OneToOneChatroomSummaryResponse(
                    cr.getId(),
                    member != null ? String.valueOf(member.getId()) : null,
                    member != null ? member.getNickname() : null,
                    member != null ? member.getImageUrl() : null,
                    last != null ? last.getContent() : null,
                    last != null ? last.getCreatedAt() : null
            );
        }).toList();
    }
}
