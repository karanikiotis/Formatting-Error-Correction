package com.vimeo.sample_model;

import org.junit.Test;

/**
 * Created by restainoa on 2/2/17.
 */
public class ExternalModelGeneric1Test {

    @Test
    public void typeAdapterWasGenerated() throws Exception {
        Utils.verifyTypeAdapterGeneration(ExternalModelGeneric1.class);
    }

}