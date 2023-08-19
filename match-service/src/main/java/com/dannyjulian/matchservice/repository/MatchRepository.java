package com.dannyjulian.matchservice.repository;

import com.dannyjulian.matchservice.model.MatchItem;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface MatchRepository extends ListCrudRepository<MatchItem, Long> {
    List<MatchItem> findAllByGuid(String guid); // Using derived query method
}
