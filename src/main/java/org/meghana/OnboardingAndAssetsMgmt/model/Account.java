package org.meghana.OnboardingAndAssetsMgmt.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "accounts")
@Setter
@Getter
@ToString
public class Account {

    public void setId(String id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getAuthorities() {
        return Authorities;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAuthorities(String authorities) {
        Authorities = authorities;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    @Id
    private String id;

    private String email;

    private String password;

    private String Authorities;

    private boolean approved = false;
}
