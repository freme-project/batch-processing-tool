package eu.freme.bpt.service;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import eu.freme.bpt.Callback;
import eu.freme.bpt.common.Format;
import eu.freme.bpt.io.IO;
import eu.freme.bpt.io.IOIterator;
import eu.freme.bpt.util.FailurePolicy;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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
 * Some common logic for e-services.
 *
 */
public abstract class AbstractService implements Service {
	private static Logger logger = LoggerFactory.getLogger(AbstractService.class);

	private final IOIterator ioIterator;

	private final String endpoint;

	private final Map<String, String> headers;
	protected final Map<String, Object> parameters;

	protected AbstractService(final String endpoint, final IOIterator ioIterator, final Format inFormat, final Format outFormat) {
		this.endpoint = endpoint;
		this.ioIterator = ioIterator;
		headers = new HashMap<>();
		headers.put("accept", outFormat != null ? outFormat.getMimeType() : Format.turtle.getMimeType());
		headers.put("content-type", inFormat != null ? inFormat.getMimeType() : Format.turtle.getMimeType());
		parameters = new HashMap<>(3);
	}

	public void run(final FailurePolicy failurePolicy, final int nrThreads, final Callback callback) {
		logger.info("Running service {}", this.getClass().getName());
		ExecutorService executorService = Executors.newFixedThreadPool(nrThreads);
		Unirest.setTimeouts(30000, 300000);	// TODO: configurable?
		while (ioIterator.hasNext()) {
			final IO io = ioIterator.next();

			executorService.submit(() -> {
				try (final InputStream inputStream = io.getInputStream(); final OutputStream outputStream = io.getOutputStream()) {
					byte[] input = IOUtils.toByteArray(inputStream);
					HttpResponse<InputStream> response = Unirest.post(endpoint).headers(headers).queryString(parameters).body(input).asBinary();
					if (response.getStatus() == 200) {
						logger.debug("Request alright.");
						try (InputStream responseInput = response.getBody()) {
							IOUtils.copy(responseInput, outputStream);
							callback.onTaskComplete(io.getInputFile(), io.getOutputFile());
						} catch (IOException e) {
							logger.error("Error while writing response.", e);
							callback.onTaskFails(io.getInputFile(), io.getOutputFile(), "Error while writing response. " + e.getMessage());
							if (!failurePolicy.check()) {
								System.exit(3);
							}
						}
					} else {
						String body = IOUtils.toString(response.getBody());
						String msg = "Error response from service " + endpoint + ": Status " + response.getStatus() + ": " + response.getStatusText() + " - " + body;
						logger.error(msg);
						callback.onTaskFails(io.getInputFile(), io.getOutputFile(), msg);
						if (!failurePolicy.check()) {
							System.exit(3);
						}
					}
				} catch (Exception e) {
					logger.error("Request to {} failed." + endpoint, e);
					callback.onTaskFails(io.getInputFile(), io.getOutputFile(), "Request to " + endpoint + " failed. " + e.getMessage());
					if (!failurePolicy.check()) {
						System.exit(3);
					}
				}
			});
		}
		executorService.shutdown();
		try {
			executorService.awaitTermination(1, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			logger.warn("Waiting on termination interrupted.");
		}
		callback.onBatchComplete();
	}

	@Override
	public void close() throws IOException {
		Unirest.shutdown();
		logger.debug("Service {} shut down.", getClass().getName());
	}
}
