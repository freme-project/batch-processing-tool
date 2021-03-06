package eu.freme.bpt.service;

import eu.freme.bpt.BPT;
import eu.freme.bpt.Callback;
import eu.freme.bpt.common.Format;
import eu.freme.bpt.io.IOCombinationNotPossibleException;
import eu.freme.bpt.util.Pair;
import org.junit.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.*;

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

	/**
	 * This test demonstrates how you can run the tool on a directory, and write output to a directory.
	 * It uses the e-Entity service.
	 * @throws IOException
	 * @throws IOCombinationNotPossibleException
	 */
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

	/**
	 * This test demonstrates how you can run the tool on a directory, and write output to a directory. It runs in the
	 * background
	 * It uses the e-Entity service.
	 * @throws IOException
	 * @throws IOCombinationNotPossibleException
	 */
	@Test
	public void testEntityInputDirBackground() throws IOException, IOCombinationNotPossibleException {
		Pair<Path, Path> outDirAndFile = copyToTemp("/scripts/input.txt");
		Path inputDir = outDirAndFile.getName();

		new BPT()
				.setInput(inputDir.toAbsolutePath().toString())
				.setOutput(inputDir.toAbsolutePath().toString())
				.setInFormat(Format.text)
				.registerCallback(new Callback() {
					@Override
					public void onTaskComplete(File inputFile, File outputFile) {
						System.out.println("This is the callback calling: task complete!!");
						// check if output file exists and print to std out
						Path outFile = outDirAndFile.getValue().resolveSibling("input.ttl");
						assertTrue(Files.exists(outFile));
						try {
							Files.copy(outFile, System.out);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onTaskFails(File inputFile, File outputFile, String reason) {
						System.out.println("Task fails!!");
						fail("This should not go wrong!");
					}

					@Override
					public void onBatchComplete() {
						System.out.println("Ready!!!");
					}
				})
				.eEntity("en", "dbpedia", null);

		for (int i = 10; i > 0; i--) {
			System.out.println("Waiting for " + i + " more seconds!");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * This test demonstrates how you can run the tool on a given input stream, and let the results write to a given
	 * output stream.
	 * It uses the e-Entity service.
	 * @throws IOException
	 * @throws IOCombinationNotPossibleException
	 */
	@Test
	public void testEntityStreams() throws IOException, IOCombinationNotPossibleException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try (InputStream in = new ByteArrayInputStream("Rachmaninov was a great Russian composer.".getBytes(StandardCharsets.UTF_8))) {
			new BPT()
					.setInput(in)
					.setOutput(bos)
					.setInFormat(Format.text)
					.eEntity(null, null, null);
		}

		String output = bos.toString(StandardCharsets.UTF_8.name());
		assertNotNull(output);
		System.out.println(output);
	}

	/**
	 * Test to see what happens if the input- and output dir are the same, AND the input- and output format are the same.
	 * Output files should be prefixed with 'out_' .
	 * @throws IOException
	 * @throws IOCombinationNotPossibleException
	 */
	@Test
	public void testEntitySameInputAndOutput() throws IOException, IOCombinationNotPossibleException {
		Pair<Path, Path> outDirAndFile = copyToTemp("/scripts/input4.ttl");
		Path inputDir = outDirAndFile.getName();

		new BPT()
				.setInput(inputDir.toAbsolutePath().toString())
				.setOutput(inputDir.toAbsolutePath().toString())
				.eEntity("en", "dbpedia", null);

		// check if output file exists and print to std out
		Path outFile = outDirAndFile.getValue().resolveSibling("out_input4.ttl");
		assertTrue(Files.exists(outFile));
		Files.copy(outFile, System.out);
	}

	@Test
	public void testPipelining() throws IOException, IOCombinationNotPossibleException {
		Pair<Path, Path> outDirAndFile = copyToTemp("/scripts/input2.txt");
		Path inputDir = outDirAndFile.getName();

		new BPT()
				.setInput(inputDir.toAbsolutePath().toString())
				.setOutput(inputDir.toAbsolutePath().toString())
				.setInFormat(Format.text)
				.pipelining("34");

		// check if output file exists and print to std out
		Path outFile = outDirAndFile.getValue().resolveSibling("input2.ttl");
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
