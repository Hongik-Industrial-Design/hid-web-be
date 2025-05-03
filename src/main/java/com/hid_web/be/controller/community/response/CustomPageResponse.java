package com.hid_web.be.controller.community.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class CustomPageResponse<T> {
    private List<T> content;
    private PageInfo pageInfo;

    @Getter
    @AllArgsConstructor
    public static class PageInfo {
        private int currentPage;    // 현재 페이지 (1부터 시작)
        private int totalPages;     // 전체 페이지 수
        private long totalElements; // 전체 데이터 개수
        private boolean isFirst;    // 첫 번째 페이지 여부
        private boolean isLast;     // 마지막 페이지 여부
    }
}
