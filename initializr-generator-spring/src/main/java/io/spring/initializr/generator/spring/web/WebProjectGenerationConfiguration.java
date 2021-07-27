package io.spring.initializr.generator.spring.web;

import io.spring.initializr.generator.condition.ConditionalOnRequestedDependency;
import io.spring.initializr.generator.project.ProjectGenerationConfiguration;
import io.spring.initializr.generator.spring.web.controller.ControllerMainContributorConfiguration;
import io.spring.initializr.generator.spring.web.repository.RepositoryMainContributorConfiguration;
import io.spring.initializr.generator.spring.web.service.ServiceMainContributorConfiguration;
import org.springframework.context.annotation.Import;

@ProjectGenerationConfiguration
@ConditionalOnRequestedDependency("web")
@Import({ControllerMainContributorConfiguration.class,
    ServiceMainContributorConfiguration.class,
    RepositoryMainContributorConfiguration.class})
public class WebProjectGenerationConfiguration {

}
