package com.example.rsupport.noticeboard.repository;

import com.example.rsupport.noticeboard.entity.Notice;
import com.example.rsupport.noticeboard.entity.QNotice;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class NoticeRepositoryCustomImpl implements NoticeRepositoryCustom {
    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    @Override
    public Notice getNoticeDetail(Long id) {
        return jpaQueryFactory.selectFrom(QNotice.notice)
                .where(QNotice.notice.noticeId.eq(id))
                .fetchOne();
    }

    @Override
    public Page<Notice> getNoticeSearch(String searchType, String keyword, Pageable pageable) {
        QNotice notice = QNotice.notice;
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if ("title".equals(searchType)) {
            booleanBuilder.and(notice.title.containsIgnoreCase(keyword));
        } else if ("content".equals(searchType)) {
            booleanBuilder.and(notice.content.containsIgnoreCase(keyword));
        } else if ("author".equals(searchType)) {
            booleanBuilder.and(notice.author.containsIgnoreCase(keyword));
        }

        JPQLQuery<Notice> query = jpaQueryFactory.selectFrom(notice)
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
        jpaQueryFactory.update(QNotice.notice)
                .where(QNotice.notice.noticeId.eq(notice.getNoticeId()))
                .set(QNotice.notice.title, notice.getTitle())
                .set(QNotice.notice.content, notice.getContent())
                .execute();
    }
}
