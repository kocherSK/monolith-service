package com.osttra.fx.blockstream.web.rest;

import com.osttra.fx.blockstream.domain.Customer;
import com.osttra.fx.blockstream.domain.User;
import com.osttra.fx.blockstream.domain.Wallet;
import com.osttra.fx.blockstream.repository.CustomerRepository;
import com.osttra.fx.blockstream.repository.WalletRepository;
import com.osttra.fx.blockstream.service.UserService;
import com.osttra.fx.blockstream.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
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

    private final CustomerRepository customerRepository;

    public WalletResource(WalletRepository walletRepository, UserService userService, CustomerRepository customerRepository) {
        this.walletRepository = walletRepository;
        this.userService = userService;
        this.customerRepository = customerRepository;
    }

    /**
     * {@code POST  /wallets} : Create a new wallet.
     *
     * @param wallet the wallet to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new wallet, or with status {@code 400 (Bad Request)} if the wallet has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/wallets")
    public ResponseEntity<Wallet> createWallet(@RequestBody Wallet wallet) throws URISyntaxException {
        log.debug("REST request to save Wallet : {}", wallet);
        if (wallet.getId() != null) {
            throw new BadRequestAlertException("A new wallet cannot already have an ID", ENTITY_NAME, "idexists");
        }

        Optional<User> currentUser = userService.getUserWithAuthorities();
        String loggedInUser = currentUser.get().getLogin();
        List<Customer> customers = customerRepository
            .findAll()
            .stream()
            .filter(cust -> cust.getCustomerLegalEntity().equals(loggedInUser))
            .collect(Collectors.toList());
        if (!customers.isEmpty()) {
            wallet.setCustomer(customers.get(0));
        }
        Wallet result = walletRepository.save(wallet);
        return ResponseEntity
            .created(new URI("/api/wallets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
            .body(result);
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
        if (eagerload) {
            return walletRepository.findAllWithEagerRelationships();
        } else {
            Optional<User> currentUser = userService.getUserWithAuthorities();
            List<Wallet> wallets = walletRepository.findAll();
            if (!wallets.isEmpty()) {
                wallets =
                    wallets
                        .stream()
                        .filter(w -> w.getCustomer().getCustomerLegalEntity().equals(currentUser.get().getLogin()))
                        .collect(Collectors.toList());
            }
            return wallets;
        }
    }

    /**
     * {@code GET  /wallets/:id} : get the "id" wallet.
     *
     * @param id the id of the wallet to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the wallet, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/wallets/{id}")
    public ResponseEntity<Wallet> getWallet(@PathVariable String id) {
        log.debug("REST request to get Wallet : {}", id);
        Optional<Wallet> wallet = walletRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(wallet);
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
