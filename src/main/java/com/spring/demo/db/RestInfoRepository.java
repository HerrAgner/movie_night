package com.spring.demo.db;

import com.spring.demo.entities.RestInfo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RestInfoRepository extends MongoRepository<RestInfo, String> {
}
