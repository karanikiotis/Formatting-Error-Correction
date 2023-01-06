/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.xwiki.notifications.internal.email;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import javax.mail.Session;

import org.quartz.JobExecutionException;
import org.xwiki.component.annotation.Component;
import org.xwiki.component.annotation.InstantiationStrategy;
import org.xwiki.component.descriptor.ComponentInstantiationStrategy;
import org.xwiki.mail.MailListener;
import org.xwiki.mail.MailSender;
import org.xwiki.mail.SessionFactory;
import org.xwiki.model.reference.DocumentReference;
import org.xwiki.wiki.descriptor.WikiDescriptorManager;

/**
 * Send notifications emails for specified users.
 *
 * @version $Id: 8ab572974ac001269b831fa1bf421d6fe1f92c1b $
 * @since 9.5RC1
 */
@Component(roles = NotificationEmailSender.class)
@InstantiationStrategy(ComponentInstantiationStrategy.PER_LOOKUP)
public class NotificationEmailSender
{
    @Inject
    private MailSender mailSender;

    @Inject
    private SessionFactory sessionFactory;

    @Inject
    @Named("database")
    private Provider<MailListener> mailListenerProvider;

    @Inject
    private Provider<NotificationMimeMessageIterator> notificationMimeMessageIteratorProvider;

    @Inject
    private WikiDescriptorManager wikiDescriptorManager;

    /**
     * Send notifications emails for specified users.
     * @param fromDate only send notifications about events that happened after this date
     * @param notificationUserIterator iterator for users interested in the notifications emails
     * @throws JobExecutionException if error happens
     */
    public void sendEmails(Date fromDate, NotificationUserIterator notificationUserIterator)
            throws JobExecutionException
    {
        Map<String, Object> emailFactoryParameters = new HashMap<>();

        DocumentReference templateReference = new DocumentReference(wikiDescriptorManager.getCurrentWikiId(),
                Arrays.asList("XWiki", "Notifications"), "MailTemplate");

        NotificationMimeMessageIterator notificationMimeMessageIterator = notificationMimeMessageIteratorProvider.get();
        notificationMimeMessageIterator.initialize(notificationUserIterator, emailFactoryParameters,
                fromDate, templateReference);

        Session session = this.sessionFactory.create(Collections.emptyMap());
        MailListener mailListener = mailListenerProvider.get();

        // Pass it to the message sender to send it asynchronously.
        mailSender.sendAsynchronously(notificationMimeMessageIterator, session, mailListener);
    }
}
