package com.ourdressingtable.community.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdatePostRequest {

    private String title;
    private String content;
    private Long CommunityCategoryId;
    private List<String> images;
}
