package com.energymarket.repository;

import com.energymarket.model.NFT;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.math.BigInteger;

public interface NFTRepository extends JpaRepository<NFT, BigInteger> {
    Page<NFT> findByIsListedTrue(Pageable pageable);
    Page<NFT> findByOwner(String owner, Pageable pageable);
} 