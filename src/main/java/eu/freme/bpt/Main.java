package eu.freme.bpt;

import eu.freme.bpt.config.Configuration;
import eu.freme.bpt.io.IOIterator;
import eu.freme.bpt.io.IteratorFactory;
import eu.freme.bpt.service.*;
import eu.freme.bpt.util.FailurePolicy;
import eu.freme.bpt.util.Pair;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Copyright (C) 2016 Agroknow, Deutsches Forschungszentrum für Künstliche
 * Intelligenz, iMinds, Institut für Angewandte Informatik e. V. an der
 * Universität Leipzig, Istituto Superiore Mario Boella, Tilde, Vistatec, WRIPL
 * (http://freme-project.eu)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        final List<String> services = Arrays.asList("e-entity", "e-link", "e-publishing", "e-terminology", "e-translate", "pipelining");
        Pair<String, String[]> serviceAndArgs = extractService(args, services);

        String service = serviceAndArgs.getName();

        // create options that will be parsed from the args
        /////// General BPT options ///////
        Option helpOption = new Option("h", "help", false, "Prints this message");
        Option inputOption = Option.builder("if").longOpt("input-file").argName("input file")
                .desc("The input file or directory to process. In case of a directory, each file in that directory is processed. "
                        + "If not given, standard in is used.").hasArg().build();
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

        Class serviceClass = null;
        /////// Service specific options ///////
        if (service != null) {
            switch (service) {
                case "e-translate":
                    serviceClass = ETranslate.class;
                    ETranslate.addOptions(options);
                    break;
                case "e-entity":
                    serviceClass = EEntity.class;
                    EEntity.addOptions(options);
                    break;
                case "e-link":
                    serviceClass = ELink.class;
                    ELink.addOptions(options);
                    break;
                case "e-terminology":
                    serviceClass = ETerminology.class;
                    ETerminology.addOptions(options);
                    break;
                default:
                    logger.warn("Unknown service {}. Skipping!", service);
                    break;
            }
        } else {
            ETranslate.addOptions(options);
            EEntity.addOptions(options);
            ELink.addOptions(options);
            ETerminology.addOptions(options);
        }

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
        IOIterator ioIterator;
        try {
            Configuration configuration = Configuration.create(commandLine, serviceClass);

			String failureStrategy = configuration.getFailureStrategy();
			File outputDir = commandLine.hasOption("od") ? new File(commandLine.getOptionValue("od")) : null;
			FailurePolicy failurePolicy = FailurePolicy.create(failureStrategy, outputDir);
			ioIterator = IteratorFactory.create(configuration.getOutFormat(), configuration.getOutputDir(), configuration.getInputFile());

			Service eService;
			switch (service) {
				case "e-translate":
					eService = new ETranslate(
							configuration.getEndpoint("e-translate"),
							ioIterator,
							configuration.getInFormat(),
							configuration.getOutFormat(),
							configuration.getSourceLang(),
							configuration.getTargetLang(),
							configuration.getSystem(),
							configuration.getDomain(),
							configuration.getKey()
					);
					break;
				case "e-entity":
					eService = new EEntity(
							configuration.getEndpoint("e-entity"),
							ioIterator,
							configuration.getInFormat(),
							configuration.getOutFormat(),
							configuration.getLanguage(),
							configuration.getDataset(),
							configuration.getMode()
					);
					break;
				case "e-link":
					eService = new ELink(
							configuration.getEndpoint("e-entity"),
							ioIterator,
							configuration.getInFormat(),
							configuration.getOutFormat(),
							configuration.getTemplateID()
					);
					break;
				case "e-terminology":
					eService = new ETerminology(
							configuration.getEndpoint("e-translate"),
							ioIterator,
							configuration.getInFormat(),
							configuration.getOutFormat(),
							configuration.getSourceLang(),
							configuration.getTargetLang(),
							configuration.getCollection(),
							configuration.getDomain(),
							configuration.getKey(),
							configuration.getMode()
					);
					break;
				default:
					logger.error("Unknown service {}. Aborting!", service);
					System.exit(3);
					return;

			}

			eService.run(failurePolicy, configuration.getThreads());
        } catch (Exception e) {
            logger.error("Cannot handle input or output. Reason: ", e);
            System.exit(2);
        }
    }

    /**
     * Finds and removes the service from the arguments array. This helps
     * processing the rest of the parameters using the Apache Commons CLI
     * library (and thus needs to be called before processing the arguments).
     *
     * @param args The arguments of the program.
     * @param services The list of registered services.
     * @return	The service name if found, or {@code null} if not found.
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
