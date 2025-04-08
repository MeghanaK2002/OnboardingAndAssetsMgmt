package org.meghana.OnboardingAndAssetsMgmt.payload.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AccountViewDTO {
    
    private String id;

    private String email;

    private String authorities;
}
