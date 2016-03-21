package eu.freme.bpt.service;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
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
import java.util.*;
import java.util.concurrent.*;

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

	public void run(final FailurePolicy failurePolicy, int nrThreads) {
		ExecutorService executorService = Executors.newFixedThreadPool(nrThreads);
		Set<Future<Boolean>> tasks = new HashSet<>();
		while (ioIterator.hasNext()) {
			final IO io = ioIterator.next();
			final InputStream inputStream = io.getInputStream();
			final OutputStream outputStream = io.getOutputStream();

			tasks.add(executorService.submit(() -> {
				boolean success = false;
				Unirest.setTimeouts(30000, 300000);
				try {
					byte[] input = IOUtils.toByteArray(inputStream);
					HttpResponse<InputStream> response = Unirest.post(endpoint).headers(headers).queryString(parameters).body(input).asBinary();
					if (response.getStatus() == 200) {
						logger.debug("Request alright.");
						InputStream responseInput = response.getBody();
						try {
							IOUtils.copy(responseInput, outputStream);
							success = true;
						} catch (IOException e) {
							logger.error("Error while writing response.", e);
						} finally {
							try {
								responseInput.close();
							} catch (IOException e) {
								// not important :)
							}
						}
					} else {
						String body = IOUtils.toString(response.getBody());
						logger.error("Error response from service {}: Status {}: {} - {}", endpoint, response.getStatus(), response.getStatusText(), body);
					}
				} catch (Exception e) {
					logger.error("Request to {} failed." + endpoint, e);
				} finally {
					try {
						inputStream.close();
						outputStream.close();
					} catch (IOException e) {
						// not important
					}
				}
				return success;
			}));

			executorService.shutdown();
			while (!executorService.isTerminated()) {
				Iterator<Future<Boolean>> resultIter = tasks.iterator();
				Future<Boolean> result = resultIter.next();
				if (result.isDone()) {
					boolean success;
					try {
						success = result.get();
					} catch (CancellationException | ExecutionException | InterruptedException e) {
						success = false;
					}
					if (!success) {
						logger.warn("A task failed!");
						if (!failurePolicy.check()) {
							System.exit(3);
						}
					} else {
						logger.info("Success!");
					}
					resultIter.remove();
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// not important...
				}
			}
			try {
				executorService.awaitTermination(1, TimeUnit.DAYS);
			} catch (InterruptedException e) {
				logger.warn("Waiting on termination interrupted.");
			}
		}
	}
}
