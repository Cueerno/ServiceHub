package com.radiuk.innotter_service.scheduling;

import com.radiuk.innotter_service.entity.PageEntity;
import com.radiuk.innotter_service.repository.PageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class PageUnblockScheduler {

    private final PageRepository pageRepository;

    @Scheduled(cron = "0 0 0 * * *")
    public void unblockPages() {
        List<PageEntity> pagesToUnblock = pageRepository.findAllByBlockedTrueAndUnblockDateBefore(Instant.now());

        for (PageEntity page : pagesToUnblock) {
            page.setBlocked(false);
            page.setUnblockDate(null);
            pageRepository.save(page);
            log.info("Page automatically unblocked: pageId={}", page.getId());
        }
    }
}