package io.spring.initializr.generator.spring.web.repository;

import io.spring.initializr.generator.language.CompilationUnit;
import io.spring.initializr.generator.language.SourceCode;
import io.spring.initializr.generator.language.TypeDeclaration;
import io.spring.initializr.generator.spring.code.MainSourceCodeCustomizer;
import io.spring.initializr.generator.spring.util.LambdaSafe;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.ObjectProvider;

class RepositoryMainContributor implements
    MainSourceCodeCustomizer<TypeDeclaration, CompilationUnit<TypeDeclaration>, SourceCode<TypeDeclaration, CompilationUnit<TypeDeclaration>>> {

  private static final String CLASS_NAME = "HelloWorldRepository";

  private final String packageName;

  private final ObjectProvider<RepositoryMainCustomizer<?>> repositoryMainCustomizers;

  RepositoryMainContributor(String packageName,
                            ObjectProvider<RepositoryMainCustomizer<?>> repositoryMainCustomizers) {
    this.packageName = packageName;
    this.repositoryMainCustomizers = repositoryMainCustomizers;
  }

  @Override
  public void customize(SourceCode<TypeDeclaration, CompilationUnit<TypeDeclaration>> sourceCode) {
    CompilationUnit<TypeDeclaration> compilationUnit =
        sourceCode.createCompilationUnit(this.packageName, CLASS_NAME);
    TypeDeclaration typeDeclaration = compilationUnit.createTypeDeclaration(CLASS_NAME);
    customizeControllerMain(typeDeclaration);
  }

  @SuppressWarnings("unchecked")
  private void customizeControllerMain(TypeDeclaration typeDeclaration) {
    List<RepositoryMainCustomizer<?>> customizers = this.repositoryMainCustomizers.orderedStream()
        .collect(Collectors.toList());
    LambdaSafe.callbacks(RepositoryMainCustomizer.class, customizers, typeDeclaration)
        .invoke((customizer) -> customizer.customize(typeDeclaration));
  }

}
