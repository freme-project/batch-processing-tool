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
 * Represents the e-Entity service
 *
 */
public class EEntity extends AbstractService {

	/**
	 * Creates an EEntity service object, that can be used to address the e-entity service.
	 * @param endpoint		The address of the endpoint (url) to send the request to.
	 * @param ioIterator	Iterator over input / output streams.
	 * @param inFormat		OPTIONAL. The format of the input. The default is {@code turtle}.
	 * @param outFormat		OPTIONAL. The format of the output. The default is {@code turtle}.
	 * @param language      OPTIONAL. The language of the input. The default is {@code en}.
	 * @param dataset		OPTIONAL. The dataset used for entity linking. The default is {@code dbpedia}.
	 * @param mode			OPTIONAL. Allows to produce only partly results of named entity recognition. the default is {@code all}
	 */
	public EEntity(final String endpoint,
				   final IOIterator ioIterator,
				   final Format inFormat,
				   final Format outFormat,
				   final String language,
				   final String dataset,
				   final String mode) {
		super(endpoint, ioIterator, inFormat, outFormat);
		parameters.put("language", language != null ? language : "en");
		parameters.put("mode", mode != null ? mode : "all");
		parameters.put("dataset", dataset != null ? dataset : "dbpedia");

	}

	public static void addOptions(Options options) {
        Option language = Option.builder("l").longOpt("language").hasArg().argName("LANGUAGE").desc("The source language of the input document(s)").required(false).build();
        Option dataset = Option.builder().longOpt("dataset").hasArg().argName("DATASET").desc("The dataset used for entity linking which includes a list of entites and associated labels.").required(false).build();
        Option mode = Option.builder().longOpt("mode").hasArg().argName("MODE").desc("This parameter allows to produce only partly results of named entity recognition.").required(false).build();
        options.addOption(language).addOption(dataset).addOption(mode);
    }
}
