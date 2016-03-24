package eu.freme.bpt;

import eu.freme.bpt.common.Format;
import eu.freme.bpt.config.BPTProperties;
import eu.freme.bpt.io.IOCombinationNotPossibleException;
import eu.freme.bpt.io.IOIterator;
import eu.freme.bpt.io.IteratorFactory;
import eu.freme.bpt.service.EEntity;
import eu.freme.bpt.service.Service;
import eu.freme.bpt.util.FailurePolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

import static eu.freme.bpt.service.EService.E_ENTITY;

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
 * This object serves as the API.
 *
 */
public class BPT {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private BPTProperties properties = new BPTProperties();
	private File input = null;
	private File output = null;
	private InputStream inputStream = null;
	private OutputStream outputStream = null;
	private Format inFormat = Format.turtle;
	private Format outFormat = Format.turtle;

	/**
	 * Load properties from a given file.
	 * @param propertiesFile	The file to read the properties from.
	 * @return					A BPT object with the properties set.
	 * @throws IOException		The properties file is not found, or something goes wrong reading the file.
	 */
	public BPT loadProperties(final String propertiesFile) throws IOException {
		properties.load(propertiesFile);
		logger.debug("Properties loaded. {}", properties.toString());
		return this;
	}

	// TODO: asynchonous mode
	//public BPT registerCallback() {	}

	// TODO: user auth

	/**
	 * Set a file or directory as input.
	 * @param fileOrDirectory	The path to a file or directory.
	 * @return					A BPT object with the input path set.
	 * @throws FileNotFoundException	The input file or directory is not found.
	 * @throws IOException		The input file or directory cannot be read.
	 */
	public BPT setInput(final String fileOrDirectory) throws IOException {
		input = new File(fileOrDirectory);
		if (!input.exists()) {
			throw new FileNotFoundException("File or directory " + fileOrDirectory + " not found!");
		}
		if (!input.canRead()) {
			throw new IOException("File or directory " + fileOrDirectory + " cannot be read!");
		}
		logger.debug("Input file or directory {} set.", input);
		return this;
	}

	/**
	 * Set an InputStream as input
	 * @param inputStream	The stream to process
	 * @return				A BPT object with the InputStream set.
	 */
	public BPT setInput(final InputStream inputStream) {
		this.inputStream = inputStream;
		logger.debug("Input stream set.");
		return this;
	}


	/**
	 * Set a directory to write output in.
	 * @param outputDirectory	The directory to write the output to.
	 * @return 					A BPT object with the output directory set.
	 */
	public BPT setOutput(final String outputDirectory) throws FileNotFoundException {
		output = new File(outputDirectory);
		logger.debug("Output directory {} set.", outputDirectory);
		// no need to perform checks; this happens in IteratorFactory.
		return this;
	}

	/**
	 * Set an OutputStream to write the results to.
	 * @param outputStream		The OutputStream to write results to.
	 * @return					A BPT object withe the OutputStream set.
	 */
	public BPT setOutput(final OutputStream outputStream) {
		this.outputStream = outputStream;
		logger.debug("Output stream set.");
		return this;
	}

	/**
	 * Set the format of the input.
	 * @param inFormat	The format of the input.
	 * @return			A BPT object with the input format set.
	 */
	public BPT setInFormat(final Format inFormat) {
		this.inFormat = inFormat;
		logger.debug("Informat set to {}.", inFormat);
		return this;
	}

	/**
	 * Set the format of the output
	 * @param outFormat	The format of the output
	 * @return		A BPT object with the output format set.
	 */
	public BPT setOutFormat(final Format outFormat) {
		this.outFormat = outFormat;
		logger.debug("Outformat set to {}.", outFormat);
		return this;
	}

	/**
	 * Invoke the eEntity service
	 * @param language      OPTIONAL. The language of the input. The default is {@code en}.
	 * @param dataset		OPTIONAL. The dataset used for entity linking. The default is {@code dbpedia}.
	 * @param mode			OPTIONAL. Allows to produce only partly results of named entity recognition. the default is {@code all}
	 * @throws IOException
	 * @throws IOCombinationNotPossibleException
	 */
	public void eEntity(final String language, final String dataset, final String mode) throws IOException, IOCombinationNotPossibleException {
		Service service = new EEntity(properties.getUriOf(E_ENTITY),
				ioIterator(),
				inFormat,
				outFormat,
				language,
				dataset,
				mode);
		run(service);
	}

	private void run(final Service service) {
		service.run(failurePolicy(), properties.getThreads());
	}

	private IOIterator ioIterator() throws IOException, IOCombinationNotPossibleException {
		if (inputStream != null && outputStream != null) {
			logger.debug("Using given input and output stream.");
			return IteratorFactory.create(inputStream, outputStream);
		} else {
			logger.debug("Trying to determine input and output from given file(s)");
			return IteratorFactory.create(outFormat, output, input);
		}
	}

	private FailurePolicy failurePolicy() {
		return FailurePolicy.create(properties.getFailureStrategy(), output);
	}

}
