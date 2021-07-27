package io.spring.initializr.generator.spring.web;

import io.spring.initializr.generator.condition.ConditionalOnRequestedDependency;
import io.spring.initializr.generator.project.ProjectGenerationConfiguration;
import io.spring.initializr.generator.spring.web.controller.ControllerMainContributorConfiguration;
import io.spring.initializr.generator.spring.web.dto.DtoMainContributorConfiguration;
import io.spring.initializr.generator.spring.web.entity.EntityMainContributorConfiguration;
import io.spring.initializr.generator.spring.web.repository.RepositoryMainContributorConfiguration;
import io.spring.initializr.generator.spring.web.service.ServiceMainContributorConfiguration;
import org.springframework.context.annotation.Import;

@ProjectGenerationConfiguration
@ConditionalOnRequestedDependency("web")
@Import({ControllerMainContributorConfiguration.class,
    ServiceMainContributorConfiguration.class,
    RepositoryMainContributorConfiguration.class,
    EntityMainContributorConfiguration.class,
    DtoMainContributorConfiguration.class})
public class WebProjectGenerationConfiguration {

}
