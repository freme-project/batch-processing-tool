package eu.freme.bpt;

import com.mashape.unirest.http.Unirest;
import eu.freme.bpt.common.Configuration;
import eu.freme.bpt.io.IO;
import eu.freme.bpt.io.IOIterator;
import eu.freme.bpt.io.IteratorFactory;
import eu.freme.bpt.service.EEntity;
import eu.freme.bpt.service.ETranslate;
import eu.freme.bpt.service.Service;
import eu.freme.bpt.util.Pair;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Copyright (C) 2016 Agroknow, Deutsches Forschungszentrum für Künstliche Intelligenz, iMinds,
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
		final List<String> services = Arrays.asList("e-entity", "e-link", "e-publishing", "e-terminology", "e-translate", "pipelining");
		Pair<String, String[]> serviceAndArgs = extractService(args, services);

		// create options that will be parsed from the args

		/////// General BPT options ///////
		Option helpOption = new Option("h", "help", false, "Prints this message");
		Option inputOption = Option.builder("if").longOpt("input-file").argName("input file")
				.desc("The input file or directory to process. In case of a directory, each file in that directory is processed. " +
						"If not given, standard in is used.").hasArg().build();
		Option outputOption = Option.builder("od").longOpt("output-dir").argName("output dir")
				.desc("The output directory. If not given, output is written to standard out.").hasArg().build();
		Option propertiesOption = Option.builder("prop").longOpt("properties").argName("properties file").desc("The properties file that contains configuration of the tool.").hasArg().build();

		Options options = new Options()
				.addOption(helpOption)
				.addOption(inputOption)
				.addOption(outputOption)
				.addOption(propertiesOption);

		/////// Common service options ///////
		Option informatOption = Option.builder("f").longOpt("informat").argName("FORMAT").desc("The format of the input document(s). Defaults to 'turtle'").hasArg().build();
		Option outformatOption = Option.builder("o").longOpt("outformat").argName("FORMAT").desc("The desired output format of the service. Defaults to 'turtle'").hasArg().build();
		options.addOption(informatOption).addOption(outformatOption);

		/////// Service specific options ///////
		Option sourceLang = Option.builder("s").longOpt("source-lang").hasArg().argName("LANGUAGE").desc("The source language of the input document(s)").build();
		Option targetLang = Option.builder("t").longOpt("target-lang").hasArg().argName("LANGUAGE").desc("The target language of the output document(s)").build();
		Option domain = Option.builder("d").longOpt("domain").hasArg().argName("DOMAIN").desc("The domain of the translation system.").required(false).build();
		Option key = Option.builder("k").longOpt("key").hasArg().argName("KEY").desc("A private key to access private and not publicly available translation systems. Key can be created by contacting Tilde team. Optional, if omitted then translates with public systems.").required(false).build();
		Option system = Option.builder().longOpt("system").hasArg().argName("SYSTEM").desc("ID of the translation system to be used. Overwrites source-lang, target-lang and domain.").required(false).build();
		options.addOption(sourceLang).addOption(targetLang).addOption(domain).addOption(key).addOption(system);
                //e-entity
                Option language = Option.builder("l").longOpt("language").hasArg().argName("LANGUAGE").desc("The source language of the input document(s)").required(false).build();
                Option dataset = Option.builder().longOpt("dataset").hasArg().argName("DATASET").desc("The dataset used for entity linking which includes a list of entites and associated labels.").required(false).build();
                Option mode = Option.builder().longOpt("mode").hasArg().argName("MODE").desc("This parameter allows to produce only partly results of named entity recognition.").required(false).build();
                options.addOption(language).addOption(dataset).addOption(mode);	
                // TODO

		String service = serviceAndArgs.getName();
		// TODO: map service on service endpoint using the properties

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
			formatter.printHelp("java -jar <this jar file> <e-service> ", options, true);
			System.exit(exitValue);
		}

		logger.debug("Commandline successfully parsed!");

		// Iterate over the input source(s)
		// TODO: services can be put on an ExecutorService (thread pool)
		IOIterator ioIterator;
		try {
			Configuration configuration = Configuration.create(commandLine);
			ioIterator = IteratorFactory.create(configuration);
			while (ioIterator.hasNext()) {
				IO io = ioIterator.next();
				Service eService = null;
				switch (service) {
					case "e-translate":
						eService = new ETranslate(io.getInputStream(), io.getOutputStream(), configuration);
						break;
                                        case "e-entity":
						eService = new EEntity(io.getInputStream(), io.getOutputStream(), configuration);
						break;
					default:
						logger.warn("Unknown service {}. Skipping!", service);
						break;
				}
				if (eService != null) {
					eService.run();
				}
			}
			System.out.println();
		} catch (Exception e) {
			logger.error("Cannot handle input or output. Reason: ", e);
			System.exit(2);
		}

		try {
			Unirest.shutdown();
		} catch (IOException e) {
			e.printStackTrace();
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
