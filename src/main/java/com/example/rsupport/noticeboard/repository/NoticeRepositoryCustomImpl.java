package com.example.rsupport.noticeboard.repository;

import com.example.rsupport.noticeboard.entity.Notice;
import com.example.rsupport.noticeboard.entity.QNotice;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class NoticeRepositoryCustomImpl implements NoticeRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    private static final QNotice qNotice = QNotice.notice;

    @Override
    public Notice getNoticeDetail(Long id) {
        return jpaQueryFactory.selectFrom(qNotice)
                .where(qNotice.noticeId.eq(id))
                .fetchOne();
    }

    @Override
    public Page<Notice> getNoticeSearch(String searchType, String keyword, Pageable pageable) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if ("title".equals(searchType)) {
            booleanBuilder.and(qNotice.title.containsIgnoreCase(keyword));
        } else if ("content".equals(searchType)) {
            booleanBuilder.and(qNotice.content.containsIgnoreCase(keyword));
        } else if ("author".equals(searchType)) {
            booleanBuilder.and(qNotice.author.containsIgnoreCase(keyword));
        }

        JPQLQuery<Notice> query = jpaQueryFactory.selectFrom(qNotice)
                .where(booleanBuilder);

        List<Notice> notices = query.offset((long) pageable.getPageNumber() * pageable.getPageSize())
                .limit(pageable.getPageSize())
                .fetch();

        long total = query.fetchCount();

        return new PageImpl<>(notices, pageable, total);
    }

    @Override
    @Transactional
    public void updateNotice(Notice notice) {
        jpaQueryFactory.update(qNotice)
                .where(qNotice.noticeId.eq(notice.getNoticeId()))
                .set(qNotice.title, notice.getTitle())
                .set(qNotice.content, notice.getContent())
                .execute();
    }
}
