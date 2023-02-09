package com.osttra.fx.blockstream.web.rest;

import com.osttra.fx.blockstream.domain.Currencies;
import com.osttra.fx.blockstream.repository.CurrenciesRepository;
import com.osttra.fx.blockstream.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.osttra.fx.blockstream.domain.Currencies}.
 */
@RestController
@RequestMapping("/api")
public class CurrenciesResource {

    private final Logger log = LoggerFactory.getLogger(CurrenciesResource.class);

    private static final String ENTITY_NAME = "currencies";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CurrenciesRepository currenciesRepository;

    public CurrenciesResource(CurrenciesRepository currenciesRepository) {
        this.currenciesRepository = currenciesRepository;
    }

    /**
     * {@code POST  /currencies} : Create a new currencies.
     *
     * @param currencies the currencies to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new currencies, or with status {@code 400 (Bad Request)} if the currencies has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/currencies")
    public ResponseEntity<Currencies> createCurrencies(@RequestBody Currencies currencies) throws URISyntaxException {
        log.debug("REST request to save Currencies : {}", currencies);
        if (currencies.getId() != null) {
            throw new BadRequestAlertException("A new currencies cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Currencies result = currenciesRepository.save(currencies);
        return ResponseEntity
            .created(new URI("/api/currencies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /currencies/:id} : Updates an existing currencies.
     *
     * @param id the id of the currencies to save.
     * @param currencies the currencies to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated currencies,
     * or with status {@code 400 (Bad Request)} if the currencies is not valid,
     * or with status {@code 500 (Internal Server Error)} if the currencies couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/currencies/{id}")
    public ResponseEntity<Currencies> updateCurrencies(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody Currencies currencies
    ) throws URISyntaxException {
        log.debug("REST request to update Currencies : {}, {}", id, currencies);
        if (currencies.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, currencies.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!currenciesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Currencies result = currenciesRepository.save(currencies);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, currencies.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /currencies/:id} : Partial updates given fields of an existing currencies, field will ignore if it is null
     *
     * @param id the id of the currencies to save.
     * @param currencies the currencies to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated currencies,
     * or with status {@code 400 (Bad Request)} if the currencies is not valid,
     * or with status {@code 404 (Not Found)} if the currencies is not found,
     * or with status {@code 500 (Internal Server Error)} if the currencies couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/currencies/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Currencies> partialUpdateCurrencies(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody Currencies currencies
    ) throws URISyntaxException {
        log.debug("REST request to partial update Currencies partially : {}, {}", id, currencies);
        if (currencies.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, currencies.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!currenciesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Currencies> result = currenciesRepository
            .findById(currencies.getId())
            .map(existingCurrencies -> {
                if (currencies.getCurrencyName() != null) {
                    existingCurrencies.setCurrencyName(currencies.getCurrencyName());
                }
                if (currencies.getCurrencyCode() != null) {
                    existingCurrencies.setCurrencyCode(currencies.getCurrencyCode());
                }

                return existingCurrencies;
            })
            .map(currenciesRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, currencies.getId())
        );
    }

    /**
     * {@code GET  /currencies} : get all the currencies.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of currencies in body.
     */
    @GetMapping("/currencies")
    public List<Currencies> getAllCurrencies() {
        log.debug("REST request to get all Currencies");
        return currenciesRepository.findAll();
    }

    /**
     * {@code GET  /currencies/:id} : get the "id" currencies.
     *
     * @param id the id of the currencies to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the currencies, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/currencies/{id}")
    public ResponseEntity<Currencies> getCurrencies(@PathVariable String id) {
        log.debug("REST request to get Currencies : {}", id);
        Optional<Currencies> currencies = currenciesRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(currencies);
    }

    /**
     * {@code DELETE  /currencies/:id} : delete the "id" currencies.
     *
     * @param id the id of the currencies to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/currencies/{id}")
    public ResponseEntity<Void> deleteCurrencies(@PathVariable String id) {
        log.debug("REST request to delete Currencies : {}", id);
        currenciesRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }
}
