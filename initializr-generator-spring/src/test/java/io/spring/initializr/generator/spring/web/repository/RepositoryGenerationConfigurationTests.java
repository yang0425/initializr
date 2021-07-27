package io.spring.initializr.generator.spring.web.repository;

import static org.assertj.core.api.Assertions.assertThat;

import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.buildsystem.maven.MavenBuildSystem;
import io.spring.initializr.generator.language.java.JavaLanguage;
import io.spring.initializr.generator.project.MutableProjectDescription;
import io.spring.initializr.generator.spring.code.java.JavaProjectGenerationConfiguration;
import io.spring.initializr.generator.test.project.ProjectAssetTester;
import io.spring.initializr.generator.test.project.ProjectStructure;
import io.spring.initializr.generator.version.Version;
import java.nio.file.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.core.io.ClassPathResource;

class RepositoryGenerationConfigurationTests {

    private ProjectAssetTester projectTester;

    @BeforeEach
    void setup(@TempDir Path directory) {
        this.projectTester = new ProjectAssetTester().withIndentingWriterFactory()
            .withConfiguration(JavaProjectGenerationConfiguration.class,
                RepositoryGenerationConfiguration.class)
            .withDirectory(directory).withDescriptionCustomizer((description) -> {
                description.setLanguage(new JavaLanguage());
                if (description.getPlatformVersion() == null) {
                    description.setPlatformVersion(Version.parse("2.1.0.RELEASE"));
                }
                description.setBuildSystem(new MavenBuildSystem());
            });
    }

    @Test
    void mainClassIsNotContributedWithoutWebDependency() {
        MutableProjectDescription description = new MutableProjectDescription();

        ProjectStructure project = this.projectTester.generate(description);

        assertThat(project)
            .doesNotContainFiles("src/main/java/com/example/demo/repository/HelloWorldRepository.java");
    }

    @Test
    void mainClassIsContributed() {
        MutableProjectDescription description = new MutableProjectDescription();
        description.addDependency("web", Dependency.withCoordinates("web", "web").build());

        ProjectStructure project = this.projectTester.generate(description);

        assertThat(project)
            .containsFiles("src/main/java/com/example/demo/repository/HelloWorldRepository.java");
    }

    @Test
    void mainClassIsContributedWithCode() {
        MutableProjectDescription description = new MutableProjectDescription();
        description.addDependency("web", Dependency.withCoordinates("web", "web").build());

        ProjectStructure project = this.projectTester.generate(description);

        assertThat(project).textFile("src/main/java/com/example/demo/repository/HelloWorldRepository.java")
            .hasSameContentAs(new ClassPathResource("repository/HelloWorldRepository.java"));
    }

}
