package eu.freme.bpt.common;

import org.apache.commons.cli.CommandLine;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

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
 *
 * Holds the configuration of the tool. This includes parameter options and properties.
 *
 */
public class Configuration {
	private final File inputFile;
	private final File outputDir;
	private final Format inFormat;
	private final Format outFormat;
	private final String sourceLang;
	private final String targetLang;
	private final String domain;
	private final String key;
	private final String system;
	private final String language;
	private final String dataset;
	private final String mode;

	private final Map<String, String> serviceToEndpoint;

	public static Configuration create(CommandLine commandLine) throws IOException {
		File inputFile = commandLine.hasOption("if") ? new File(commandLine.getOptionValue("if")) : null;
		File outputDir = commandLine.hasOption("od") ? new File(commandLine.getOptionValue("od")) : null;
		Format inFormat = commandLine.hasOption('f') ? Format.valueOf(commandLine.getOptionValue('f')) : Format.turtle;
		Format outFormat = commandLine.hasOption('o') ? Format.valueOf(commandLine.getOptionValue('o')) : Format.turtle;
		String sourceLang = commandLine.getOptionValue('s', "en");
		String targetLang = commandLine.getOptionValue('t', "de");
                String domain = commandLine.getOptionValue('d', null);
                String system = commandLine.getOptionValue("system", null);
                String key = commandLine.getOptionValue('k', null);                        
                String language = commandLine.getOptionValue("language", "en");                        
                String dataset = commandLine.getOptionValue("dataset", "dbpedia");                        
                String mode = commandLine.getOptionValue("mode", "all");                        

		Properties properties = new Properties();
		try (InputStream propertiesStream = Configuration.class.getResourceAsStream("/bpt.properties")) {
			properties.load(propertiesStream);
		}
		if (commandLine.hasOption("prop")) {
			try (InputStream propertiesStream = new FileInputStream(commandLine.getOptionValue("prop"))) {
				properties.load(propertiesStream);
			}
		}

		Map<String, String> serviceToEndpoint = new HashMap<>();
        serviceToEndpoint.put("e-entity", properties.getProperty("e-entity"));
        serviceToEndpoint.put("e-link", properties.getProperty("e-link"));
        serviceToEndpoint.put("e-publishing", properties.getProperty("e-publishing"));
        serviceToEndpoint.put("e-terminology", properties.getProperty("e-terminology"));
        serviceToEndpoint.put("e-translate", properties.getProperty("e-translate"));
        serviceToEndpoint.put("pipelining", properties.getProperty("pipelining"));

		return new Configuration(inputFile, outputDir, inFormat, outFormat, serviceToEndpoint, sourceLang, targetLang, domain, key, system, language, dataset, mode);
	}

	public Configuration(File inputFile,
						 File outputDir,
						 Format inFormat,
						 Format outFormat,
						 Map<String, String> serviceToEndpoint,
						 String sourceLang,
						 String targetLang,
                                                 String domain,
                                                 String key,
                                                 String system,
                                                 String language,
                                                 String dataset,
                                                 String mode) {
		this.inputFile = inputFile;
		this.outputDir = outputDir;
		this.inFormat = inFormat;
		this.outFormat = outFormat;
		this.serviceToEndpoint = serviceToEndpoint;
		this.sourceLang = sourceLang;
		this.targetLang = targetLang;
                this.domain = domain;
                this.key = key;
                this.system = system;
                this.language = language;
                this.dataset = dataset;
                this.mode = mode;
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

	public String getEndpoint(final String service) {
		return serviceToEndpoint.get(service);
	}

	public String getSourceLang() {
		return sourceLang;
	}

	public String getTargetLang() {
		return targetLang;
	}
        
        public String getDomain() {
		return domain;
	}
        
        public String getKey() {
		return key;
	}
        
        public String getSystem() {
		return system;
	}

    public String getLanguage() {
        return language;
    }

    public String getDataset() {
        return dataset;
    }

    public String getMode() {
        return mode;
    }
}
