package com.fresco.tenderManagement.service;

import com.fresco.tenderManagement.model.BiddingModel;
import com.fresco.tenderManagement.model.UserModel;
import com.fresco.tenderManagement.repository.BiddingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class BiddingService {

    @Autowired
    private BiddingRepository biddingRepository;

    @Autowired
    private UserService userService;

    // helper: gets the UserModel of whoever is currently logged in
    // SecurityContextHolder stores the authenticated user after JwtFilter runs
    private UserModel getCurrentUser() {
        // getPrincipal() returns the UserDetails object we built in LoginService
        UserDetails userDetails = (UserDetails) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        return userService.getUserByEmail(userDetails.getUsername()); // username = email
    }

    public ResponseEntity<Object> postBidding(BiddingModel biddingModel) {
        try {
            // auto-set fields that the client should NOT provide
            UserModel currentUser = getCurrentUser();
            biddingModel.setBidderId(currentUser.getId());  // who is creating this bid

            // today's date in dd/MM/yyyy format
            String today = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
            biddingModel.setDateOfBidding(today);

            // status defaults to "pending" in the model, but set it explicitly to be safe
            if (biddingModel.getStatus() == null || biddingModel.getStatus().isEmpty()) {
                biddingModel.setStatus("pending");
            }

            BiddingModel saved = biddingRepository.save(biddingModel);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved); // 201
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Bad request: " + e.getMessage()); // 400
        }
    }

    public ResponseEntity<Object> getBidding(double bidAmount) {
        List<BiddingModel> results = biddingRepository.findByBidAmountGreaterThan(bidAmount);
        if (results.isEmpty()) {
            return ResponseEntity.badRequest().body("no data available"); // 400 when empty
        }
        return ResponseEntity.ok(results); // 200 with list
    }

    public ResponseEntity<Object> updateBidding(int id, BiddingModel model) {
        Optional<BiddingModel> optional = biddingRepository.findById(id);
        if (!optional.isPresent()) {
            return ResponseEntity.badRequest().body("Bidding not found"); // 400 for bad id
        }
        BiddingModel existing = optional.get();
        existing.setStatus(model.getStatus()); // only update status
        biddingRepository.save(existing);
        return ResponseEntity.ok(existing); // 200 with updated object
    }

    public ResponseEntity<Object> deleteBidding(int id) {
        // find the bid
        Optional<BiddingModel> optional = biddingRepository.findById(id);
        if (!optional.isPresent()) {
            return ResponseEntity.badRequest().body("not found"); // 400
        }
        BiddingModel bidding = optional.get();

        // get who is calling this endpoint
        UserModel currentUser = getCurrentUser();
        String currentRole = currentUser.getRole().getRolename(); // "BIDDER" or "APPROVER"

        // APPROVER can delete anything
        // BIDDER can only delete their own bids
        if (currentRole.equals("APPROVER")) {
            biddingRepository.deleteById(id);
        } else if (currentRole.equals("BIDDER")) {
            if (bidding.getBidderId() != currentUser.getId()) {
                // this bidder did not create this bid
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("you don't have permission"); // 403
            }
            biddingRepository.deleteById(id);
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // 204
    }
}