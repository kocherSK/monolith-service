package com.osttra.fx.blockstream.web.rest;

import com.osttra.fx.blockstream.domain.SmartTrade;
import com.osttra.fx.blockstream.repository.SmartTradeRepository;
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
 * REST controller for managing {@link com.osttra.fx.blockstream.domain.SmartTrade}.
 */
@RestController
@RequestMapping("/api")
public class SmartTradeResource {

    private final Logger log = LoggerFactory.getLogger(SmartTradeResource.class);

    private static final String ENTITY_NAME = "smartTrade";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SmartTradeRepository smartTradeRepository;

    public SmartTradeResource(SmartTradeRepository smartTradeRepository) {
        this.smartTradeRepository = smartTradeRepository;
    }

    /**
     * {@code POST  /smart-trades} : Create a new smartTrade.
     *
     * @param smartTrade the smartTrade to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new smartTrade, or with status {@code 400 (Bad Request)} if the smartTrade has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/smart-trades")
    public ResponseEntity<SmartTrade> createSmartTrade(@RequestBody SmartTrade smartTrade) throws URISyntaxException {
        log.debug("REST request to save SmartTrade : {}", smartTrade);
        if (smartTrade.getId() != null) {
            throw new BadRequestAlertException("A new smartTrade cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SmartTrade result = smartTradeRepository.save(smartTrade);
        return ResponseEntity
            .created(new URI("/api/smart-trades/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /smart-trades/:id} : Updates an existing smartTrade.
     *
     * @param id the id of the smartTrade to save.
     * @param smartTrade the smartTrade to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated smartTrade,
     * or with status {@code 400 (Bad Request)} if the smartTrade is not valid,
     * or with status {@code 500 (Internal Server Error)} if the smartTrade couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/smart-trades/{id}")
    public ResponseEntity<SmartTrade> updateSmartTrade(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody SmartTrade smartTrade
    ) throws URISyntaxException {
        log.debug("REST request to update SmartTrade : {}, {}", id, smartTrade);
        if (smartTrade.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, smartTrade.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!smartTradeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SmartTrade result = smartTradeRepository.save(smartTrade);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, smartTrade.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /smart-trades/:id} : Partial updates given fields of an existing smartTrade, field will ignore if it is null
     *
     * @param id the id of the smartTrade to save.
     * @param smartTrade the smartTrade to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated smartTrade,
     * or with status {@code 400 (Bad Request)} if the smartTrade is not valid,
     * or with status {@code 404 (Not Found)} if the smartTrade is not found,
     * or with status {@code 500 (Internal Server Error)} if the smartTrade couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/smart-trades/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SmartTrade> partialUpdateSmartTrade(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody SmartTrade smartTrade
    ) throws URISyntaxException {
        log.debug("REST request to partial update SmartTrade partially : {}, {}", id, smartTrade);
        if (smartTrade.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, smartTrade.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!smartTradeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SmartTrade> result = smartTradeRepository
            .findById(smartTrade.getId())
            .map(existingSmartTrade -> {
                if (smartTrade.getCounterParty() != null) {
                    existingSmartTrade.setCounterParty(smartTrade.getCounterParty());
                }
                if (smartTrade.getTradingParty() != null) {
                    existingSmartTrade.setTradingParty(smartTrade.getTradingParty());
                }
                if (smartTrade.getCurrencyBuy() != null) {
                    existingSmartTrade.setCurrencyBuy(smartTrade.getCurrencyBuy());
                }
                if (smartTrade.getCurrencySell() != null) {
                    existingSmartTrade.setCurrencySell(smartTrade.getCurrencySell());
                }
                if (smartTrade.getRate() != null) {
                    existingSmartTrade.setRate(smartTrade.getRate());
                }
                if (smartTrade.getAmount() != null) {
                    existingSmartTrade.setAmount(smartTrade.getAmount());
                }
                if (smartTrade.getContraAmount() != null) {
                    existingSmartTrade.setContraAmount(smartTrade.getContraAmount());
                }
                if (smartTrade.getValueDate() != null) {
                    existingSmartTrade.setValueDate(smartTrade.getValueDate());
                }
                if (smartTrade.getTransactionId() != null) {
                    existingSmartTrade.setTransactionId(smartTrade.getTransactionId());
                }
                if (smartTrade.getDirection() != null) {
                    existingSmartTrade.setDirection(smartTrade.getDirection());
                }

                return existingSmartTrade;
            })
            .map(smartTradeRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, smartTrade.getId())
        );
    }

    /**
     * {@code GET  /smart-trades} : get all the smartTrades.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of smartTrades in body.
     */
    @GetMapping("/smart-trades")
    public List<SmartTrade> getAllSmartTrades(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all SmartTrades");
        if (eagerload) {
            return smartTradeRepository.findAllWithEagerRelationships();
        } else {
            return smartTradeRepository.findAll();
        }
    }

    /**
     * {@code GET  /smart-trades/:id} : get the "id" smartTrade.
     *
     * @param id the id of the smartTrade to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the smartTrade, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/smart-trades/{id}")
    public ResponseEntity<SmartTrade> getSmartTrade(@PathVariable String id) {
        log.debug("REST request to get SmartTrade : {}", id);
        Optional<SmartTrade> smartTrade = smartTradeRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(smartTrade);
    }

    /**
     * {@code DELETE  /smart-trades/:id} : delete the "id" smartTrade.
     *
     * @param id the id of the smartTrade to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/smart-trades/{id}")
    public ResponseEntity<Void> deleteSmartTrade(@PathVariable String id) {
        log.debug("REST request to delete SmartTrade : {}", id);
        smartTradeRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }
}
