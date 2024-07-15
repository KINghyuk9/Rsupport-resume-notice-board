package com.example.rsupport.noticeboard.specification;

import com.example.rsupport.noticeboard.entity.Notice;
import org.springframework.data.jpa.domain.Specification;

public class NoticeSpecification {
    public static Specification<Notice> search(String searchType, String keyword) {
        switch (searchType.toLowerCase()){
            case "title":
                return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("title"), "%" + keyword + "%");
            case "content":
                return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("content"), "%" + keyword + "%");
            case "author":
                return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("author"), "%" + keyword + "%");
            default:
                return null;
        }
    }
}
