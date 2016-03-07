package eu.freme.bpt;

import eu.freme.bpt.io.IOIterator;
import eu.freme.bpt.io.IteratorFactory;
import eu.freme.bpt.util.Pair;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
		final List<String> services = Arrays.asList("e-entity", "e-translate");	// TODO add rest
		Pair<String, String[]> serviceAndArgs = extractService(args, services);

		// create options that will be parsed from the args

		/////// General BPT options ///////
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

		/////// Common service options ///////
		Option informatOption = Option.builder("f").longOpt("informat").argName("FORMAT").desc("The format of the input document(s). Defaults to 'turtle'").build();
		Option outformatOption = Option.builder("o").longOpt("outformat").argName("FORMAT").desc("The desired output format of the service. Defaults to 'turtle'").build();
		options.addOption(informatOption).addOption(outformatOption);

		/////// Service specific options ///////
		// TODO

		String service = serviceAndArgs.getName();

		CommandLine commandLine = null;
		int exitValue;
		try {
			CommandLineParser parser = new DefaultParser();
			commandLine = parser.parse(options, serviceAndArgs.getValue());
			exitValue = 0;
		} catch (ParseException e) {
			System.err.println(e.getMessage());
			exitValue = 1;
		}
		if ((exitValue != 0) || commandLine.hasOption("h") || service == null) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.setWidth(132);
			formatter.printHelp("java -jar <this jar file> ", options, true);
			System.exit(exitValue);
		}

		logger.debug("Commandline successfully parsed!");

		// Create an IOIterator. This will be used to iterate over the input source(s)
		IOIterator ioIterator;
		try {
			ioIterator = IteratorFactory.create(commandLine);
			System.out.println();
		} catch (Exception e) {
			logger.error("Cannot handle input or output. Reason: ", e);
			System.exit(2);
		}
	}

	/**
	 * Finds and removes the service from the arguments array. This helps processing the rest of the parameters
	 * using the Apache Commons CLI library (and thus needs to be called before processing the arguments).
	 * @param args        The arguments of the program.
	 * @param services    The list of registered services.
	 * @return			The service name if found, or {@code null} if not found.
	 */
	private static Pair<String, String[]> extractService(String[] args, final List<String> services) {
		String foundService = null;
		List<String> newArgs = new ArrayList<>(args.length);
		for (String arg : args) {
			if (!services.contains(arg)) {
				newArgs.add(arg);
			} else {
				foundService = arg;
			}
		}
		return new Pair<>(foundService, newArgs.toArray(new String[newArgs.size()]));
	}
}
