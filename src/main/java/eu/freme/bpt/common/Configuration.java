package eu.freme.bpt.common;

import org.apache.commons.cli.CommandLine;

import java.io.File;

/**
 * Copyright (C) 2016 Agro-Know, Deutsches Forschungszentrum für Künstliche Intelligenz, iMinds,
 * Institut für Angewandte Informatik e. V. an der Universität Leipzig,
 * Istituto Superiore Mario Boella, Tilde, Vistatec, WRIPL (http://freme-project.eu)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Holds the configuration of the tool. This includes parameter options and properties.
 *
 */
public class Configuration {
	private final File inputFile;
	private final File outputDir;
	private final Format inFormat;
	private final Format outFormat;

	public static Configuration create(CommandLine commandLine) {
		File inputFile = commandLine.hasOption("if") ? new File(commandLine.getOptionValue("if")) : null;
		File outputDir = commandLine.hasOption("od") ? new File(commandLine.getOptionValue("od")) : null;
		Format inFormat = commandLine.hasOption('f') ? Format.valueOf(commandLine.getOptionValue('f')) : Format.turtle;
		Format outFormat = commandLine.hasOption('o') ? Format.valueOf(commandLine.getOptionValue('o')) : Format.turtle;

		return new Configuration(inputFile, outputDir, inFormat, outFormat);
	}

	private Configuration(File inputFile, File outputDir, Format inFormat, Format outFormat) {
		this.inputFile = inputFile;
		this.outputDir = outputDir;
		this.inFormat = inFormat;
		this.outFormat = outFormat;
	}

	public File getInputFile() {
		return inputFile;
	}

	public File getOutputDir() {
		return outputDir;
	}

	public Format getInFormat() {
		return inFormat;
	}

	public Format getOutFormat() {
		return outFormat;
	}
}
