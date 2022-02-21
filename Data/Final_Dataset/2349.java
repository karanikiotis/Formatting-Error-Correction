/*
 * Copyright 2011 <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
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
package org.ocpsoft.rewrite.servlet.config;

import org.junit.Assert;

import org.apache.http.client.methods.HttpGet;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ocpsoft.rewrite.config.ConfigurationProvider;
import org.ocpsoft.rewrite.test.HttpAction;
import org.ocpsoft.rewrite.test.RewriteTest;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
@RunWith(Arquillian.class)
public class EncodeQueryConfigurationTest extends RewriteTest
{
   @Deployment(testable = false)
   public static WebArchive getDeployment()
   {
      WebArchive deployment = RewriteTest
               .getDeployment()
               .addPackages(true, ConfigRoot.class.getPackage())
               .addAsServiceProvider(ConfigurationProvider.class, EncodeQueryConfigurationProvider.class);
      return deployment;
   }

   @Test
   public void testQueryEncoding() throws Exception
   {
      HttpAction<HttpGet> action = get("/encodequery?foo=bar");
      Assert.assertEquals(210, action.getResponse().getStatusLine().getStatusCode());
      Assert.assertTrue(action.getCurrentContextRelativeURL().contains("/encodequery?c="));
   }

   @Test
   public void testQueryEncodingExclusions() throws Exception
   {
      HttpAction<HttpGet> action = get("/encodequeryexcluding?foo=bar&keep=this");
      Assert.assertEquals(210, action.getResponse().getStatusLine().getStatusCode());
      Assert.assertTrue(action.getCurrentContextRelativeURL().contains("c="));
      Assert.assertTrue(action.getCurrentContextRelativeURL().contains("keep=this"));
   }

   @Test
   public void testQueryEncodingInclusions() throws Exception
   {
      HttpAction<HttpGet> action = get("/encodequeryspecific?encode1=value1&keep=this&encode2=value2");
      Assert.assertEquals(210, action.getResponse().getStatusLine().getStatusCode());
      Assert.assertTrue(action.getCurrentContextRelativeURL().contains("c="));
      Assert.assertTrue(action.getCurrentContextRelativeURL().contains("keep=this"));
      Assert.assertFalse(action.getCurrentContextRelativeURL().contains("encode1=value1"));
      Assert.assertFalse(action.getCurrentContextRelativeURL().contains("encode2=value2"));
   }

   @Test
   public void testQueryEncodingUnchanged() throws Exception
   {
      HttpAction<HttpGet> action = get("/encodequery");
      Assert.assertEquals(404, action.getResponse().getStatusLine().getStatusCode());
   }
}
