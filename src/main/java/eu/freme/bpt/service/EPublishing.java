package eu.freme.bpt.service;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import eu.freme.bpt.Callback;
import eu.freme.bpt.util.FailurePolicy;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
 * Represents the e-Publishing service
 *
 */
public class EPublishing implements Service {
	private final File inputDirectory;
	private final File outputDirectory;
	private final String endpoint;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	protected EPublishing(String endpoint, final File inputDirectory, final File outputDirectory) {
		this.inputDirectory = inputDirectory;
		this.outputDirectory = outputDirectory;
		this.endpoint = endpoint;
		// TODO: checks on input dir? Create output dir?
		// TODO accept / content-type headers?
	}

	@Override
	public void run(FailurePolicy failurePolicy, int nrThreads, Callback callback) {
		logger.info("Running service EPublishing");
		ExecutorService executorService = Executors.newFixedThreadPool(nrThreads);
		Unirest.setTimeouts(30000, 300000);	// TODO: configurable?

		// iterate over zip files
		File[] zipFiles = inputDirectory.listFiles((dir, name) -> {
			return name.endsWith(".zip");
		});

		for (final File zipFile : zipFiles) {
			executorService.submit(() -> {
				File jsonFile = new File(zipFile.getParentFile(), zipFile.getName().replace(".zip", ".json"));
				if (jsonFile.exists()) {
					File outputFile = new File(outputDirectory, zipFile.getName().replace(".zip", ".epub"));
					try {
						String json = new String(Files.readAllBytes(jsonFile.toPath()), StandardCharsets.UTF_8);
						HttpResponse<InputStream> response = Unirest
								.post(endpoint)
								.field("htmlZip", zipFile)
								.field("metadata", json)
								.asBinary();
						if (response.getStatus() == 200) {
							logger.debug("Request alright.");
							try (InputStream responseInput = response.getBody();
								 OutputStream out = new FileOutputStream(outputFile)) {
								IOUtils.copy(responseInput, out);
								callback.onTaskComplete(zipFile, outputFile);
							}
							//Files.write(outputFile.toPath(), IOUtils.toByteArray())
						} else {
							String body = IOUtils.toString(response.getBody());
							String msg = "Error response from service " + endpoint + ": Status " + response.getStatus() + ": " + response.getStatusText() + " - " + body;
							logger.error(msg);
							callback.onTaskFails(zipFile, outputFile, msg);
							if (!failurePolicy.check()) {
								System.exit(3);
							}
						}
					} catch (IOException e) {
						logger.error("Error while reading json file: {}", jsonFile, e);
						callback.onTaskFails(zipFile, outputFile, "Error while reading json file: " + jsonFile + " " + e.getMessage());
						if (!failurePolicy.check()) {
							System.exit(3);
						}
					} catch (UnirestException e) {
						logger.error("Request to {} failed." + endpoint, e);
						callback.onTaskFails(zipFile, outputFile, "Request to " + endpoint + " failed. " + e.getMessage());
						if (!failurePolicy.check()) {
							System.exit(3);
						}
					}


				} else {
					String msg = "Missing metatada file " + jsonFile + " for input file " + zipFile;
					logger.error(msg);
					callback.onTaskFails(zipFile, null, msg);
					if (!failurePolicy.check()) {
						System.exit(3);
					}
				}
			});
		}
	}

	@Override
	public void close() throws IOException {

	}
}
