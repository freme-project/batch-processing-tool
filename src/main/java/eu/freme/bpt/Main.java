package eu.freme.bpt;

import eu.freme.bpt.input.DirectoryInputIterator;
import eu.freme.bpt.input.InputIterator;
import eu.freme.bpt.input.StandardInputIterator;
import org.apache.commons.cli.*;

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
 */
public class Main {
	public static void main(String[] args) {

		// create options that will be parsed from the args
		Option helpOption = new Option("h", "help", false, "Prints this message");
		Option inputOption = Option.builder("i").longOpt("input").argName("input file(s)")
				.desc("The input file or directory to process. In case of a directory, each file in that directory is processed").hasArg().build();

		Options options = new Options()
				.addOption(helpOption)
				.addOption(inputOption);

		CommandLine commandLine = null;
		int exitValue;
		try {
			CommandLineParser parser = new DefaultParser();
			commandLine = parser.parse(options, args);
			exitValue = 0;
		} catch (ParseException e) {
			System.err.println(e.getMessage());
			exitValue = 1;
		}
		if ((exitValue != 0) || commandLine.hasOption("h")) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.setWidth(132);
			formatter.printHelp("java -jar <this jar file> ", options, true);
			System.exit(exitValue);
		}

		// Create an InputIterator. This will be used to iterate over the input source(s)
		InputIterator inputIterator;
		if (commandLine.hasOption('i')) {
			File input = new File(commandLine.getOptionValue('i'));
			if (input.isDirectory()) {
				inputIterator = new DirectoryInputIterator(input);
			} else {
				inputIterator = new DirectoryInputIterator(input);
			}
		} else {
			inputIterator = new StandardInputIterator();
		}

		// TODO now do something useful with the iterator!

	}
}
