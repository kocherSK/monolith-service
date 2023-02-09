package com.osttra.fx.blockstream.repository;

import com.osttra.fx.blockstream.domain.SmartTrade;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the SmartTrade entity.
 */
@Repository
public interface SmartTradeRepository extends MongoRepository<SmartTrade, String> {
    @Query("{}")
    Page<SmartTrade> findAllWithEagerRelationships(Pageable pageable);

    @Query("{}")
    List<SmartTrade> findAllWithEagerRelationships();

    @Query("{'id': ?0}")
    Optional<SmartTrade> findOneWithEagerRelationships(String id);
}
