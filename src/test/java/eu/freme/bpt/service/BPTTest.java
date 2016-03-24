package eu.freme.bpt.service;

import eu.freme.bpt.BPT;
import eu.freme.bpt.common.Format;
import eu.freme.bpt.io.IOCombinationNotPossibleException;
import eu.freme.bpt.util.Pair;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.assertTrue;

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
 * Tests on the BPT class.
 *
 */
public class BPTTest {

	@Test
	public void testEntityInputDir() throws IOException, IOCombinationNotPossibleException {
		Pair<Path, Path> outDirAndFile = copyToTemp("/scripts/input.txt");
		Path inputDir = outDirAndFile.getName();

		new BPT()
				.setInput(inputDir.toAbsolutePath().toString())
				.setOutput(inputDir.toAbsolutePath().toString())
				.setInFormat(Format.text)
				.eEntity("en", "dbpedia", null);

		// check if output file exists and print to std out
		Path outFile = outDirAndFile.getValue().resolveSibling("input.ttl");
		assertTrue(Files.exists(outFile));
		Files.copy(outFile, System.out);
	}

	private Pair<Path, Path> copyToTemp(final String classPathLocation) throws IOException {
		Path tempDir = Files.createTempDirectory("bpttest");
		Path tempFile = tempDir.resolve(classPathLocation.substring(classPathLocation.lastIndexOf('/') + 1));
		Files.copy(this.getClass().getResourceAsStream(classPathLocation), tempFile);
		return new Pair<>(tempDir, tempFile);
	}
}
