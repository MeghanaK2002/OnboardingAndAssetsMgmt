package org.meghana.OnboardingAndAssetsMgmt.payload.candidate;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class UpdateCandidateDTO {

    private String firstName;
    private String middleName;
    private String lastName;
    private String hiringPartner;
    private String candidatureStage;
    private String jobProfile;

    //Documents
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