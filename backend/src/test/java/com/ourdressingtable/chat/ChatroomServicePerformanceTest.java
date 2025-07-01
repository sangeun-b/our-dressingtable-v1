package com.ourdressingtable.chat;

import com.ourdressingtable.chat.dto.OneToOneChatroomSummaryResponse;
import com.ourdressingtable.chat.service.ChatroomService;
import com.ourdressingtable.common.interceptor.RateLimitInterceptor;
import com.ourdressingtable.common.security.WithCustomUser;
import java.util.List;
import net.ttddyy.dsproxy.QueryCount;
import net.ttddyy.dsproxy.QueryCountHolder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.StopWatch;

@SpringBootTest
@ActiveProfiles("test")
public class ChatroomServicePerformanceTest {

    @Autowired
    private ChatroomService chatroomService;

    @MockBean
    private RateLimitInterceptor rateLimitInterceptor;

    @BeforeEach
    void clearQueryCount() {
        QueryCountHolder.clear();
    }

    @WithCustomUser
    @Test
    void measureOneToOneChatroomListPerformanceWithStopWatch() {
        int iterations  = 10;
        StopWatch stopWatch = new StopWatch("1:1 채팅방 목록 조회 성능 측정");

        for (int i = 1; i<= iterations; i++) {
            stopWatch.start("Iteration "+i);

            List<OneToOneChatroomSummaryResponse> result = chatroomService.getMyOneToOneChatrooms();

            stopWatch.stop();

            System.out.printf("Iteration $d - 결과개수: %d개%n", i, result.size());

        }

        System.out.println("===================================");
        System.out.println(stopWatch.prettyPrint());
        System.out.printf("총 소요 시간: %.2f ms%n", stopWatch.getTotalTimeMillis() * 1.0);
        System.out.printf("평균 실행 시간: %.2f ms%n", stopWatch.getTotalTimeMillis() / (double) iterations);



    }
    @Test
    @WithCustomUser
    void checkQueryCount() {
        chatroomService.getMyOneToOneChatrooms();
        var count = QueryCountHolder.get("proxy-ds");

        System.out.println("총 쿼리 수: " + count.getTotal());
        System.out.println("SELECT 수: " + count.getSelect());
    }

}
