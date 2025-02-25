/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.spring.initializr.generator.language.java;

import java.util.List;

import io.spring.initializr.generator.io.IndentingWriter;

/**
 * A return statement.
 *
 * @author Andy Wilkinson
 */
public class JavaReturnStatement implements JavaStatement {

	private final JavaExpression expression;

	public JavaReturnStatement(JavaExpression expression) {
		this.expression = expression;
	}

	public JavaExpression getExpression() {
		return this.expression;
	}

	@Override
	public List<String> getImports() {
		return this.expression.getImports();
	}

	@Override
	public List<String> getStaticImports() {
		return this.expression.getStaticImports();
	}

	@Override
	public void write(IndentingWriter writer) {
		writer.print("return ");
		this.expression.write(writer);
		writer.println(";");
	}

}
