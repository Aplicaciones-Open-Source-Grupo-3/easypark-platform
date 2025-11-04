package com.easypark.platform.shared.infrastructure.persistence.jpa.configuration.strategy;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

import static io.github.encryptorcode.pluralize.Pluralize.pluralize;

public class SnakeCasePhysicalNamingStrategy implements PhysicalNamingStrategy {

    @Override
    public Identifier toPhysicalCatalogName(Identifier identifier, JdbcEnvironment jdbcEnvironment) {
        return this.toSnakeCase(identifier);
    }

    @Override
    public Identifier toPhysicalSchemaName(Identifier identifier, JdbcEnvironment jdbcEnvironment) {
        return this.toSnakeCase(identifier);
    }

    @Override
    public Identifier toPhysicalTableName(Identifier identifier, JdbcEnvironment jdbcEnvironment) {
        return this.toSnakeCase(this.toPlural(identifier));
    }

    @Override
    public Identifier toPhysicalSequenceName(Identifier identifier, JdbcEnvironment jdbcEnvironment) {
        return this.toSnakeCase(identifier);
    }

    @Override
    public Identifier toPhysicalColumnName(Identifier identifier, JdbcEnvironment jdbcEnvironment) {
        return this.toSnakeCase(identifier);
    }

    private Identifier toSnakeCase(Identifier identifier) {
        if (identifier == null) return null;

        String name = identifier.getText();
        String snakeCaseName = name.replaceAll("([a-z]+)([A-Z]+)", "$1_$2").toLowerCase();
        return !snakeCaseName.equals(name)
            ? Identifier.toIdentifier(snakeCaseName)
            : identifier;
    }

    private Identifier toPlural(Identifier identifier) {
        if (identifier == null) return null;

        String name = identifier.getText();
        String pluralName = pluralize(name);
        return !pluralName.equals(name)
            ? Identifier.toIdentifier(pluralName)
            : identifier;
    }
}

