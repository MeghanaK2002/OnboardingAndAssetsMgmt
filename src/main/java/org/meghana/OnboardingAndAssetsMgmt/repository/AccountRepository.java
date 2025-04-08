package org.meghana.OnboardingAndAssetsMgmt.repository;

import org.meghana.OnboardingAndAssetsMgmt.model.Account;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AccountRepository extends MongoRepository<Account, String>{

    Optional<Account> findByEmail(String email);
    
}
