package io.spring.initializr.generator.spring.util;

import java.nio.file.Path;

import io.spring.initializr.generator.buildsystem.maven.MavenBuildSystem;
import io.spring.initializr.generator.language.java.JavaLanguage;
import io.spring.initializr.generator.spring.code.java.JavaProjectGenerationConfiguration;
import io.spring.initializr.generator.spring.web.WebProjectGenerationConfiguration;
import io.spring.initializr.generator.test.project.ProjectAssetTester;
import io.spring.initializr.generator.version.Version;

public final class TestUtils {

	private TestUtils() {
	}

	public static ProjectAssetTester defaultWebTester(Path directory) {
		return new ProjectAssetTester().withIndentingWriterFactory()
				.withConfiguration(JavaProjectGenerationConfiguration.class, WebProjectGenerationConfiguration.class)
				.withDirectory(directory).withDescriptionCustomizer((description) -> {
					description.setLanguage(new JavaLanguage());
					description.setPlatformVersion(Version.parse("2.1.0.RELEASE"));
					description.setBuildSystem(new MavenBuildSystem());
				});
	}

}
