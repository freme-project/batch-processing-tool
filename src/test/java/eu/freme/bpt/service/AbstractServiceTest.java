package eu.freme.bpt.service;

import eu.freme.bpt.common.Format;
import eu.freme.bpt.config.Configuration;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

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
 * Tests on the AbstractService class.
 *
 */
public class AbstractServiceTest {

	@Test
	public void testETranslate() throws IOException {
		String input = "This is an English text";
		InputStream inputStream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		Configuration configuration = new Configuration(null, null, Format.text, Format.turtle, "en", "de", null, null, null, null, null, null, null, null, null);

		ETranslate eTranslate = new ETranslate (
				inputStream,
				outputStream,
				configuration
		);

		Boolean success = eTranslate.call();
		assertTrue(success);
		System.out.println("translation: " + outputStream.toString(StandardCharsets.UTF_8.name()));

	}

}
