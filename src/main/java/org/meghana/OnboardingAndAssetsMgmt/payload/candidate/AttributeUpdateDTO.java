package org.meghana.OnboardingAndAssetsMgmt.payload.candidate;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class AttributeUpdateDTO {

    @NotEmpty(message = "Attribute name cannot be empty.")
    private String attribute;

    @NotNull(message = "Attribute value cannot be null.")
    private Object value;
}