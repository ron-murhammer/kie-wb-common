/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates.
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
package org.kie.workbench.common.stunner.cm.client.session;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import org.kie.workbench.common.stunner.client.lienzo.canvas.wires.WiresCanvas;
import org.kie.workbench.common.stunner.cm.client.wires.CaseModellerControlFactoryImpl;
import org.kie.workbench.common.stunner.cm.qualifiers.CaseManagementEditor;
import org.kie.workbench.common.stunner.core.client.canvas.AbstractCanvas;
import org.kie.workbench.common.stunner.core.client.canvas.AbstractCanvasHandler;
import org.kie.workbench.common.stunner.core.client.canvas.CanvasFactory;
import org.kie.workbench.common.stunner.core.client.canvas.controls.actions.CanvasNameEditionControl;
import org.kie.workbench.common.stunner.core.client.canvas.controls.actions.CanvasValidationControl;
import org.kie.workbench.common.stunner.core.client.canvas.controls.builder.ElementBuilderControl;
import org.kie.workbench.common.stunner.core.client.canvas.controls.connection.ConnectionAcceptorControl;
import org.kie.workbench.common.stunner.core.client.canvas.controls.containment.ContainmentAcceptorControl;
import org.kie.workbench.common.stunner.core.client.canvas.controls.docking.DockingAcceptorControl;
import org.kie.workbench.common.stunner.core.client.canvas.controls.drag.DragControl;
import org.kie.workbench.common.stunner.core.client.canvas.controls.palette.CanvasPaletteControl;
import org.kie.workbench.common.stunner.core.client.canvas.controls.pan.PanControl;
import org.kie.workbench.common.stunner.core.client.canvas.controls.select.SelectionControl;
import org.kie.workbench.common.stunner.core.client.canvas.controls.zoom.ZoomControl;
import org.kie.workbench.common.stunner.core.client.command.CanvasCommandManager;
import org.kie.workbench.common.stunner.core.client.command.Request;
import org.kie.workbench.common.stunner.core.client.command.SessionCommandManager;
import org.kie.workbench.common.stunner.core.client.session.Session;
import org.kie.workbench.common.stunner.core.client.session.impl.AbstractClientFullSession;
import org.kie.workbench.common.stunner.core.graph.Element;
import org.kie.workbench.common.stunner.core.registry.RegistryFactory;

@Dependent
@CaseManagementEditor
public class CaseManagementClientFullSession extends AbstractClientFullSession {

    private CanvasPaletteControl<AbstractCanvasHandler> canvasPaletteControl;
    private CanvasNameEditionControl<AbstractCanvasHandler, Element> canvasNameEditionControl;

    @Inject
    @SuppressWarnings("unchecked")
    public CaseManagementClientFullSession(final @CaseManagementEditor CanvasFactory<AbstractCanvas, AbstractCanvasHandler> factory,
                                           final CanvasCommandManager<AbstractCanvasHandler> canvasCommandManager,
                                           final @Session SessionCommandManager<AbstractCanvasHandler> sessionCommandManager,
                                           final @Request SessionCommandManager<AbstractCanvasHandler> requestCommandManager,
                                           final RegistryFactory registryFactory,
                                           final CanvasPaletteControl<AbstractCanvasHandler> canvasPaletteControl,
                                           final CanvasNameEditionControl<AbstractCanvasHandler, Element> canvasNameEditionControl) {
        super(factory.newCanvas(),
              factory.newCanvasHandler(),
              factory.newControl(CanvasValidationControl.class),
              factory.newControl(SelectionControl.class),
              factory.newControl(ZoomControl.class),
              factory.newControl(PanControl.class),
              canvasCommandManager,
              () -> sessionCommandManager,
              () -> requestCommandManager,
              registryFactory.newCommandRegistry(),
              factory.newControl(DragControl.class),
              factory.newControl(ConnectionAcceptorControl.class),
              factory.newControl(ContainmentAcceptorControl.class),
              factory.newControl(DockingAcceptorControl.class),
              factory.newControl(ElementBuilderControl.class));
        this.canvasPaletteControl = factory.newControl(CanvasPaletteControl.class);
        this.canvasNameEditionControl = factory.newControl(CanvasNameEditionControl.class);
        getRegistrationHandler().registerCanvasHandlerControl(canvasPaletteControl);
        canvasPaletteControl.setCommandManagerProvider(() -> sessionCommandManager);
        getRegistrationHandler().registerCanvasHandlerControl(canvasNameEditionControl);
        canvasNameEditionControl.setCommandManagerProvider(() -> sessionCommandManager);
        ((WiresCanvas) getCanvas()).getWiresManager().setWiresControlFactory(new CaseModellerControlFactoryImpl());
    }

    public CanvasPaletteControl<AbstractCanvasHandler> getPaletteControl() {
        return canvasPaletteControl;
    }

    public CanvasNameEditionControl<AbstractCanvasHandler, Element> getCanvasNameEditionControl() {
        return canvasNameEditionControl;
    }
}
