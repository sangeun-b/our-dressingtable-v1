package com.ourdressingtable.chat.domain.repository;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;

import com.ourdressingtable.chat.dto.ChatroomIdResponse;
import com.ourdressingtable.chat.dto.OneToOneChatroomSummaryResponse;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ChatRepositoryImpl implements ChatRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    @Override
    public Optional<String> findOneToOneChatroom(String memberId, String targetId) {
        Aggregation aggregation = newAggregation(
                match(Criteria.where("isActive").is(true)
                        .and("memberId").in(memberId, targetId)),
                group("chatroomId").count().as("count"),
                match(Criteria.where("count").is(2)),
                project("_id").and("_id").as("chatroomId")
        );

        AggregationResults<ChatroomIdResponse> results = mongoTemplate.aggregate(aggregation, "chat", ChatroomIdResponse.class);

        return results.getMappedResults().stream()
                .map(ChatroomIdResponse::chatroomId)
                .findFirst();

    }
}
