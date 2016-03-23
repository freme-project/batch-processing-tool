package eu.freme.bpt.util;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

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
 * Determines what to do in case of a (service) failure.
 *
 */
public class FailurePolicy {
	private static Logger logger = LoggerFactory.getLogger(FailurePolicy.class);
	private final Strategy strategy;
	private final File outputDir;


	public enum Strategy {
		ABORT,
		BEST_EFFORT,
		REVERT
	}

	/**
	 * Create a FailurePolicy object.
	 * @param policy	The desired policy behaviour: "abort" (just stop), "best-effort" (continue) or "revert" (stop and clean up).
	 * @param outputDir	The output dir to clean up in case of "revert". If outputDir is {@code null} (in case of std out), "revert" becomes "abort".
	 * @return			The created object.
	 */
	public static FailurePolicy create(final Strategy policy, final File outputDir) {
		Strategy pol;
		switch (policy) {
			case ABORT:
				pol = policy;
				break;
			case REVERT:
				if (outputDir == null) {
					logger.info("Given strategy is 'revert', but the output is standard out. Changing to 'abort'.");
					pol = Strategy.ABORT;
				} else {
					pol = policy;
				}
				break;
			case BEST_EFFORT:
			default:
				pol = policy;
				break;
		}
		return new FailurePolicy(pol, outputDir);
	}

	/**
	 * Create a Failure policy object directly.
	 * @param strategy	The desired policy behaviour.
	 * @param outputDir	The output directory to clean up in case the policy is 'revert'. In other cases, this does nothing.
	 */
	private FailurePolicy(Strategy strategy, final File outputDir) {
		this.strategy = strategy;
		this.outputDir = outputDir;
	}

	public Strategy getStrategy() {
		return strategy;
	}

	/**
	 * Checks whether the processing should continue, depending on the strategy. If the strategy is "revert" and the
	 * output directory is not {@code null}, the output directory is deleted recurively!
	 * @return	{@code true} if the process should continue, {@code false} if not.
	 */
	public boolean check() {
		switch (strategy) {
			case ABORT:
				logger.debug("Strategy is 'abort', so stop!");
				return false;
			case REVERT:
				logger.debug("Strategy is 'revert', so stop and clean up output dir!");
				try {
					FileUtils.deleteDirectory(outputDir);
				} catch (IOException e) {
					logger.warn("Could not delete output dir {}: ", outputDir, e);
				}
				return false;
			case BEST_EFFORT:
			default:
				logger.debug("Strategy is 'best-effort', so continue!");
				return true;
		}
	}

}
