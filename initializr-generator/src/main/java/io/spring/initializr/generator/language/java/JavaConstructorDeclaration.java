package io.spring.initializr.generator.language.java;

import io.spring.initializr.generator.language.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class JavaConstructorDeclaration {

    private final int modifiers;

    private final List<Parameter> parameters;

    private final List<JavaStatement> statements;

    private JavaConstructorDeclaration(int modifiers, List<Parameter> parameters, List<JavaStatement> statements) {
        this.modifiers = modifiers;
        this.parameters = parameters;
        this.statements = statements;
    }

    public static JavaConstructorDeclaration.Builder constructor() {
        return new JavaConstructorDeclaration.Builder();
    }

    public int getModifiers() {
        return this.modifiers;
    }

    public List<Parameter> getParameters() {
        return this.parameters;
    }

    public List<JavaStatement> getStatements() {
        return this.statements;
    }

    public static final class Builder {

        private List<Parameter> parameters = new ArrayList<>();

        private int modifiers;

        private Builder() {
        }

        public JavaConstructorDeclaration.Builder modifiers(int modifiers) {
            this.modifiers = modifiers;
            return this;
        }

        public JavaConstructorDeclaration.Builder parameters(Parameter... parameters) {
            this.parameters = Arrays.asList(parameters);
            return this;
        }

        public JavaConstructorDeclaration body(JavaStatement... statements) {
            return new JavaConstructorDeclaration(this.modifiers, this.parameters,
                Arrays.asList(statements));
        }

    }

}
