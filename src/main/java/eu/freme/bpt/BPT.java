package eu.freme.bpt;

import eu.freme.bpt.common.Format;
import eu.freme.bpt.config.BPTProperties;
import eu.freme.bpt.io.IOCombinationNotPossibleException;
import eu.freme.bpt.io.IOIterator;
import eu.freme.bpt.io.IteratorFactory;
import eu.freme.bpt.service.*;
import eu.freme.bpt.util.FailurePolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

import static eu.freme.bpt.service.EService.*;

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
	private Callback callback = new DummyCallback();

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

	public BPT registerCallback(final Callback callback) {
		this.callback = callback;
		return this;
	}

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
	 * Set the format of the output.
	 * @param outFormat	The format of the output
	 * @return		A BPT object with the output format set.
	 */
	public BPT setOutFormat(final Format outFormat) {
		this.outFormat = outFormat;
		logger.debug("Outformat set to {}.", outFormat);
		return this;
	}

	/**
	 * Invoke the e-Entity service
	 * @param language      OPTIONAL. The language of the input. The default is {@code en}.
	 * @param dataset		OPTIONAL. The dataset used for entity linking. The default is {@code dbpedia}.
	 * @param mode			OPTIONAL. Allows to produce only partly results of named entity recognition. the default is {@code all}
	 * @throws IOException  Something went wrong processing input or output.
	 * @throws IOCombinationNotPossibleException	The combination input and output is not valid. E.g.: If the input is a directory, the output should also be a directory, not a stream.
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

	/**
	 * Invoke the e-Link service.
	 * @param templateId	The ID of the template to be used.
	 * @throws IOException	Something went wrong processing input or output.
	 * @throws IOCombinationNotPossibleException	The combination input and output is not valid. E.g.: If the input is a directory, the output should also be a directory, not a stream.
	 */
	public void eLink(final String templateId) throws IOException, IOCombinationNotPossibleException {
		Service service = new ELink(properties.getUriOf(E_LINK),
				ioIterator(),
				inFormat,
				outFormat,
				templateId
				);
		run(service);
	}

	/**
	 * Invoke the Pipelining service
	 * @param templateId	The ID of the template to be used.
	 * @throws IOException  Something went wrong processing input or output.
	 * @throws IOCombinationNotPossibleException	The combination input and output is not valid. E.g.: If the input is a directory, the output should also be a directory, not a stream.
	 */
	public void pipelining(final String templateId) throws IOException, IOCombinationNotPossibleException {
		Service service = new Pipelining(properties.getUriOf(PIPELINING),
				ioIterator(),
				inFormat,
				outFormat,
				templateId
				);
		run(service);
	}

	/**
	 * Invoke the e-Terminology service.
	 * @param sourceLang    OPTIONAL. The language of the input. The default is {@code en}
	 * @param targetLang	OPTIONAL. The language of the output. The default is {@code en}
	 * @param collection    Collection ID from https://term.tilde.com portal.
	 * @param domain        OPTIONAL. Filters out by domain proposed terms
	 * @param key           OPTIONAL. A private key to access private and not publicly available translation systems.
	 * @param mode          OPTIONAL. Whether the result must contain full terminology information or only term annotations with references to the full information.
	 * @throws IOException  Something went wrong processing input or output.
	 * @throws IOCombinationNotPossibleException	The combination input and output is not valid. E.g.: If the input is a directory, the output should also be a directory, not a stream.
	 */
	public void eTerminology(final String sourceLang, final String targetLang, final String collection, final String domain, final String key, final String mode) throws IOException, IOCombinationNotPossibleException {
		Service service = new ETerminology(properties.getUriOf(E_TERMINOLOGY),
				ioIterator(),
				inFormat,
				outFormat,
				sourceLang,
				targetLang,
				collection,
				domain,
				key,
				mode
				);
		run(service);
	}

	/**
	 * Invoke the e-Translation service.
	 * @param sourceLang    OPTIONAL. The language of the input. The default is {@code en}
	 * @param targetLang	OPTIONAL. The language of the output. The default is {@code en}
	 * @param system    	OPTIONAL. ID of the translation system to be used. Overwrites source-lang, target-lang and domain.
	 * @param domain        OPTIONAL. Filters out by domain proposed terms
	 * @param key           OPTIONAL. A private key to access private and not publicly available translation systems.
	 * @throws IOException	Something went wrong processing input or output.
	 * @throws IOCombinationNotPossibleException	The combination input and output is not valid. E.g.: If the input is a directory, the output should also be a directory, not a stream.
	 */
	public void eTranslation(final String sourceLang, final String targetLang, final String system, final String domain, final String key) throws IOException, IOCombinationNotPossibleException {
		Service service = new ETranslation(properties.getUriOf(E_TRANSLATION),
				ioIterator(),
				inFormat,
				outFormat,
				sourceLang,
				targetLang,
				system,
				domain,
				key);
		run(service);
	}

	// TODO: e-Publishing, Pipelining

	private void run(final Service service) {
		if (callback instanceof DummyCallback) {
			service.run(failurePolicy(), properties.getThreads(), callback);
			try {
				service.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			Thread serviceThread = new Thread(() -> {
				service.run(failurePolicy(), properties.getThreads(), callback);
				try {
					service.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}, "serviceThread");
			serviceThread.start();
		}
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
