package org.meghana.OnboardingAndAssetsMgmt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.meghana.OnboardingAndAssetsMgmt.model.Candidate;
import org.meghana.OnboardingAndAssetsMgmt.payload.candidate.CreateCandidateDTO;
import org.meghana.OnboardingAndAssetsMgmt.service.CandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/people/candidates")
public class CandidateController {

    @Autowired
    private CandidateService candidateService;

    @PostMapping(value = "/create", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponse(responseCode = "400", description = "Please enter valid candidature details.")
    @ApiResponse(responseCode = "200", description = "Candidature created successfully.")
    @Operation(summary = "Create a new candidature")
    public ResponseEntity<String> createCandidate(@Valid @RequestBody CreateCandidateDTO createCandidateDTO, Authentication authentication) {
        try {
            candidateService.createCandidate(createCandidateDTO, authentication);
            return ResponseEntity.ok("Candidature created successful.");
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping(value = "/", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponse(responseCode = "200", description = "Successfully retrieved all candidates.")
    @Operation(summary = "Get all candidates")
    public ResponseEntity<List<Candidate>> getAllCandidates() {
        List<Candidate> candidates = candidateService.getAllCandidates();
        return ResponseEntity.ok(candidates);
    }

    @GetMapping(value = "/{userId}", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponse(responseCode = "200", description = "Successfully retrieved candidate by User ID.")
    @ApiResponse(responseCode = "404", description = "Candidate not found with the given User ID.")
    @Operation(summary = "Get candidate by User ID")
    public ResponseEntity<Candidate> getCandidateByUserId(@PathVariable String userId) {
        return candidateService.getCandidateByUserId(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping(value = "/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponse(responseCode = "200", description = "Successfully deleted candidate by User ID.")
    @ApiResponse(responseCode = "404", description = "Candidate not found with the given User ID.")
    @Operation(summary = "Delete candidate by User ID")
    public String deleteCandidateByUserId(@PathVariable String userId) {
        return candidateService.deleteCandidateByUserId(userId);
    }
}