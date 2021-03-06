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

package org.kie.workbench.common.stunner.client.widgets.presenters.session.impl;

import org.jboss.errai.common.client.ui.ElementWrapperWidget;
import org.kie.workbench.common.stunner.client.widgets.palette.PaletteWidget;
import org.kie.workbench.common.stunner.client.widgets.palette.PaletteWidgetFactory;
import org.kie.workbench.common.stunner.client.widgets.presenters.session.SessionPresenter;
import org.kie.workbench.common.stunner.client.widgets.presenters.session.SessionViewer;
import org.kie.workbench.common.stunner.client.widgets.toolbar.Toolbar;
import org.kie.workbench.common.stunner.client.widgets.toolbar.ToolbarFactory;
import org.kie.workbench.common.stunner.core.client.api.SessionManager;
import org.kie.workbench.common.stunner.core.client.canvas.AbstractCanvasHandler;
import org.kie.workbench.common.stunner.core.client.components.palette.model.definition.DefinitionSetPalette;
import org.kie.workbench.common.stunner.core.client.service.ClientRuntimeError;
import org.kie.workbench.common.stunner.core.client.session.impl.AbstractClientReadOnlySession;
import org.kie.workbench.common.stunner.core.diagram.Diagram;

public abstract class AbstractSessionPresenter<D extends Diagram, H extends AbstractCanvasHandler,
        S extends AbstractClientReadOnlySession, E extends SessionViewer<S, H, D>>
        implements SessionPresenter<S, H, D> {

    private final SessionManager sessionManager;
    private final ToolbarFactory<S> toolbarFactory;
    private final PaletteWidgetFactory<DefinitionSetPalette, ?> paletteFactory;
    private final SessionPresenter.View view;

    private D diagram;
    private Toolbar<S> toolbar;
    private PaletteWidget<DefinitionSetPalette> palette;
    private boolean hasToolbar = false;
    private boolean hasPalette = false;
    private boolean displayNotifications = false;
    private boolean displayErrors = false;

    @SuppressWarnings("unchecked")
    protected AbstractSessionPresenter(final SessionManager sessionManager,
                                       final ToolbarFactory<S> toolbarFactory,
                                       final PaletteWidgetFactory<DefinitionSetPalette, ?> paletteFactory,
                                       final SessionPresenter.View view) {
        this.sessionManager = sessionManager;
        this.toolbarFactory = toolbarFactory;
        this.paletteFactory = paletteFactory;
        this.view = view;
        this.hasToolbar = true;
        this.hasPalette = true;
    }

    protected AbstractSessionPresenter(final SessionManager sessionManager,
                                       final ToolbarFactory<S> toolbarFactory,
                                       final SessionPresenter.View view) {
        this(sessionManager,
             toolbarFactory,
             null,
             view);
    }

    protected AbstractSessionPresenter(final SessionManager sessionManager,
                                       final PaletteWidgetFactory<DefinitionSetPalette, ?> paletteFactory,
                                       final SessionPresenter.View view) {
        this(sessionManager,
             null,
             paletteFactory,
             view);
    }

    protected abstract E getDisplayer();

    @Override
    public void open(final D diagram,
                     final S session,
                     final SessionPresenterCallback<S, D> callback) {
        this.diagram = diagram;
        open(session,
             callback);
    }

    public void open(final S item,
                     final SessionPresenterCallback<S, D> callback) {
        beforeOpen(item);
        getDisplayer().open(item,
                            new SessionViewer.SessionViewerCallback<S, D>() {
                                @Override
                                public void afterCanvasInitialized() {
                                    callback.afterCanvasInitialized();
                                    sessionManager.open(getInstance());
                                    callback.afterSessionOpened();
                                }

                                @Override
                                public void onSuccess() {
                                    onSessionOpened(item);
                                    callback.onSuccess();
                                }

                                @Override
                                public void onError(final ClientRuntimeError error) {
                                    AbstractSessionPresenter.this.showError(error);
                                    callback.onError(error);
                                }
                            });
    }

    public void open(final S item,
                     final int width,
                     final int height,
                     final SessionPresenterCallback<S, D> callback) {
        beforeOpen(item);
        getDisplayer().open(item,
                            width,
                            height,
                            new SessionViewer.SessionViewerCallback<S, D>() {
                                @Override
                                public void afterCanvasInitialized() {
                                    callback.afterCanvasInitialized();
                                    sessionManager.open(getInstance());
                                    callback.afterSessionOpened();
                                }

                                @Override
                                public void onSuccess() {
                                    onSessionOpened(item);
                                    callback.onSuccess();
                                }

                                @Override
                                public void onError(final ClientRuntimeError error) {
                                    AbstractSessionPresenter.this.showError(error);
                                    callback.onError(error);
                                }
                            });
    }

    @Override
    public SessionPresenter<S, H, D> withToolbar(final boolean hasToolbar) {
        this.hasToolbar = hasToolbar;
        return this;
    }

    @Override
    public SessionPresenter<S, H, D> withPalette(final boolean hasPalette) {
        this.hasPalette = hasPalette;
        return this;
    }

    @Override
    public SessionPresenter<S, H, D> displayNotifications(final boolean showNotifications) {
        this.displayNotifications = showNotifications;
        return this;
    }

    @Override
    public SessionPresenter<S, H, D> displayErrors(final boolean showErrors) {
        this.displayErrors = showErrors;
        return this;
    }

    public void scale(final int width,
                      final int height) {
        getDisplayer().scale(width,
                             height);
    }

    public void clear() {
        if (null != getPalette()) {
            getPalette().unbind();
        }
        if (null != getToolbar()) {
            getToolbar().clear();
        }
        sessionManager.destroy();
        getDisplayer().clear();
        diagram = null;
    }

    @Override
    public void destroy() {
        destroyToolbar();
        destroyPalette();
        sessionManager.destroy();
        getDisplayer().destroy();
        getView().destroy();
        diagram = null;
    }

    public S getInstance() {
        return getDisplayer().getInstance();
    }

    @Override
    public Toolbar<S> getToolbar() {
        return toolbar;
    }

    @Override
    public PaletteWidget<DefinitionSetPalette> getPalette() {
        return palette;
    }

    @Override
    public View getView() {
        return view;
    }

    public H getHandler() {
        return getDisplayer().getHandler();
    }

    protected void beforeOpen(final S item) {
        getView().showLoading(true);
    }

    protected void onSessionOpened(final S session) {
        destroyToolbar();
        destroyPalette();
        if (hasToolbar) {
            toolbar = buildToolbar(session);
            getView().setToolbarWidget(toolbar.getView());
        }
        if (hasPalette) {
            this.palette = buildPalette(session);
            getView().setPaletteWidget(ElementWrapperWidget.getWidget(getPalette().getElement()));
        }
        getView().setCanvasWidget(getDisplayer().getView());
        getView().showLoading(false);
    }

    private Toolbar<S> buildToolbar(final S session) {
        if (null == toolbarFactory) {
            throw new UnsupportedOperationException("This session presenter with type [" + this.getClass().getName() + "] does not supports the toolbar.");
        }
        return toolbarFactory.build(session);
    }

    private PaletteWidget<DefinitionSetPalette> buildPalette(final S session) {
        if (null == paletteFactory) {
            throw new UnsupportedOperationException("This session presenter with type [" + this.getClass().getName() + "] does not supports the palette.");
        }
        final Diagram diagram = session.getCanvasHandler().getDiagram();
        return paletteFactory.newPalette(diagram.getMetadata().getShapeSetId(),
                                         session.getCanvasHandler());
    }

    private void destroyToolbar() {
        if (null != getToolbar()) {
            getToolbar().destroy();
            toolbar = null;
        }
    }

    private void destroyPalette() {
        if (null != getPalette()) {
            getPalette().unbind();
            getPalette().destroy();
        }
    }

    protected void showError(final ClientRuntimeError error) {
        if (isDisplayErrors()) {
            getView().showLoading(false);
            getView().showMessage(error.getMessage());
        }
    }

    protected void showError(final String error) {
        if (isDisplayErrors()) {
            getView().showMessage(error);
        }
    }

    protected void showMessage(final String message) {
        if (isDisplayNotifications()) {
            getView().showMessage(message);
        }
    }

    protected D getDiagram() {
        return diagram;
    }

    protected SessionManager getSessionManager() {
        return sessionManager;
    }

    private boolean isDisplayNotifications() {
        return displayNotifications;
    }

    private boolean isDisplayErrors() {
        return displayErrors;
    }
}
