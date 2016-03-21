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
 * Represents the e-translate service
 *
 */
public class ETranslate extends AbstractService {

	/**
	 * Creates an ETranslate service object, that can be used to address the e-translate service.
	 * @param endpoint		The address of the endpoint (url) to send the request to.
	 * @param ioIterator	Iterator over input / output streams.
	 * @param inFormat		OPTIONAL. The format of the input. The default is {@code turtle}.
	 * @param outFormat		OPTIONAL. The format of the output. The default is {@code turtle}.
	 * @param sourceLang    OPTIONAL. The language of the input. The default is {@code en}
	 * @param targetLang	OPTIONAL. The language of the output. The default is {@code en}
	 * @param system    	OPTIONAL. ID of the translation system to be used. Overwrites source-lang, target-lang and domain.
	 * @param domain        OPTIONAL. Filters out by domain proposed terms
	 * @param key           OPTIONAL. A private key to access private and not publicly available translation systems.
	 */
	public ETranslate(final String endpoint,
					  final IOIterator ioIterator,
					  final Format inFormat,
					  final Format outFormat,
					  final String sourceLang,
					  final String targetLang,
					  final String system,
					  final String domain,
					  final String key) {
		super(endpoint, ioIterator, inFormat, outFormat);
		parameters.put("source-lang", sourceLang != null ? sourceLang : "en");
		parameters.put("target-lang", targetLang != null ? targetLang : "en");
		if (system != null) parameters.put("system", system);
		if (key != null) parameters.put("key", key);
		if (domain != null) parameters.put("domain", domain);
	}

	public static void addOptions(Options options) {
        Option sourceLang = Option.builder("s").longOpt("source-lang").hasArg().argName("LANGUAGE").desc("The source language of the input document(s)").build();
        Option targetLang = Option.builder("t").longOpt("target-lang").hasArg().argName("LANGUAGE").desc("The target language of the output document(s)").build();
        Option domain = Option.builder("d").longOpt("domain").hasArg().argName("DOMAIN").desc("The domain of the translation system.").build();
        Option key = Option.builder("k").longOpt("key").hasArg().argName("KEY").desc("A private key to access private and not publicly available translation systems. Key can be created by contacting Tilde team. Optional, if omitted then translates with public systems.").build();
        Option system = Option.builder().longOpt("system").hasArg().argName("SYSTEM").desc("ID of the translation system to be used. Overwrites source-lang, target-lang and domain.").build();
        options.addOption(sourceLang).addOption(targetLang).addOption(domain).addOption(key).addOption(system);
    }
    
    public static String getDefaultValue(String option) {
        switch (option) {
            case "source-lang":
                return "en";
            case "target-lang":
                return "de";
            default:
                return null;
        }
    }
}
