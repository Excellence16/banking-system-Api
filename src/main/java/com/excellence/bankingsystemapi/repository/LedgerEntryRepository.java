package com.excellence.bankingsystemapi.repository;

import com.excellence.bankingsystemapi.model.LedgerEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LedgerEntryRepository extends JpaRepository<LedgerEntry, Long> {
    Optional<LedgerEntry> findTopByAccountIdOrderByCreatedAtDesc(Long accountId);
}
