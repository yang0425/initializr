package io.spring.initializr.generator.language.java;

import java.util.Collections;
import java.util.List;

import io.spring.initializr.generator.io.IndentingWriter;

public class JavaEmptyLineStatement implements JavaStatement {

	@Override
	public void write(IndentingWriter writer) {
		writer.println();
	}

	@Override
	public List<String> getImports() {
		return Collections.emptyList();
	}

	@Override
	public List<String> getStaticImports() {
		return Collections.emptyList();
	}

}
