package org.meghana.OnboardingAndAssetsMgmt.repository;


import org.meghana.OnboardingAndAssetsMgmt.model.Candidate;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CandidateRepository extends MongoRepository<Candidate, String> {
    Optional<Candidate> findByUserId(String userId);

    long deleteByUserId(String userId);

    boolean existsByUserId(String userId);

    Optional<Candidate> findByEmail(String email);

    Optional<Candidate> findByPhoneNumber(String phoneNumber);
}