/*
 * Copyright 2017 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kie.workbench.common.screens.library.client.widgets;

import java.util.ArrayList;
import java.util.List;

import org.jboss.errai.ioc.client.api.ManagedInstance;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.workbench.common.screens.projecteditor.client.handlers.NewPackageHandler;
import org.kie.workbench.common.screens.projecteditor.client.handlers.NewProjectHandler;
import org.kie.workbench.common.widgets.client.handlers.NewResourceHandler;
import org.kie.workbench.common.widgets.client.handlers.NewResourcePresenter;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.uberfire.mvp.Command;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ProjectActionsWidgetTest {

    @Mock
    private ProjectActionsWidget.View view;

    @Mock
    private ManagedInstance<NewResourceHandler> newResourceHandlers;

    @Mock
    private NewResourcePresenter newResourcePresenter;

    private ProjectActionsWidget presenter;

    @Before
    public void setup() {
        presenter = spy( new ProjectActionsWidget( view,
                                                   newResourceHandlers,
                                                   newResourcePresenter ) );
    }

    @Test
    public void initTest() {
        NewResourceHandler projectHandler = mock( NewProjectHandler.class );
        doReturn( true ).when( projectHandler ).canCreate();
        NewResourceHandler packageHandler = mock( NewPackageHandler.class );
        doReturn( true ).when( packageHandler ).canCreate();
        NewResourceHandler type1Handler = mock( NewResourceHandler.class );
        doReturn( true ).when( type1Handler ).canCreate();
        NewResourceHandler type2Handler = mock( NewResourceHandler.class );
        doReturn( true ).when( type2Handler ).canCreate();

        List<NewResourceHandler> handlers = new ArrayList<>();
        handlers.add( projectHandler );
        handlers.add( packageHandler );
        handlers.add( type1Handler );
        handlers.add( type2Handler );
        doReturn( handlers ).when( presenter ).getNewResourceHandlers();

        presenter.init( mock( Command.class ) );

        verify( view, times( 2 ) ).addResourceHandler( any( NewResourceHandler.class ) );
        verify( view ).addResourceHandler( type1Handler );
        verify( view ).addResourceHandler( type2Handler );
    }
}
