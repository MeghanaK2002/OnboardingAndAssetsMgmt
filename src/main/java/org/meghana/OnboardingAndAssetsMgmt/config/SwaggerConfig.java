package org.meghana.OnboardingAndAssetsMgmt.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.context.annotation.Configuration;


@Configuration
@OpenAPIDefinition(
  info =@Info(
    title = "Onborading and Asset Management System",
    version = "Verions 1.0",
    contact = @Contact(
      name = "Meghana", email = "meghanakrishnappa2002@gmail.com", url = "https://www.linkedin.com/in/meghana-k-1131281a0"
    ),
    license = @License(
      name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0"
    ),
    description = "System to manage user onboarding and asset management"
  )
)
public class SwaggerConfig {
}
