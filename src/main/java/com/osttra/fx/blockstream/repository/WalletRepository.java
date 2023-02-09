package com.osttra.fx.blockstream.repository;

import com.osttra.fx.blockstream.domain.Wallet;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Wallet entity.
 */
@Repository
public interface WalletRepository extends MongoRepository<Wallet, String> {
    @Query("{}")
    Page<Wallet> findAllWithEagerRelationships(Pageable pageable);

    @Query("{}")
    List<Wallet> findAllWithEagerRelationships();

    @Query("{'id': ?0}")
    Optional<Wallet> findOneWithEagerRelationships(String id);
}
