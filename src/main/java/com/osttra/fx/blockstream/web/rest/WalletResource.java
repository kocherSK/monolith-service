package com.osttra.fx.blockstream.web.rest;

import com.google.common.collect.Lists;
import com.osttra.fx.blockstream.domain.Customer;
import com.osttra.fx.blockstream.domain.User;
import com.osttra.fx.blockstream.domain.Wallet;
import com.osttra.fx.blockstream.repository.CustomerRepository;
import com.osttra.fx.blockstream.repository.WalletRepository;
import com.osttra.fx.blockstream.service.UserService;
import com.osttra.fx.blockstream.web.rest.errors.BadRequestAlertException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.osttra.fx.blockstream.domain.Wallet}.
 */
@RestController
@RequestMapping("/api")
public class WalletResource {

    private final Logger log = LoggerFactory.getLogger(WalletResource.class);

    private static final String ENTITY_NAME = "wallet";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WalletRepository walletRepository;

    private final UserService userService;

    private final CustomerResource customerResource;

    public WalletResource(WalletRepository walletRepository, UserService userService, CustomerResource customerResource) {
        this.walletRepository = walletRepository;
        this.userService = userService;
        this.customerResource = customerResource;
    }

    /**
     * {@code POST  /wallets} : Create a new wallet.
     *
     * @param wallet the wallet to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new wallet, or with status {@code 400 (Bad Request)} if the wallet has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/wallets")
    public ResponseEntity<List<Wallet>> createWallet(@RequestBody Wallet wallet) throws URISyntaxException {
        log.debug("REST request to save Wallet : {}", wallet);
        if (wallet.getId() != null) {
            throw new BadRequestAlertException("A new wallet cannot already have an ID", ENTITY_NAME, "idexists");
        }
        wallet.setCustomer(customerResource.getCurrentCustomer());
        Wallet result = walletRepository.save(wallet);

        return ResponseEntity
            .created(new URI("/api/wallets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
            .body(getAllWallets(false));
    }

    /**
     * {@code GET  /wallets} : get all the wallets.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of wallets in body.
     */
    @GetMapping("/wallets")
    public List<Wallet> getAllWallets(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Wallets");
        List<Wallet> wallets = Lists.newArrayList();
        if (eagerload) {
            wallets = walletRepository.findAllWithEagerRelationships();
        } else {
            wallets = walletRepository.findAll();
        }

        if (!wallets.isEmpty()) {
            String currentCustomerLegals = customerResource.getCurrentCustomer().getCustomerLegalEntity();
            wallets = getWallets(currentCustomerLegals, wallets);
        }
        return wallets;
    }

    /**
     * {@code GET  /wallets/:id} : get the "id" wallet.
     *
     * @param id the id of the wallet to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the wallet, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/wallets/{loginId}")
    public List<Wallet> getWallet(@PathVariable String loginId) {
        log.debug("REST request to get Wallet : {}", loginId);
        List<Wallet> wallets = walletRepository.findAll();
        if (!wallets.isEmpty()) {
            wallets = getWallets(loginId, wallets);
        }
        return wallets;
    }

    private List<Wallet> getWallets(String loginId, List<Wallet> wallets) {
        Map<String, Integer> conWallets = wallets
            .stream()
            .filter(w -> w.getCustomer().getCustomerLegalEntity().equals(loginId))
            .collect(
                Collectors.groupingBy(wallet -> wallet.getCurrencyCode(), Collectors.summingInt(wallet -> wallet.getAmount().intValue()))
            );

        wallets =
            conWallets
                .entrySet()
                .stream()
                .map(wle -> new Wallet().currencyCode(wle.getKey()).amount(new BigDecimal(wle.getValue())))
                .collect(Collectors.toList());
        return wallets;
    }

    /**
     * {@code DELETE  /wallets/:id} : delete the "id" wallet.
     *
     * @param id the id of the wallet to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/wallets/{id}")
    public ResponseEntity<Void> deleteWallet(@PathVariable String id) {
        log.debug("REST request to delete Wallet : {}", id);
        walletRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }
}
