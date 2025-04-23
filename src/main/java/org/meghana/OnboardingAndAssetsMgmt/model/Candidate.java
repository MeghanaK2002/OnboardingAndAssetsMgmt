package org.meghana.OnboardingAndAssetsMgmt.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "candidates")
@Setter
@Getter
@ToString
public class Candidate {

    @Id
    private String candidateId;

    private String userId;

    private String firstName;

    private String middleName;

    private String lastName;

    private String hiringPartner;

    private String candidatureStage;

    private LocalDateTime profileCreatedAt;

    private LocalDateTime profileUpdatedAt;

    private String profileCreatedBy;

    private String profileUpdatedBy;

    private String jobProfile;

    private String govtId;

    private String resume;

    private String prevExp;

    private String phoneNo;

    private String email;

    private String address;

    private String preferredLocation;

    private String preferredShift;

    private Boolean workVisaRequired;

    private String gender;

    private Boolean hasDisability;

    private String disability;

    private String profilePicture;

    private String Nationality;

}