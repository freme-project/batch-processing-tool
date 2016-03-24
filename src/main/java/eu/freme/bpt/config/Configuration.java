package eu.freme.bpt.config;

import eu.freme.bpt.common.Format;
import org.apache.commons.cli.CommandLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Copyright (C) 2016 Agroknow, Deutsches Forschungszentrum für Künstliche
 * Intelligenz, iMinds, Institut für Angewandte Informatik e. V. an der
 * Universität Leipzig, Istituto Superiore Mario Boella, Tilde, Vistatec, WRIPL
 * (http://freme-project.eu)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * <p>
 * Holds the configuration of the tool. This includes parameter options and
 * properties.
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
    private final String templateID;
    private final String collection;

    private final BPTProperties properties;

	private static Logger logger = LoggerFactory.getLogger(Configuration.class);

	public static Configuration create(CommandLine commandLine) throws IOException {
		File inputFile = commandLine.hasOption("if") ? new File(commandLine.getOptionValue("if")) : null;
		File outputDir = commandLine.hasOption("od") ? new File(commandLine.getOptionValue("od")) : null;
		Format inFormat = commandLine.hasOption('f') ? Format.valueOf(commandLine.getOptionValue('f')) : null;
		Format outFormat = commandLine.hasOption('o') ? Format.valueOf(commandLine.getOptionValue('o')) : null;
		String sourceLang = commandLine.getOptionValue('s');
		String targetLang = commandLine.getOptionValue('t');
		String domain = commandLine.getOptionValue('d');
		String system = commandLine.getOptionValue("system");
		String key = commandLine.getOptionValue('k');
		String language = commandLine.getOptionValue("language");
		String dataset = commandLine.getOptionValue("dataset");
		String mode = commandLine.getOptionValue("mode");
		String templateID = commandLine.getOptionValue("templateid");
		String collection = commandLine.getOptionValue('c');
		String propertiesFile = commandLine.hasOption("prop") ? commandLine.getOptionValue("prop") : null;
		return new Configuration(inputFile, outputDir, inFormat, outFormat, sourceLang, targetLang, domain, key, system, language, dataset, mode, templateID, collection, propertiesFile);
	}

    private Configuration(File inputFile,
						  File outputDir,
						  Format inFormat,
						  Format outFormat,
						  String sourceLang,
						  String targetLang,
						  String domain,
						  String key,
						  String system,
						  String language,
						  String dataset,
						  String mode,
						  String templateID,
						  String collection,
						  String propertiesFile) throws IOException {
        this.inputFile = inputFile;
        this.outputDir = outputDir;
        this.inFormat = inFormat;
        this.outFormat = outFormat;
        this.sourceLang = sourceLang;
        this.targetLang = targetLang;
        this.domain = domain;
        this.key = key;
        this.system = system;
        this.language = language;
        this.dataset = dataset;
        this.mode = mode;
        this.templateID = templateID;
        this.collection = collection;

		properties = new BPTProperties();
		if (propertiesFile != null) {
			try (InputStream propertiesStream = new FileInputStream(propertiesFile)) {
				properties.load(propertiesStream);
			}
		}
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

    public String getTemplateID() {
        return templateID;
    }
    
    public String getCollection() {
        return collection;
    }

	public BPTProperties getProperties() {
		return properties;
	}
}
