package eu.freme.bpt.input;

import java.io.*;

/**
 * Copyright (C) 2016 Agro-Know, Deutsches Forschungszentrum für Künstliche Intelligenz, iMinds,
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
 * Does not really iterate, just returns the InputStream created from the given file. Always allows one iteration.
 *
 */
public class SingleFileInputIterator implements InputIterator {
	private boolean hasNext = true;
	private final InputStream in;

	/**
	 * Creates a SingleFileInputIterator from the given file.
	 * @param inputFile    The file to process.
	 * @throws FileNotFoundException	The given file is not found.
	 */
	public SingleFileInputIterator(final File inputFile) throws FileNotFoundException {
		in = new BufferedInputStream(new FileInputStream(inputFile));
	}

	@Override
	public boolean hasNext() {
		return hasNext;
	}

	@Override
	public InputStream next() {
		hasNext = false;
		return in;
	}
}
