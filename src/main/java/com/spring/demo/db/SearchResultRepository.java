package com.spring.demo.db;

import com.spring.demo.entities.Movie;
import com.spring.demo.entities.MovieSearchResult;
import com.spring.demo.entities.SearchResult;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SearchResultRepository extends MongoRepository<SearchResult, String> {

    SearchResult findBySearchText(String text);
}
