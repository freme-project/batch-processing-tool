package eu.freme.bpt;

import eu.freme.bpt.io.IOIterator;
import eu.freme.bpt.io.IteratorFactory;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

	public static final Logger logger = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) {

		// create options that will be parsed from the args
		Option helpOption = new Option("h", "help", false, "Prints this message");
		Option inputOption = Option.builder("if").longOpt("input-file").argName("input file")
				.desc("The input file or directory to process. In case of a directory, each file in that directory is processed. " +
						"If not given, standard in is used.").hasArg().build();
		Option outputOption = Option.builder("od").longOpt("output-dir").argName("output dir")
				.desc("The output directory. If not given, output is written to standard out.").hasArg().build();

		Options options = new Options()
				.addOption(helpOption)
				.addOption(inputOption)
				.addOption(outputOption);

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

		// Create an IOIterator. This will be used to iterate over the input source(s)
		IOIterator ioIterator;
		try {
			ioIterator = IteratorFactory.create(commandLine);
		} catch (Exception e) {
			logger.error("Cannot handle input or output. Reason: ", e);
			System.exit(2);
		}

	}
}
