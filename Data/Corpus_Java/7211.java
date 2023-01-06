/*
 *
 *  Copyright 2012-2014 Eurocommercial Properties NV
 *
 *
 *  Licensed under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package org.estatio.integtests.party;

import javax.inject.Inject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.apache.isis.applib.fixturescripts.FixtureScript;
import org.incode.module.communications.dom.impl.commchannel.CommunicationChannelOwner_newChannelContributions;
import org.estatio.dom.party.PartyRepository;
import org.estatio.dom.party.Party;
import org.estatio.fixture.EstatioBaseLineFixture;
import org.estatio.fixture.party.OrganisationForHelloWorldGb;
import org.estatio.integtests.EstatioIntegrationTest;

import static org.hamcrest.CoreMatchers.is;

public class CommunicationChannelContributions_IntegTest extends EstatioIntegrationTest {

    public static class CommunicationChannels extends CommunicationChannelContributions_IntegTest {

        @Before
        public void setupData() {
            runScript(new FixtureScript() {
                @Override
                protected void execute(ExecutionContext executionContext) {
                    executionContext.executeChild(this, new EstatioBaseLineFixture());
                    executionContext.executeChild(this, new OrganisationForHelloWorldGb());
                }
            });
        }

        @Inject
        private PartyRepository partyRepository;
        @Inject
        private CommunicationChannelOwner_newChannelContributions communicationChannelContributions;

        private Party partyHelloWorld;

        @Before
        public void setUp() throws Exception {
            partyHelloWorld = partyRepository.findPartyByReference(OrganisationForHelloWorldGb.REF);
        }

        @Test
        public void fixtureData() throws Exception {
            Assert.assertThat(communicationChannelContributions.communicationChannels(partyHelloWorld).size(), is(5));
        }
    }
}