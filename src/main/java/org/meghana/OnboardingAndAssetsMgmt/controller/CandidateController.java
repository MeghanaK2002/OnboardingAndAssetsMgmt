package org.meghana.OnboardingAndAssetsMgmt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.meghana.OnboardingAndAssetsMgmt.model.Candidate;
import org.meghana.OnboardingAndAssetsMgmt.payload.candidate.BulkUpdateCandidateDTO;
import org.meghana.OnboardingAndAssetsMgmt.payload.candidate.CreateCandidateDTO;
import org.meghana.OnboardingAndAssetsMgmt.payload.candidate.UpdateCandidateDTO;
import org.meghana.OnboardingAndAssetsMgmt.payload.response.ApiResponseDTO;
import org.meghana.OnboardingAndAssetsMgmt.service.CandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/people/candidates")
public class CandidateController {

    private static final Logger logger = LoggerFactory.getLogger(CandidateController.class);

    @Autowired
    private CandidateService candidateService;

    @PostMapping(value = "/create", produces = "application/json")
    @ApiResponse(responseCode = "400", description = "Please enter valid candidature details.")
    @ApiResponse(responseCode = "201", description = "Candidature created successfully.")
    @Operation(summary = "Create a new candidature")
    public ResponseEntity<ApiResponseDTO<Candidate>> createCandidate(@Valid @RequestBody CreateCandidateDTO createCandidateDTO, Authentication authentication) {
        try {
            Candidate createdCandidate = candidateService.createCandidate(createCandidateDTO, authentication);
            ApiResponseDTO<Candidate> response = new ApiResponseDTO<>("success", "Candidature created successfully with User ID: " + createdCandidate.getUserId(), createdCandidate, LocalDateTime.now());
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }
        catch (IllegalArgumentException e) {
            ApiResponseDTO<Candidate> response = new ApiResponseDTO<>("failure", e.getMessage(), null, LocalDateTime.now());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            logger.error("Failed to create candidature due to : " + e.getMessage());
            ApiResponseDTO<Candidate> response = new ApiResponseDTO<>("failure", "Failed to create candidature due to : " + e.getMessage(), null, LocalDateTime.now());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/", produces = "application/json")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved all candidates.")
    @Operation(summary = "Get all candidates")
    public ResponseEntity<ApiResponseDTO<List<Candidate>>> getAllCandidates() {
        try {
            List<Candidate> candidates = candidateService.getAllCandidates();
            ApiResponseDTO<List<Candidate>> response = new ApiResponseDTO<>("success", "Successfully retrieved all candidates.", candidates, LocalDateTime.now());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Failed to retrieve all candidates due to : " + e.getMessage());
            ApiResponseDTO<List<Candidate>> response = new ApiResponseDTO<>("failure", "Failed to retrieve all candidates due to : " + e.getMessage(), null, LocalDateTime.now());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/{userId}", produces = "application/json")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved candidate by User ID.")
    @ApiResponse(responseCode = "404", description = "Candidate not found with the given User ID.")
    @Operation(summary = "Get candidate by User ID")
    public ResponseEntity<ApiResponseDTO<Candidate>> getCandidateByUserId(@PathVariable String userId) {
        try {
            return candidateService.getCandidateByUserId(userId)
                    .map(candidate -> {
                        ApiResponseDTO<Candidate> response = new ApiResponseDTO<>("success", "Successfully retrieved candidate.", candidate, LocalDateTime.now());
                        return ResponseEntity.ok(response);
                    })
                    .orElseGet(() -> {
                        ApiResponseDTO<Candidate> response = new ApiResponseDTO<>("failure", "Candidate not found with the given User ID.", null, LocalDateTime.now());
                        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
                    });
        }
        catch (IllegalArgumentException e) {
            ApiResponseDTO<Candidate> response = new ApiResponseDTO<>("failure", e.getMessage(), null, LocalDateTime.now());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            logger.error("Failed to retrieve candidature due to : " + e.getMessage());
            ApiResponseDTO<Candidate> response = new ApiResponseDTO<>("failure", "Failed to retrieve candidature due to : " + e.getMessage(), null, LocalDateTime.now());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value = "/{userId}", produces = "application/json")
    @ApiResponse(responseCode = "200", description = "Successfully deleted candidate by User ID.")
    @ApiResponse(responseCode = "404", description = "Candidate not found with the given User ID.")
    @Operation(summary = "Delete candidate by User ID")
    public ResponseEntity<ApiResponseDTO<Void>> deleteCandidateByUserId(@PathVariable String userId) {
        try {
            boolean deleted = candidateService.deleteCandidateByUserId(userId);
            if (deleted) {
                ApiResponseDTO<Void> response = new ApiResponseDTO<>("success", "Candidate with user id = " + userId + " deleted successfully.", null, LocalDateTime.now());
                return ResponseEntity.ok(response);
            } else {
                ApiResponseDTO<Void> response = new ApiResponseDTO<>("failure", "Candidate not found with User ID: " + userId, null, LocalDateTime.now());
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        }
        catch (IllegalArgumentException e) {
            ApiResponseDTO<Void> response = new ApiResponseDTO<>("failure", e.getMessage(), null, LocalDateTime.now());
            return new ResponseEntity<ApiResponseDTO<Void>>(response, HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            logger.error("Failed to delete candidature due to : " + e.getMessage());
            ApiResponseDTO<Void> response = new ApiResponseDTO<>("failure", "Failed to delete candidature due to : " + e.getMessage(), null, LocalDateTime.now());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = "/{userId}", produces = "application/json")
    @ApiResponse(responseCode = "200", description = "Candidate updated successfully.")
    @ApiResponse(responseCode = "404", description = "Candidate not found with the given User ID.")
    @ApiResponse(responseCode = "400", description = "Invalid update data provided.")
    @Operation(summary = "Update an existing candidate by User ID")
    public ResponseEntity<ApiResponseDTO<Candidate>> updateCandidate(@PathVariable String userId, @Valid @RequestBody UpdateCandidateDTO updateCandidateDTO, Authentication authentication) {
        try {
            Candidate updatedCandidate = candidateService.updateCandidate(userId, updateCandidateDTO, authentication);
            ApiResponseDTO<Candidate> response = new ApiResponseDTO<>("success", "Candidate with User Id : " + userId + " updated successfully.", updatedCandidate, LocalDateTime.now());
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            ApiResponseDTO<Candidate> response = new ApiResponseDTO<>("failure", e.getMessage(), null, LocalDateTime.now());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Failed to update candidature due to : " + e.getMessage());
            ApiResponseDTO<Candidate> response = new ApiResponseDTO<>("failure", "Failed to update candidate: " + e.getMessage(), null, LocalDateTime.now());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = "/update/multi", produces = "application/json")
    @ApiResponse(responseCode = "200", description = "Successfully updated the specified attributes for multiple candidates.")
    @ApiResponse(responseCode = "400", description = "Invalid bulk update data provided.")
    @ApiResponse(responseCode = "404", description = "One or more User IDs not found.")
    @Operation(summary = "Update multiple attributes for multiple candidates")
    public ResponseEntity<ApiResponseDTO<Map<String, Object>>> bulkUpdateCandidates(@Valid @RequestBody BulkUpdateCandidateDTO bulkUpdateDTO, Authentication authentication) {
        try {
            Map<String, Object> result = candidateService.bulkUpdateCandidates(bulkUpdateDTO, authentication);
            String status = (boolean) result.get("success") ? "success" : "partial_success";
            @SuppressWarnings("unchecked")
            Map<String, Object> data = (Map<String, Object>) result.get("data");
            String message = (String) result.get("message");
            ApiResponseDTO<Map<String, Object>> response = new ApiResponseDTO<>(status, message, data, LocalDateTime.now());
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            ApiResponseDTO<Map<String, Object>> response = new ApiResponseDTO<>("failure", e.getMessage(), null, LocalDateTime.now());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Failed to bulk update candidates due to: {}", e.getMessage());
            ApiResponseDTO<Map<String, Object>> response = new ApiResponseDTO<>("failure", "Failed to bulk update candidates: " + e.getMessage(), null, LocalDateTime.now());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
}