/*
 * Copyright 2013 NGDATA nv
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.lilyproject.repository.cli;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.lilyproject.repository.model.api.RepositoryNotFoundException;

import java.util.List;

public class DeleteRepositoryCli extends BaseRepositoriesAdminCli {

    protected Option failIfNotExistsOption;

    @Override
    protected String getCmdName() {
        return "lily-delete-repository";
    }

    public static void main(String[] args) {
        new DeleteRepositoryCli().start(args);
    }

    @Override
    @SuppressWarnings("static-access")
    public List<Option> getOptions() {
        List<Option> options = super.getOptions();
        options.add(nameOption);

        failIfNotExistsOption = OptionBuilder
                .withDescription("Fails if the repository did not exist (process status code 1).")
                .withLongOpt("fail-if-not-exists")
                .create();
        options.add(failIfNotExistsOption);

        return options;
    }

    @Override
    public int run(CommandLine cmd) throws Exception {
        int result = super.run(cmd);
        if (result != 0) {
            return result;
        }

        String repositoryName = cmd.getOptionValue(nameOption.getOpt());
        if (repositoryName == null) {
            System.out.println("Specify the name of the repository to delete with -" + nameOption.getOpt());
            return 1;
        }

        boolean exists = true;
        try {
            repositoryModel.delete(repositoryName);
        } catch (RepositoryNotFoundException e) {
            exists = false;
        }

        if (!exists) {
            System.out.println(String.format("Repository '%s' does not exist.", repositoryName));
            if (cmd.hasOption(failIfNotExistsOption.getLongOpt())) {
                return 1;
            }
        } else {
            System.out.println(String.format("Deletion of repository '%s' has been requested.", repositoryName));
        }

        return 0;
    }
}
