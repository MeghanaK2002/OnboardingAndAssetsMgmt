package org.meghana.OnboardingAndAssetsMgmt.payload.candidate;

import jakarta.validation.constraints.*;
import lombok.*;
import org.meghana.OnboardingAndAssetsMgmt.model.Candidate;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CreateCandidateDTO {

    @NotBlank(message = "First name is required")
    @NotNull
    private String firstName;

    private String middleName;

    @NotBlank(message = "Last name is required")
    @NotNull
    private String lastName;

    @NotBlank(message = "Hiring Partner ID is required")
    private String hiringPartner;

    @NotBlank(message = "Job Profile ID is required")
    private String jobProfile;

    private String govtId;

    private String resume;

    private String prevExp;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits")
    @NotNull
    private String phoneNo;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @NotNull
    private String email;

    @NotBlank(message = "Address is required")
    @NotNull
    private String address;

    private String preferredLocation;

    private String preferredShift;

    @NotNull(message = "Work visa requirement must be specified")
    private Boolean workVisaRequired;

    @NotBlank(message = "Gender is required")
    private String gender;

    @NotNull(message = "Disability status must be specified")
    private Boolean hasDisability;

    private String disability;

    private String profilePicture;

    @NotBlank(message = "Nationality is required")
    @NotNull
    private String Nationality;

    //DTO to Entity method -> helpful in services
    public Candidate toEntity() {
        Candidate candidate = new Candidate();
        candidate.setFirstName(this.firstName);
        candidate.setMiddleName(this.middleName);
        candidate.setLastName(this.lastName);
        candidate.setHiringPartner(this.hiringPartner);
        candidate.setCandidatureStage("ShortListed");
        // Note: Creation and update timestamps/users are often handled in the service layer
        candidate.setJobProfile(this.jobProfile);
        candidate.setGovtId(this.govtId);
        candidate.setResume(this.resume);
        candidate.setPrevExp(this.prevExp);
        candidate.setPhoneNo(this.phoneNo);
        candidate.setEmail(this.email);
        candidate.setAddress(this.address);
        candidate.setPreferredLocation(this.preferredLocation);
        candidate.setPreferredShift(this.preferredShift);
        candidate.setWorkVisaRequired(this.workVisaRequired);
        candidate.setGender(this.gender);
        candidate.setHasDisability(this.hasDisability);
        candidate.setDisability(this.disability);
        candidate.setProfilePicture(this.profilePicture);
        candidate.setNationality(this.Nationality);
        return candidate;
    }
}