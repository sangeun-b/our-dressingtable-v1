package com.ourdressingtable.community.post.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdatePostRequest {

    private String title;
    private String content;
    private Long communityCategoryId;
    private List<String> images;

    @Builder
    public UpdatePostRequest(String title, String content, Long communityCategoryId, List<String> images) {
        this.title = title;
        this.content = content;
        this.communityCategoryId = communityCategoryId;
        this.images = images;
    }
}
