// Copyright 2015 Eivind Vegsundvåg
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package ninja.eivind.hotsreplayuploader.concurrent.tasks;

import javafx.concurrent.Task;
import ninja.eivind.hotsreplayuploader.models.ReplayFile;
import ninja.eivind.hotsreplayuploader.models.Status;
import ninja.eivind.hotsreplayuploader.models.UploadStatus;
import ninja.eivind.hotsreplayuploader.providers.Provider;
import ninja.eivind.mpq.models.MpqException;
import ninja.eivind.stormparser.StormParser;
import ninja.eivind.stormparser.models.Replay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

/**
 * {@link Task} for uploading a replayFile to a {@link Collection} of {@link Provider}s.<br>
 * Blocks if there are no replays to process.
 */
public class UploadTask extends Task<ReplayFile> {
    private static final Logger LOG = LoggerFactory.getLogger(UploadTask.class);
    private final Collection<Provider> providers;
    private final ReplayFile replayFile;
    private final StormParser parser;


    public UploadTask(final Collection<Provider> providers, ReplayFile replayFile, StormParser parser) {
        this.providers = providers;
        this.replayFile = replayFile;
        this.parser = parser;

        setOnFailed(event -> LOG.error("UploadTask failed.", event.getSource().getException()));
    }

    @Override
    protected ReplayFile call() throws Exception {
        //take suceeded, so we now have a file to handle
        LOG.info("Uploading replayFile " + replayFile);
        providers.forEach(provider -> {

            Replay replay;
            Status preStatus;
            try {
                replay = parser.apply(replayFile.getFile());
                preStatus = provider.getPreStatus(replay);
            } catch (MpqException e) {
                LOG.warn("Could not parse replayFile, deferring to upload: " + replayFile, e);

                preStatus = Status.EXCEPTION;
            }
            if (preStatus == Status.UPLOADED || preStatus == Status.UNSUPPORTED_GAME_MODE) {
                LOG.info("Parsed preStatus reported no need to upload "
                        + replayFile + " for provider " + provider.getName());
                applyStatus(replayFile, provider, preStatus);
            } else {
                final Status upload = provider.upload(replayFile);
                if (upload == null) {
                    throw new RuntimeException("Failed");
                }
                applyStatus(replayFile, provider, upload);
            }
            succeeded();
        });

        return replayFile;
    }

    private void applyStatus(final ReplayFile file, final Provider provider, final Status status) {
        final Collection<UploadStatus> uploadStatuses = file.getUploadStatuses();
        final UploadStatus current = uploadStatuses.stream()
                .filter(uploadStatus -> uploadStatus.getHost().equals(provider.getName()))
                .findFirst()
                .orElse(null);
        if (current == null) {
            uploadStatuses.add(new UploadStatus(provider.getName(), status));
        } else {
            current.setStatus(status);
        }
    }
}
