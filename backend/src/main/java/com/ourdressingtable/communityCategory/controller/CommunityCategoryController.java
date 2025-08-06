package com.ourdressingtable.communitycategory.controller;

import com.ourdressingtable.communitycategory.dto.CommunityCategoryResponse;
import com.ourdressingtable.communitycategory.service.CommunityCategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/community-categories")
public class CommunityCategoryController {

    private final CommunityCategoryService communityCategoryService;

    @GetMapping()
    public ResponseEntity<List<CommunityCategoryResponse>> getCommunityCategories() {
        List<CommunityCategoryResponse> communityCategories = communityCategoryService.getAllCategories();
        return ResponseEntity.ok(communityCategories);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CommunityCategoryResponse> getCommunityCategoryById(@PathVariable("categoryId") Long categoryId) {
        CommunityCategoryResponse communityCategory = communityCategoryService.getCategoryById(categoryId);
        return ResponseEntity.ok(communityCategory);
    }



}
