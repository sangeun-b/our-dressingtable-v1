package com.ourdressingtable.community.post.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreatePostRequest {

    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    @NotNull(message = "카테고리를 선택해주세요.")
    private Long communityCategoryId;

    private List<String> images;

    @Builder
    public CreatePostRequest(String title, String content, Long communityCategoryId, List<String> images) {
        this.title = title;
        this.content = content;
        this.communityCategoryId = communityCategoryId;
        this.images = images;

    }
}

