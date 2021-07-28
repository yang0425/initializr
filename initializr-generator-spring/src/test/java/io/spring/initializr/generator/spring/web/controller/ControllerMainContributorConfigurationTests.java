package io.spring.initializr.generator.spring.web.controller;

import java.nio.file.Path;

import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.project.MutableProjectDescription;
import io.spring.initializr.generator.spring.util.TestUtils;
import io.spring.initializr.generator.test.project.ProjectAssetTester;
import io.spring.initializr.generator.test.project.ProjectStructure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import org.springframework.core.io.ClassPathResource;

import static org.assertj.core.api.Assertions.assertThat;

class ControllerMainContributorConfigurationTests {

	private ProjectAssetTester projectTester;

	@BeforeEach
	void setup(@TempDir Path directory) {
		this.projectTester = TestUtils.defaultWebTester(directory);
	}

	@Test
	void mainClassIsContributedWithCode() {
		MutableProjectDescription description = new MutableProjectDescription();
		description.addDependency("web", Dependency.withCoordinates("web", "web").build());

		ProjectStructure project = this.projectTester.generate(description);

		assertThat(project).textFile("src/main/java/com/example/demo/controller/HelloWorldController.java")
				.hasSameContentAs(new ClassPathResource("project/java/web/controller/HelloWorldController.java.gen"));
	}

}
