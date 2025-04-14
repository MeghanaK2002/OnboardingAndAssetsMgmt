package org.meghana.OnboardingAndAssetsMgmt.service;

import org.meghana.OnboardingAndAssetsMgmt.model.Candidate;
import org.meghana.OnboardingAndAssetsMgmt.payload.candidate.CreateCandidateDTO;
import org.meghana.OnboardingAndAssetsMgmt.repository.CandidateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CandidateService {

    @Autowired
    private CandidateRepository candidateRepository;

    public String createCandidate(CreateCandidateDTO createCandidateDTO, Authentication authentication) throws Exception{
        Candidate candidateToBeCreated = createCandidateDTO.toEntity();
        String generatedUserId = generateUniqueUserId(createCandidateDTO.getFirstName(), createCandidateDTO.getLastName());
        candidateToBeCreated.setUserId(generatedUserId);
        candidateToBeCreated.setProfileCreatedAt(LocalDateTime.now());
        candidateToBeCreated.setProfileCreatedBy(authentication.getName());
        candidateRepository.save(candidateToBeCreated);
        return "Candidature created successfully with User ID: " + generatedUserId;
    }

    public List<Candidate> getAllCandidates() {
        return candidateRepository.findAll();
    }

    public Optional<Candidate> getCandidateByUserId(String userId) {
        return candidateRepository.findByUserId(userId);
    }

    public String deleteCandidateByUserId(String userId) {
        candidateRepository.deleteByUserId(userId);
        return "Candidate with user id = "+userId+" deleted successfully.";
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