package com.spring.demo.db;

import com.spring.demo.entities.SearchResult;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SearchResultRepository extends MongoRepository<SearchResult, String> {

    SearchResult findFirstBySearchTextAndPage(String text, int p);
}
