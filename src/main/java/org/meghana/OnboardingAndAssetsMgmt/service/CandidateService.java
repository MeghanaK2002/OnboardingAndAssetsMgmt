package org.meghana.OnboardingAndAssetsMgmt.service;

import org.meghana.OnboardingAndAssetsMgmt.model.Candidate;
import org.meghana.OnboardingAndAssetsMgmt.payload.candidate.AttributeUpdateDTO;
import org.meghana.OnboardingAndAssetsMgmt.payload.candidate.BulkUpdateCandidateDTO;
import org.meghana.OnboardingAndAssetsMgmt.payload.candidate.CreateCandidateDTO;
import org.meghana.OnboardingAndAssetsMgmt.payload.candidate.UpdateCandidateDTO;
import org.meghana.OnboardingAndAssetsMgmt.repository.CandidateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CandidateService {

    private static final Logger logger = LoggerFactory.getLogger(CandidateService.class);

    @Autowired
    private CandidateRepository candidateRepository;

    public Candidate createCandidate(CreateCandidateDTO createCandidateDTO, Authentication authentication) throws Exception{
        if (candidateRepository.findByEmail(createCandidateDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("A candidate with the provided email already exists.");
        }
        if (candidateRepository.findByPhoneNo(createCandidateDTO.getPhoneNo()).isPresent()) {
            throw new IllegalArgumentException("A candidate with the provided phone number already exists.");
        }
        Candidate candidateToBeCreated = createCandidateDTO.toEntity();
        String generatedUserId = generateUniqueUserId(createCandidateDTO.getFirstName(), createCandidateDTO.getLastName());
        candidateToBeCreated.setUserId(generatedUserId);
        candidateToBeCreated.setProfileCreatedAt(LocalDateTime.now());
        candidateToBeCreated.setProfileCreatedBy(authentication.getName());
        Candidate createdCandidate = candidateRepository.save(candidateToBeCreated);
        logger.info("Candidature created successfully with User ID: {}", generatedUserId);
        return createdCandidate;
    }

    public List<Candidate> getAllCandidates() {
        return candidateRepository.findAll();
    }

    public Optional<Candidate> getCandidateByUserId(String userId) {
        boolean candidateExists = candidateRepository.existsByUserId(userId);
        if(candidateExists) {
            return candidateRepository.findByUserId(userId);
        }
        else{
            throw new IllegalArgumentException("Candidate with user id : "+userId+ " doesn't exist.");
        }
    }

    @Transactional
    public boolean deleteCandidateByUserId(String userId) throws Exception{
        boolean candidateExists = candidateRepository.existsByUserId(userId);
        if(candidateExists) {
            long deletedCount = candidateRepository.deleteByUserId(userId);
            logger.info("Attempted to delete candidate with user id = {}. Deleted {} record(s).", userId, deletedCount);
            return deletedCount > 0;
        }
        else{
            throw new IllegalArgumentException("Candidate with user id : "+userId+ " doesn't exist.");
        }
    }

    @Transactional
    public Candidate updateCandidate(String userId, UpdateCandidateDTO updateCandidateDTO, Authentication authentication) {
        logger.debug("Updating candidate with User ID: {}", userId);
        Candidate updatedCandidate = new Candidate();
        Optional<Candidate> existingCandidateOptional = candidateRepository.findByUserId(userId);

        if (existingCandidateOptional.isEmpty()) {
            logger.warn("Candidate not found with User ID: {}", userId);
            throw new IllegalArgumentException("Candidate not found with User ID: " + userId);
        }

        Candidate existingCandidate = existingCandidateOptional.get();
        boolean updated = false;

        // Use reflection to update fields
        Field[] dtoFields = UpdateCandidateDTO.class.getDeclaredFields();
        for (Field dtoField : dtoFields) {
            dtoField.setAccessible(true); // Allow access to private fields
            try {
                Object value = dtoField.get(updateCandidateDTO);
                if (value != null) {
                    try {
                        Field entityField = Candidate.class.getDeclaredField(dtoField.getName()); // Target the Candidate entity
                        entityField.setAccessible(true);
                        entityField.set(existingCandidate, value);
                        logger.trace("Updated field '{}' with value: '{}' for User ID: {}", dtoField.getName(), value, userId);
                        updated = true;
                    } catch (NoSuchFieldException e) {
                        logger.warn("Field '{}' not found in Candidate entity.", dtoField.getName());
                    }
                }
            } catch (IllegalAccessException e) {
                logger.error("Error accessing field '{}': {}", dtoField.getName(), e.getMessage());
                throw new RuntimeException("Error accessing field for update.", e); // Propagate exception
            }
        }

        if (updated) {
            existingCandidate.setProfileUpdatedAt(LocalDateTime.now());
            existingCandidate.setProfileUpdatedBy(authentication.getName());
            updatedCandidate = candidateRepository.save(existingCandidate);
            logger.info("Candidate with User ID {} updated successfully by {}", userId, authentication.getName());
            return updatedCandidate;
        } else {
            throw new IllegalArgumentException("No valid fields provided in the update payload.");
        }
    }

    @Transactional
    public Map<String, Object> bulkUpdateCandidates(BulkUpdateCandidateDTO bulkUpdateDTO, Authentication authentication) {
        List<String> userIds = bulkUpdateDTO.getCandidates();
        List<AttributeUpdateDTO> attributesToUpdate = bulkUpdateDTO.getAttributesToUpdate();
        String updatedBy = authentication.getName();
        LocalDateTime timestamp = LocalDateTime.now();
        List<String> invalidUsers = new ArrayList<>();
        List<String> invalidAttributes = new ArrayList<>();
        List<String> updatedUserIds = new ArrayList<>();
        List<Map<String, Object>> updatesDetailsList = new ArrayList<>(); // To hold details of updates

        if (userIds.isEmpty()) {
            return errorResponse("Error: No users specified in the payload", timestamp);
        }

        if (attributesToUpdate.isEmpty()) {
            return errorResponse("Error: No attributes to update specified in the payload", timestamp);
        }

        for (String userId : userIds) {
            Optional<Candidate> candidateOptional = candidateRepository.findByUserId(userId);
            if (candidateOptional.isPresent()) {
                Candidate candidate = candidateOptional.get();
                boolean userUpdated = false;
                Map<String, Object> userSpecificUpdates = new HashMap<>();
                for (AttributeUpdateDTO attributeUpdate : attributesToUpdate) {
                    String fieldName = attributeUpdate.getAttribute();
                    Object value = attributeUpdate.getValue();
                    try {
                        Field field = Candidate.class.getDeclaredField(fieldName); // Target Candidate entity
                        field.setAccessible(true);
                        field.set(candidate, value);
                        userSpecificUpdates.put(fieldName, value);
                        userUpdated = true;
                        logger.trace("Updated field '{}' to '{}' for User ID: {}", fieldName, value, userId);
                    } catch (NoSuchFieldException e) {
                        if (!invalidAttributes.contains(fieldName)) {
                            invalidAttributes.add(fieldName);
                        }
                        logger.warn("Field '{}' not found in Candidate entity for User ID: {}", fieldName, userId);
                    } catch (IllegalAccessException e) {
                        logger.error("Error accessing field '{}' for User ID: {} due to : {}", fieldName, userId, e.getMessage());
                        return errorResponse("Error: Could not update field '" + fieldName + "'", timestamp);
                    } catch (IllegalArgumentException e) {
                        logger.warn("Invalid value '{}' for field '{}' for User ID: {}", value, fieldName, userId);
                        return errorResponse("Error: Invalid value for field '" + fieldName + "'", timestamp);
                    }
                }
                if (userUpdated) {
                    candidate.setProfileUpdatedAt(timestamp);
                    candidate.setProfileUpdatedBy(updatedBy);
                    candidateRepository.save(candidate);
                    updatedUserIds.add(userId);
                    updatesDetailsList.add(Map.of("userId", userId, "updates", userSpecificUpdates)); // Capture update details
                }
            } else {
                invalidUsers.add(userId);
                logger.warn("Candidate not found with User ID: {}", userId);
            }
        }

        Map<String, Object> resultData = new HashMap<>();
        resultData.put("updatedCandidates", updatedUserIds);
        resultData.put("invalidCandidates", invalidUsers);
        resultData.put("invalidAttributes", invalidAttributes);
        resultData.put("updatesDetails", updatesDetailsList); // Include update details in the data

        String message;
        boolean success;
        if (invalidUsers.isEmpty() && invalidAttributes.isEmpty() && !updatedUserIds.isEmpty() && userIds.size() == updatedUserIds.size()) {
            message = "Operation Completed: All specified users were successfully updated.";
            success = true;
        } else if (!updatedUserIds.isEmpty()) {
            message = "Operation Partially Completed: Valid candidatures were successfully updated.";
            success = false;
        } else {
            message = "Operation Failed: No users were updated due to invalid IDs or attributes.";
            success = false;
        }

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("updatedCandidates", updatedUserIds);
        responseData.put("invalidCandidates", invalidUsers);
        responseData.put("invalidAttributes", invalidAttributes);
        responseData.put("updatesDetails", updatesDetailsList);

        return Map.of(
                "success", success,
                "message", message,
                "timestamp", timestamp,
                "data", responseData // Return the structured data
        );
    }

    private Map<String, Object> errorResponse(String message, LocalDateTime timestamp) {
        return Map.of(
                "success", false,
                "message", message,
                "timestamp", timestamp,
                "data", new HashMap<>()
        );
    }

    private String generateUniqueUserId(String firstName, String lastName){

        // Handle cases where first or last name might be missing
        if (firstName == null || firstName.isEmpty() || lastName == null || lastName.isEmpty()) {
            return "user_" + System.currentTimeMillis(); // Fallback to a timestamp-based ID
        }

        String baseUserId = firstName.substring(0, 1).toLowerCase() + lastName.toLowerCase();
        String uniqueUserId = baseUserId;

        //check if user id already exists and add an appropriate number if already present
        int counter = 1;
        while (candidateRepository.findByUserId(uniqueUserId).isPresent()) {
            uniqueUserId = baseUserId + counter;
            counter++;
        }

        return uniqueUserId;
    }
}