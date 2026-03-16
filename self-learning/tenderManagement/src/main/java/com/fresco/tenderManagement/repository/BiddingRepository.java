package com.fresco.tenderManagement.repository;

import com.fresco.tenderManagement.model.BiddingModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BiddingRepository extends JpaRepository<BiddingModel, Integer> {
    // finds all bids where bidAmount is strictly greater than the given value
    // Spring generates: SELECT * FROM bidding_model WHERE bid_amount > ?
    List<BiddingModel> findByBidAmountGreaterThan(double bidAmount);
}