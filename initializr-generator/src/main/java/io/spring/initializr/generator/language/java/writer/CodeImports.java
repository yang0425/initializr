package io.spring.initializr.generator.language.java.writer;

import java.util.List;

public interface CodeImports {

	List<String> getImports();

	List<String> getStaticImports();

}
