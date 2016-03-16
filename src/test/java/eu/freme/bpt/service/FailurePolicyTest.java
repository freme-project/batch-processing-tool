package eu.freme.bpt.service;

import eu.freme.bpt.util.FailurePolicy;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

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
 * Tests on the FailurePolicy class.
 *
 */
public class FailurePolicyTest {

	@Test
	public void testStdOutBestEffort() {
		FailurePolicy policy = FailurePolicy.create("best-effort", null);
		assertEquals(FailurePolicy.Strategy.BEST_EFFORT, policy.getStrategy());
		assertTrue(policy.check());
	}

	@Test
	public void testStdOutAbort() {
		FailurePolicy policy = FailurePolicy.create("abort", null);
		assertEquals(FailurePolicy.Strategy.ABORT, policy.getStrategy());
		assertFalse(policy.check());
	}

	@Test
	public void testStdOutRevert() {
		FailurePolicy policy = FailurePolicy.create("revert", null);
		assertEquals(FailurePolicy.Strategy.ABORT, policy.getStrategy());
		assertFalse(policy.check());
	}

	@Test
	public void testDirBestEffort() throws IOException {
		File dir = createDir();
		FailurePolicy policy = FailurePolicy.create("best-effort", dir);
		assertEquals(FailurePolicy.Strategy.BEST_EFFORT, policy.getStrategy());
		assertTrue(policy.check());
		assertTrue(dir.exists());
		FileUtils.deleteDirectory(dir);
	}

	@Test
	public void testDirBestAbort() throws IOException {
		File dir = createDir();
		FailurePolicy policy = FailurePolicy.create("abort", dir);
		assertEquals(FailurePolicy.Strategy.ABORT, policy.getStrategy());
		assertFalse(policy.check());
		assertTrue(dir.exists());
		FileUtils.deleteDirectory(dir);
	}

	@Test
	public void testDirRevert() throws IOException {
		File dir = createDir();
		FailurePolicy policy = FailurePolicy.create("revert", dir);
		assertEquals(FailurePolicy.Strategy.REVERT, policy.getStrategy());
		assertFalse(policy.check());
		assertFalse(dir.exists());
		FileUtils.deleteDirectory(dir);
	}

	private File createDir() throws IOException {
		File dir = Files.createTempDirectory("testDir").toFile();
		try {
			FileUtils.forceDeleteOnExit(dir);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// now create some files in it
		File file1 = new File(dir, "file1");
		File file2 = new File(dir, "file2");
		Files.write(file1.toPath(), "Hello".getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
		Files.write(file2.toPath(), "World".getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
		return dir;
	}
}
