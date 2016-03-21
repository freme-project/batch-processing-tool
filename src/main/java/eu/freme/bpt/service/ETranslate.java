package eu.freme.bpt.service;

import eu.freme.bpt.config.Configuration;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import java.io.InputStream;
import java.io.OutputStream;

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
 * Represents the e-translate service
 *
 */
public class ETranslate extends AbstractService {

    public ETranslate(InputStream inputStream, OutputStream outputStream, Configuration configuration) {
        super("e-translate", inputStream, outputStream, configuration);
        parameters.put("source-lang", configuration.getSourceLang());
        parameters.put("target-lang", configuration.getTargetLang());

        if (configuration.getDomain() != null) {
            parameters.put("domain", configuration.getDomain());
        }

        if (configuration.getKey() != null) {
            parameters.put("key", configuration.getKey());
        }

        if (configuration.getSystem() != null) {
            parameters.put("system", configuration.getSystem());
        }
    }

    public static void addOptions(Options options) {
        Option sourceLang = Option.builder("s").longOpt("source-lang").hasArg().argName("LANGUAGE").desc("The source language of the input document(s)").build();
        Option targetLang = Option.builder("t").longOpt("target-lang").hasArg().argName("LANGUAGE").desc("The target language of the output document(s)").build();
        Option domain = Option.builder("d").longOpt("domain").hasArg().argName("DOMAIN").desc("The domain of the translation system.").required(false).build();
        Option key = Option.builder("k").longOpt("key").hasArg().argName("KEY").desc("A private key to access private and not publicly available translation systems. Key can be created by contacting Tilde team. Optional, if omitted then translates with public systems.").required(false).build();
        Option system = Option.builder().longOpt("system").hasArg().argName("SYSTEM").desc("ID of the translation system to be used. Overwrites source-lang, target-lang and domain.").required(false).build();
        options.addOption(sourceLang).addOption(targetLang).addOption(domain).addOption(key).addOption(system);
    }
    
    public static String getDefaultValue(String option) {
        switch (option) {
            case "source-lang":
                return "en";
            case "target-lang":
                return "de";
            default:
                return null;
        }
    }
}
