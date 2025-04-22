package org.meghana.OnboardingAndAssetsMgmt.payload.candidate;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Setter
@Getter
@ToString
public class BulkUpdateCandidateDTO {

    @NotEmpty(message = "List of user IDs cannot be empty.")
    private List<String> candidates;

    @NotEmpty(message = "List of attributes to update cannot be empty.")
    private List<@Valid AttributeUpdateDTO> attributesToUpdate;
}