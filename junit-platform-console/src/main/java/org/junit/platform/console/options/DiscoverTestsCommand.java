/*
 * Copyright 2015-2023 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * https://www.eclipse.org/legal/epl-v20.html
 */

package org.junit.platform.console.options;

import java.io.PrintWriter;

import org.junit.platform.console.tasks.ConsoleTestExecutor;

import picocli.CommandLine;
import picocli.CommandLine.Mixin;

@CommandLine.Command(name = "discover", description = "Discover tests")
public class DiscoverTestsCommand extends BaseCommand<Void> {

	@Mixin
	CommandLineOptionsMixin options;

	@Override
	protected Void execute(PrintWriter out) {
		new ConsoleTestExecutor(options.toCommandLineOptions()).discover(out);
		return null;
	}
}