package org.junit.jupiter.params.csv;/*
 * Copyright 2015-2025 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * https://www.eclipse.org/legal/epl-v20.html
 */

import static org.junit.jupiter.params.csv.MockCsvAnnotationBuilder.csvFileSource;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.CsvFileArgumentsProvider;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.support.ParameterDeclarations;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

/**
 * @since 6.0
 */
@State(Scope.Benchmark)
@Fork(1)
@Warmup(iterations = 1, time = 2)
@Measurement(iterations = 3, time = 2)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class CsvFileArgumentsProviderBenchmark {

	@Param({ "20", "50", "100", "500", "1000", "5000", "10000" })
	private int lineCount;

	@Param({ "3", "5", "10" })
	private int columnCount;

	private CsvFileSource annotation;

	private ParameterDeclarations parameterDeclarations;

	private ExtensionContext extensionContext;

	@Setup
	public void setUp() throws IOException {
		Files.writeString(
				Path.of("test.csv"),
				BenchmarkUtils.generateTextBlock(lineCount, columnCount),
				StandardOpenOption.TRUNCATE_EXISTING
		);


		annotation = csvFileSource()
				.useHeadersInDisplayName(true)
				.files("test.csv")
				.build();

		parameterDeclarations = mock();
		extensionContext = mock(ExtensionContext.class);
	}

	@Benchmark
	public Object[] benchmark() {
		var provider = new CsvFileArgumentsProvider();
		provider.accept(annotation);
		return provider.provideArguments(parameterDeclarations, extensionContext).toArray();
	}

}
