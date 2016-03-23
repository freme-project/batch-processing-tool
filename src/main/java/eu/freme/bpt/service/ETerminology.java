package eu.freme.bpt.service;

import eu.freme.bpt.common.Format;
import eu.freme.bpt.io.IOIterator;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

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
 *
 * Represents the e-Terminology service
 *
 */
public class ETerminology extends AbstractService {

    /*public ETerminology(InputStream inputStream, OutputStream outputStream, Configuration configuration) {
        super("e-terminology", inputStream, outputStream, configuration);
        parameters.put("source-lang", configuration.getSourceLang());
        parameters.put("target-lang", configuration.getTargetLang());
        parameters.put("mode", configuration.getMode());
        
        if (configuration.getCollection() != null) {
            parameters.put("collection", configuration.getCollection());
        }
        
        if (configuration.getKey() != null) {
            parameters.put("key", configuration.getKey());
        }
        
        if (configuration.getDomain() != null) {
            parameters.put("domain", configuration.getDomain());
        }
    } */

	/**
	 * Creates an ETerminology service object, that can be used to address the e-terminology service.
	 * @param endpoint		The address of the endpoint (url) to send the request to.
	 * @param ioIterator	Iterator over input / output streams.
	 * @param inFormat		OPTIONAL. The format of the input. The default is {@code turtle}.
	 * @param outFormat		OPTIONAL. The format of the output. The default is {@code turtle}.
	 * @param sourceLang    OPTIONAL. The language of the input. The default is {@code en}
	 * @param targetLang	OPTIONAL. The language of the output. The default is {@code en}
	 * @param collection    Collection ID from https://term.tilde.com portal.
	 * @param domain        OPTIONAL. Filters out by domain proposed terms
	 * @param key           OPTIONAL. A private key to access private and not publicly available translation systems.
	 * @param mode          OPTIONAL. Whether the result must contain full terminology information or only term annotations with references to the full information. TODO: what is default?
	 */
    public ETerminology(final String endpoint,
						final IOIterator ioIterator,
						final Format inFormat,
						final Format outFormat,
						final String sourceLang,
						final String targetLang,
						final String collection,
						final String domain,
						final String key,
						final String mode) {
        super(endpoint, ioIterator, inFormat, outFormat);
		parameters.put("source-lang", sourceLang != null ? sourceLang : "en");
		parameters.put("target-lang", targetLang != null ? targetLang : "en");
		if (collection != null)	parameters.put("collection", collection);
		if (key != null) parameters.put("key", key);
		if (domain != null)	parameters.put("domain", domain);
		if (mode != null) parameters.put("mode", mode);
    }

    public static void addOptions(Options options) {
        Option sourceLang = Option.builder("s").longOpt("source-lang").hasArg().argName("LANGUAGE").desc("The source language of the input document(s)").build();
        Option targetLang = Option.builder("t").longOpt("target-lang").hasArg().argName("LANGUAGE").desc("The target language of the output document(s)").build();
        Option collection = Option.builder("c").longOpt("collection").hasArg().argName("ID").desc("Collection ID from https://term.tilde.com portal").build();
        Option domain = Option.builder("d").longOpt("domain").hasArg().argName("DOMAIN").desc("It filters out by domain proposed terms").build();
        Option key = Option.builder("k").longOpt("key").hasArg().argName("KEY").desc("A private key to access private and not publicly available translation systems.").build();
        Option mode = Option.builder().longOpt("mode").hasArg().argName("MODE").desc("Whether the result must contain full terminology information or only term annotations with references to the full information").build();
        options.addOption(collection).addOption(key).addOption(mode).addOption(domain).addOption(sourceLang).addOption(targetLang);
    }
}
