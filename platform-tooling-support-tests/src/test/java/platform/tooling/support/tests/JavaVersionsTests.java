/*
 * Copyright 2015-2024 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * https://www.eclipse.org/legal/epl-v20.html
 */

package platform.tooling.support.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static platform.tooling.support.Helper.TOOL_TIMEOUT;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import de.sormuras.bartholdy.tool.Java;

import org.junit.jupiter.api.Test;

import platform.tooling.support.Helper;
import platform.tooling.support.MavenRepo;
import platform.tooling.support.Request;

/**
 * @since 1.4
 */
class JavaVersionsTests {

	@GlobalResource
	LocalMavenRepo localMavenRepo;

	@Test
	void java_8() {
		var java8Home = Helper.getJavaHome("8");
		assumeTrue(java8Home.isPresent(), "Java 8 installation directory not found!");
		var actualLines = execute("8", java8Home.get(), Map.of());

		assertTrue(actualLines.contains("[WARNING] Tests run: 2, Failures: 0, Errors: 0, Skipped: 1"));
	}

	@Test
	void java_default() {
		var actualLines = execute("default", new Java().getHome(), MavenEnvVars.FOR_JDK24_AND_LATER);

		assertTrue(actualLines.contains("[WARNING] Tests run: 2, Failures: 0, Errors: 0, Skipped: 1"));
	}

	List<String> execute(String version, Path javaHome, Map<String, String> environmentVars) {
		var builder = Request.builder() //
				.setTool(Request.maven()) //
				.setProject(Projects.JAVA_VERSIONS) //
				.setWorkspace("java-versions-" + version) //
				.addArguments(localMavenRepo.toCliArgument(), "-Dmaven.repo=" + MavenRepo.dir()) //
				.addArguments("--update-snapshots", "--batch-mode", "verify") //
				.setTimeout(TOOL_TIMEOUT) //
				.setJavaHome(javaHome);
		environmentVars.forEach(builder::putEnvironment);

		var result = builder.build().run();

		assertFalse(result.isTimedOut(), () -> "tool timed out: " + result);
		assertEquals(0, result.getExitCode());
		assertEquals("", result.getOutput("err"));
		return result.getOutputLines("out");
	}

}
