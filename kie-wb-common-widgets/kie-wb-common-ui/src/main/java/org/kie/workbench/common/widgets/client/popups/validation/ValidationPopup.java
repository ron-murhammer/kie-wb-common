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

package org.kie.workbench.common.widgets.client.popups.validation;

import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import org.guvnor.common.services.shared.validation.model.ValidationMessage;
import org.jboss.errai.ui.client.local.spi.TranslationService;
import org.kie.workbench.common.widgets.client.resources.i18n.KieWorkbenchWidgetsConstants;
import org.uberfire.commons.validation.PortablePreconditions;
import org.uberfire.mvp.Command;

@Dependent
public class ValidationPopup implements ValidationPopupView.Presenter {

    private ValidationPopupView view;

    private ValidationMessageTranslatorUtils validationMessageTranslatorUtils;

    private TranslationService translationService;

    private Command yesCommand;

    private Command cancelCommand;

    @Inject
    public ValidationPopup( final ValidationPopupView view,
                            final ValidationMessageTranslatorUtils validationMessageTranslatorUtils,
                            final TranslationService translationService ) {
        this.view = view;
        this.validationMessageTranslatorUtils = validationMessageTranslatorUtils;
        this.translationService = translationService;
    }

    @PostConstruct
    public void init() {
        view.init( this );
    }

    public void showMessages( final List<ValidationMessage> messages ) {
        clear();
        view.setCancelButtonText( translationService.getTranslation( KieWorkbenchWidgetsConstants.ValidationPopup_Cancel ) );
        view.showCancelButton( true );

        initAndShowModal( () -> {},
                          () -> {},
                          messages );
    }

    public void showCopyValidationMessages( final Command yesCommand,
                                            final Command cancelCommand,
                                            final List<ValidationMessage> validationMessages ) {
        clear();
        view.setYesButtonText( translationService.getTranslation( KieWorkbenchWidgetsConstants.ValidationPopup_YesCopyAnyway ) );
        view.showYesButton( true );

        view.setCancelButtonText( translationService.getTranslation( KieWorkbenchWidgetsConstants.ValidationPopup_Cancel ) );
        view.showCancelButton( true );

        initAndShowModal( yesCommand,
                          cancelCommand,
                          validationMessages );
    }

    public void showSaveValidationMessages( final Command yesCommand,
                                            final Command cancelCommand,
                                            final List<ValidationMessage> validationMessages ) {
        clear();
        view.setYesButtonText( translationService.getTranslation( KieWorkbenchWidgetsConstants.ValidationPopup_YesSaveAnyway ) );
        view.showYesButton( true );

        view.setCancelButtonText( translationService.getTranslation( KieWorkbenchWidgetsConstants.ValidationPopup_Cancel ) );
        view.showCancelButton( true );

        initAndShowModal( yesCommand,
                          cancelCommand,
                          validationMessages );
    }

    private void initAndShowModal( final Command yesCommand,
                                   final Command cancelCommand,
                                   final List<ValidationMessage> validationMessages ) {
        this.yesCommand = PortablePreconditions.checkNotNull( "yesCommand",
                                                              yesCommand );
        this.cancelCommand = PortablePreconditions.checkNotNull( "cancelCommand",
                                                                 cancelCommand );

        view.setValidationMessages( validationMessageTranslatorUtils.translate( validationMessages ) );
        view.show();
    }

    private void clear() {
        view.showYesButton( false );
        view.showCancelButton( false );
    }

    @Override
    public void onYesButtonClicked() {
        if ( yesCommand != null ) {
            yesCommand.execute();
        }
        view.hide();
    }

    @Override
    public void onCancelButtonClicked() {
        if ( cancelCommand != null ) {
            cancelCommand.execute();
        }
        view.hide();
    }
}
