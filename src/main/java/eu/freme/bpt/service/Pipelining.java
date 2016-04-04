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
 * Represents the Pipelining service
 *
 */
public class Pipelining extends AbstractService {


	/**
	 * Creates a Pipelining service object, that can be used to address the pipelining service.
	 * @param endpoint		The address of the endpoint (url) to send the request to.
	 * @param ioIterator	Iterator over input / output streams.
	 * @param inFormat		OPTIONAL. The format of the input. The default is {@code turtle}.
	 * @param outFormat		OPTIONAL. The format of the output. The default is {@code turtle}.
	 * @param templateId    The ID of the template to be used.
	 */
	public Pipelining(final String endpoint,
					  final IOIterator ioIterator,
					  final Format inFormat,
					  final Format outFormat,
					  final String templateId) {
		super(endpoint, ioIterator, inFormat, outFormat);
		parameters.put("id", templateId);
	}

	public static void addOptions(Options options) {
		Option templateID = Option.builder().longOpt("templateid").hasArg().argName("ID").desc("This parameter sets the ID of the template to be used.").required(true).build();
		options.addOption(templateID);
	}
}
