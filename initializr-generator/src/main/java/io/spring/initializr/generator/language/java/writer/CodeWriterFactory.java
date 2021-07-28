package io.spring.initializr.generator.language.java.writer;

import io.spring.initializr.generator.language.java.JavaCompilationUnit;

public final class CodeWriterFactory {

	private CodeWriterFactory() {

	}

	public static CompilationUtilWriter createWriter(JavaCompilationUnit compilationUnit) {
		return new CompilationUtilWriter(compilationUnit);
	}

}
