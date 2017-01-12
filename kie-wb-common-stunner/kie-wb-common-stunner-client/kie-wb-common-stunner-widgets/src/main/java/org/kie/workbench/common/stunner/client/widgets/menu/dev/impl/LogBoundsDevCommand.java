/*
 * Copyright 2017 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kie.workbench.common.stunner.client.widgets.menu.dev.impl;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import org.kie.workbench.common.stunner.core.client.session.impl.AbstractClientSessionManager;
import org.kie.workbench.common.stunner.core.graph.Element;
import org.kie.workbench.common.stunner.core.graph.content.Bounds;
import org.kie.workbench.common.stunner.core.graph.content.view.View;

@Dependent
public class LogBoundsDevCommand extends AbstractSelectionDevCommand {

    private static Logger LOGGER = Logger.getLogger( LogBoundsDevCommand.class.getName() );

    protected LogBoundsDevCommand() {
        this( null );
    }

    @Inject
    public LogBoundsDevCommand( final AbstractClientSessionManager sessionManager ) {
        super( sessionManager );
    }

    @Override
    public String getText() {
        return "Log Bounds";
    }

    @Override
    protected void execute( final Element<View<?>> item ) {
        final Bounds bounds = item.getContent().getBounds();
        final Bounds.Bound ul = bounds.getUpperLeft();
        final Bounds.Bound lr = bounds.getLowerRight();
        LOGGER.log( Level.FINE,
                    "Bounds for [" + item.getUUID() + "] ARE " +
                            "{ UL=[" + ul.getX() + ", " + ul.getY() + "] " +
                            "LR=[ " + lr.getX() + ", " + lr.getY() + "] }" );
    }
}