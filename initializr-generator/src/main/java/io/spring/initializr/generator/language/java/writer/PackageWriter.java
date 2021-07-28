package io.spring.initializr.generator.language.java.writer;

import io.spring.initializr.generator.io.IndentingWriter;

public class PackageWriter implements CodeWriter {

	private final String packageName;

	public PackageWriter(String packageName) {
		this.packageName = packageName;
	}

	@Override
	public void write(IndentingWriter writer) {
		writer.println(String.format("package %s;", packageName));
		writer.println();
	}

}
