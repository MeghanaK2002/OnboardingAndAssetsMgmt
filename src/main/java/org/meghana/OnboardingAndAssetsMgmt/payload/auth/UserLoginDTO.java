package org.meghana.OnboardingAndAssetsMgmt.payload.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLoginDTO {
   
    @Email
    @Schema(description = "Email address", example = "user@user.com", requiredMode = RequiredMode.REQUIRED)
    private String email;

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Size(min = 6, max = 20)
    @Schema(description = "Password", example = "pass987", 
    requiredMode = RequiredMode.REQUIRED, maxLength = 20, minLength = 6)
    private String password;
}
