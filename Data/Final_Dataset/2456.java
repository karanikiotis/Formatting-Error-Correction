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
package org.xwiki.extension.xar.internal.script;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.xwiki.component.annotation.Component;
import org.xwiki.extension.xar.question.ConflictQuestion;
import org.xwiki.script.internal.safe.ScriptSafeProvider;

/**
 * @version $Id: a38e4ba3ec8e90a5a65c74ff532fd37a9590577f $
 * @since 4.0M2
 */
@Component
@Singleton
public class ConflictQuestionScriptSafeProvider implements ScriptSafeProvider<ConflictQuestion>
{
    /**
     * Used to provide collection elements safe versions.
     */
    @Inject
    @SuppressWarnings("rawtypes")
    private ScriptSafeProvider safeProvider;

    @Override
    public <S> S get(ConflictQuestion unsafe)
    {
        return (S) new SafeConflictQuestion(unsafe, this.safeProvider);
    }
}
