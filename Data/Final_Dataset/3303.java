/*
 * Copyright (c) 2011 Lockheed Martin Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.eurekastreams.server.persistence.mappers.db.metrics;

import java.util.List;

import junit.framework.Assert;

import org.eurekastreams.server.domain.EntityType;
import org.eurekastreams.server.domain.dto.StreamDTO;
import org.eurekastreams.server.persistence.mappers.MapperTest;
import org.junit.Before;
import org.junit.Test;

import com.ibm.icu.util.Calendar;

/**
 * Test fixture GetStreamsByMostRecentDbMapper.
 */
public class GetStreamsByMostRecentDbMapperTest extends MapperTest
{
    /**
     * System under test.
     */
    private GetStreamsByMostRecentDbMapper sut;

    /**
     * Setup.
     */
    @Before
    public void setup()
    {
        sut = new GetStreamsByMostRecentDbMapper(4);
        sut.setEntityManager(getEntityManager());
    }

    /**
     * TestExecute.
     */
    @Test
    public void testExecute()
    {
        Calendar cal = Calendar.getInstance();

        // setup the data for: Group5, Group7 (ignored b/c pending), Group1, Group2, Group3, add in some people, but
        // they should
        // be ignored in the
        // results
        // -- the last two with the same date
        System.out.println("TIME: " + cal.getTime());
        getEntityManager().createQuery("UPDATE DomainGroup SET dateAdded = :dateAdded WHERE id=:id").setParameter(
                "id", 5L).setParameter("dateAdded", cal.getTime()).executeUpdate();

        cal.add(Calendar.HOUR, -1);
        getEntityManager().createQuery("UPDATE Person SET dateAdded = :dateAdded WHERE id=:id").setParameter("id",
                4507L).setParameter("dateAdded", cal.getTime()).executeUpdate();

        // ignored b/c PENDING
        cal.add(Calendar.HOUR, -1);
        getEntityManager().createQuery("UPDATE DomainGroup SET dateAdded = :dateAdded WHERE id=:id").setParameter(
                "id", 7L).setParameter("dateAdded", cal.getTime()).executeUpdate();

        cal.add(Calendar.HOUR, -1);
        getEntityManager().createQuery("UPDATE DomainGroup SET dateAdded = :dateAdded WHERE id=:id").setParameter(
                "id", 1L).setParameter("dateAdded", cal.getTime()).executeUpdate();

        cal.add(Calendar.HOUR, -1);
        getEntityManager().createQuery("UPDATE Person SET dateAdded = :dateAdded WHERE id=:id")
                .setParameter("id", 99L).setParameter("dateAdded", cal.getTime()).executeUpdate();

        cal.add(Calendar.HOUR, -1);
        getEntityManager().createQuery("UPDATE DomainGroup SET dateAdded = :dateAdded WHERE id=:id").setParameter(
                "id", 2L).setParameter("dateAdded", cal.getTime()).executeUpdate();

        getEntityManager().createQuery("UPDATE Person SET dateAdded = :dateAdded WHERE id=:id")
                .setParameter("id", 42L).setParameter("dateAdded", cal.getTime()).executeUpdate();

        cal.add(Calendar.HOUR, -1);
        getEntityManager().createQuery("UPDATE DomainGroup SET dateAdded = :dateAdded WHERE id=:id").setParameter(
                "id", 3L).setParameter("dateAdded", cal.getTime()).executeUpdate();

        getEntityManager().clear();

        List<StreamDTO> results = sut.execute(null);
        for (StreamDTO result : results)
        {
            System.out.println(result.getEntityType() + " - " + result.getId() + " " + result.getDateAdded());
        }

        Assert.assertEquals(4, results.size());
        Assert.assertEquals(EntityType.GROUP, results.get(0).getEntityType());
        Assert.assertEquals(5, results.get(0).getId());

        Assert.assertEquals(EntityType.GROUP, results.get(1).getEntityType());
        Assert.assertEquals(1, results.get(1).getId());

        Assert.assertEquals(EntityType.GROUP, results.get(2).getEntityType());
        Assert.assertEquals(2, results.get(2).getId());

        Assert.assertEquals(EntityType.GROUP, results.get(3).getEntityType());
        Assert.assertEquals(3, results.get(3).getId());
    }
}
