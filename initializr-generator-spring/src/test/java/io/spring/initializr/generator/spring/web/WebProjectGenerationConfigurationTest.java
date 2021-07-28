package io.spring.initializr.generator.spring.web;

import java.nio.file.Path;

import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.project.MutableProjectDescription;
import io.spring.initializr.generator.spring.util.TestUtils;
import io.spring.initializr.generator.test.project.ProjectAssetTester;
import io.spring.initializr.generator.test.project.ProjectStructure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.assertj.core.api.Assertions.assertThat;

class WebProjectGenerationConfigurationTest {

	private ProjectAssetTester projectTester;

	@BeforeEach
	void setup(@TempDir Path directory) {
		this.projectTester = TestUtils.defaultWebTester(directory);
	}

	@Test
	void classesAreNotContributedWithoutWebDependency() {
		MutableProjectDescription description = new MutableProjectDescription();

		ProjectStructure project = this.projectTester.generate(description);

		assertThat(project).doesNotContainFiles("src/main/java/com/example/demo/controller/HelloWorldController.java",
				"src/main/java/com/example/demo/service/HelloWorldService.java",
				"src/main/java/com/example/demo/repository/HelloWorldRepository.java",
				"src/main/java/com/example/demo/entity/HelloWorld.java",
				"src/main/java/com/example/demo/dto/HelloWorldDto.java");
	}

	@Test
	void classesAreContributedWithWebDependency() {
		MutableProjectDescription description = new MutableProjectDescription();
		description.addDependency("web", Dependency.withCoordinates("web", "web").build());

		ProjectStructure project = this.projectTester.generate(description);

		assertThat(project).containsFiles("src/main/java/com/example/demo/controller/HelloWorldController.java",
				"src/main/java/com/example/demo/service/HelloWorldService.java",
				"src/main/java/com/example/demo/repository/HelloWorldRepository.java",
				"src/main/java/com/example/demo/entity/HelloWorld.java",
				"src/main/java/com/example/demo/dto/HelloWorldDto.java");
	}

	@Test
	void classesAreContributedWithoutWebDependency() {
		MutableProjectDescription description = new MutableProjectDescription();

		ProjectStructure project = this.projectTester.generate(description);

		assertThat(project).containsFiles("src/test/java/com/example/demo/DemoApplicationTests.java");
	}

	@Test
	void classesAreNotContributedWithWebDependency() {
		MutableProjectDescription description = new MutableProjectDescription();
		description.addDependency("web", Dependency.withCoordinates("web", "web").build());

		ProjectStructure project = this.projectTester.generate(description);

		assertThat(project).doesNotContainFiles("src/test/java/com/example/demo/DemoApplicationTests.java");
	}

}
