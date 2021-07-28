package io.spring.initializr.generator.language.java.writer;

import io.spring.initializr.generator.io.IndentingWriter;

public interface CodeWriter {

	void write(IndentingWriter writer);

	default String getUnqualifiedName(String name) {
		if (!name.contains(".")) {
			return name;
		}
		return name.substring(name.lastIndexOf(".") + 1);
	}

}
