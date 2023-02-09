package com.osttra.fx.blockstream.repository;

import com.osttra.fx.blockstream.domain.Currencies;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Currencies entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CurrenciesRepository extends MongoRepository<Currencies, String> {}
