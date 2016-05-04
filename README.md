# batch-processing-tool

## Usage

Below you can find examples of how to use the tool. For a more detailed description, we refer to the [specification](https://docs.google.com/document/d/1XTUGCizw-kEp66ySYvXJ5ZclPNwij49fwaVOKrWFZXw/edit#heading=h.nuaipbadbwzb).

### e-Terminology

Process `input.ttl` (with the format `turtle`) using e-Terminology. The source language is `en` (English) and target language is `de` (German). The output format is also `turtle`.
The command is `java -jar batch-processing-tool-0.1-SNAPSHOT-jar-with-dependencies.jar -f turtle -if ./input.ttl -o turtle -s en -t de e-terminology`.

### e-Translation

Process `input.txt` (with the format `text`) using e-Translation. The source language is `en` (English) and target language is `de` (German). The output format is `turtl`.
The command is `java -jar batch-processing-tool-0.1-SNAPSHOT-jar-with-dependencies.jar -f text -if ./input.txt -o turtle -s en -t de e-translate`.

### e-Link

Process `input.ttl` (with the format `turtle`) using e-Link. The templateid is `1`.
The command is `java -jar batch-processing-tool-0.1-SNAPSHOT-jar-with-dependencies.jar -f turtle -if ./input.ttl -o turtle e-link --templateid 1`.

### e-Entity

Process `input.txt` (with the format `text`) using e-Entity. The output format is `turtle`.
The command is `java -jar batch-processing-tool-0.1-SNAPSHOT-jar-with-dependencies.jar -f text -if ./input.txt -o turtle e-entity`.



## License

Copyright 2015  Agro-Know, Deutsches Forschungszentrum für Künstliche Intelligenz, iMinds, 
Institut für Angewandte Informatik e. V. an der Universität Leipzig, 
Istituto Superiore Mario Boella, Tilde, Vistatec, WRIPL

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

This project uses 3rd party tools. You can find the list of 3rd party tools including their authors and licenses [here](LICENSE-3RD-PARTY).

