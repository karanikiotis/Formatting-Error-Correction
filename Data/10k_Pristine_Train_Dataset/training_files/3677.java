package org.fossasia.openevent.api;

import org.fossasia.openevent.api.processor.EventListResponseProcessor;
import org.fossasia.openevent.api.processor.MicrolocationListResponseProcessor;
import org.fossasia.openevent.api.processor.SessionListResponseProcessor;
import org.fossasia.openevent.api.processor.SpeakerListResponseProcessor;
import org.fossasia.openevent.api.processor.SponsorListResponseProcessor;
import org.fossasia.openevent.api.processor.TrackListResponseProcessor;

/**
 * User: MananWason
 * Date: 31-05-2015
 * <p/>
 * A singleton to keep track of download
 */
public final class DataDownloadManager {
    private static DataDownloadManager instance;

    private DataDownloadManager() {
    }

    public static DataDownloadManager getInstance() {
        if (instance == null) {
            instance = new DataDownloadManager();
        }
        return instance;
    }

    public void downloadEvents() {
        APIClient.getOpenEventAPI().getEvents().enqueue(new EventListResponseProcessor());
    }

    public void downloadSpeakers() {
        APIClient.getOpenEventAPI().getSpeakers().enqueue(new SpeakerListResponseProcessor());
    }

    public void downloadSponsors() {
        APIClient.getOpenEventAPI().getSponsors().enqueue(new SponsorListResponseProcessor());
    }

    public void downloadSession() {
        APIClient.getOpenEventAPI().getSessions("start_time.asc").enqueue(new SessionListResponseProcessor());
    }

    public void downloadTracks() {
        APIClient.getOpenEventAPI().getTracks().enqueue(new TrackListResponseProcessor());
    }

    public void downloadMicrolocations() {
        APIClient.getOpenEventAPI().getMicrolocations().enqueue(new MicrolocationListResponseProcessor());
    }
}
