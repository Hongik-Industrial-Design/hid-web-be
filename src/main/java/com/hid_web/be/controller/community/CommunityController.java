package com.hid_web.be.controller.community;

import com.hid_web.be.controller.community.response.CommunityResponse;
import com.hid_web.be.domain.community.CommunityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/community")
@RequiredArgsConstructor
public class CommunityController {
    private final CommunityService communityService;

    @GetMapping
    public CommunityResponse getCommunityPage() {
        return communityService.getCommunityData();
    }
}
