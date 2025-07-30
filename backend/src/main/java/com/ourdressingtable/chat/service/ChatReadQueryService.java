package com.ourdressingtable.chat.service;

import java.time.LocalDateTime;

public interface ChatReadQueryService {
    LocalDateTime getLastReadAt(String chatroomId);


}
