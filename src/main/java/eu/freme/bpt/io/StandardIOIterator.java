package eu.freme.bpt.io;

import eu.freme.bpt.common.Format;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

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
 * Does not really iterate, just returns the standard InputStream. Always allows one iteration.
 *
 */
public class StandardIOIterator implements IOIterator {
	private boolean hasNext = true;
	private final OutputStream outputStream;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final File outputFile;

	public StandardIOIterator() {
		outputStream = System.out;
		outputFile = null;
	}

	public StandardIOIterator(final File outputDir, final Format outFormat) throws FileNotFoundException {
		outputFile = new File(outputDir, "bpt_output." + outFormat.getFileExtension());
		outputStream = new BufferedOutputStream(new FileOutputStream(outputFile));
		logger.debug("Input: std in, output file: {}", outputFile);
	}

	@Override
	public boolean hasNext() {
		return hasNext;
	}

	@Override
	public IO next() {
		hasNext = false;
		return new IO(System.in, outputStream, null, outputFile);
	}
}
