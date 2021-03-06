/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates.
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

package org.kie.workbench.common.screens.projecteditor.client.menu;

import java.util.List;

import com.google.gwtmockito.GwtMockitoTestRunner;
import org.guvnor.common.services.project.context.ProjectContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.workbench.common.services.shared.project.KieProject;
import org.mockito.Mock;
import org.uberfire.client.mvp.PlaceManager;
import org.uberfire.workbench.model.menu.MenuItem;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(GwtMockitoTestRunner.class)
public class ProjectMenuTest {

    private ProjectMenu menu;

    @Mock
    private PlaceManager placeManager;

    @Mock
    private ProjectContext projectContext;

    @Before
    public void setup() {
        menu = new ProjectMenu( placeManager,
                                projectContext );
    }

    @Test
    public void getMenuItemsSynchronizesDisabledState() {
        final List<MenuItem> menus = menu.getMenuItems();

        assertFalse( menus.get( 0 ).isEnabled() );
        assertTrue( menus.get( 1 ).isEnabled() );
    }

    @Test
    public void getMenuItemsSynchronizesEnabledState() {
        when( projectContext.getActiveProject() ).thenReturn( mock( KieProject.class ) );

        final List<MenuItem> menus = menu.getMenuItems();

        assertTrue( menus.get( 0 ).isEnabled() );
        assertTrue( menus.get( 1 ).isEnabled() );
    }

}
