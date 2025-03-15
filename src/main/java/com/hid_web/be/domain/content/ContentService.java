package com.hid_web.be.domain.content;

import com.hid_web.be.controller.content.response.ContentMainVideoResponse;
import com.hid_web.be.storage.content.ContentMainVideoEntity;
import com.hid_web.be.storage.content.ContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ContentService {

    private final ContentRepository contentRepository;

    public ContentMainVideoResponse findMainVideoByYear(Long year) {
        ContentMainVideoEntity contentMainVideoEntity = contentRepository.findByYear(year);

        return ContentMainVideoResponse.of(contentMainVideoEntity);
    }
}
