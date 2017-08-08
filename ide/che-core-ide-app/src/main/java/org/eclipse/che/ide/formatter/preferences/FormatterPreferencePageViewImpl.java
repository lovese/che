/*******************************************************************************
 * Copyright (c) 2012-2017 Codenvy, S.A.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Codenvy, S.A. - initial API and implementation
 *******************************************************************************/
package org.eclipse.che.ide.formatter.preferences;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Implementation of the code formatter importer page.
 */
@Singleton
public class FormatterPreferencePageViewImpl implements FormatterPreferencePageView {

    interface FormatterPreferencePageViewImplUiBinder extends UiBinder<DockLayoutPanel, FormatterPreferencePageViewImpl> {
    }

    private ActionDelegate delegate;
    private Widget         widget;
    private FileUpload     fileUpload;
    private String         fileContent;

    @UiField
    FormPanel uploadForm;
    @UiField
    Label     errorMessage;
    @UiField
    Button    importButton;

    @Inject
    public FormatterPreferencePageViewImpl(FormatterPreferencePageViewImplUiBinder uiBinder) {
        widget = uiBinder.createAndBindUi(this);
        fileUpload = new FileUpload();
        uploadForm.add(fileUpload);

        importButton.setEnabled(false);
    }

    @Override
    public void setDelegate(ActionDelegate delegate) {
        this.delegate = delegate;
    }

    @Override
    public Widget asWidget() {
        return widget;
    }

    public void showDialog() {
        uploadForm.remove(fileUpload);
        errorMessage.setText("");
        importButton.setEnabled(false);
        fileContent = null;
        fileUpload = new FileUpload();
        fileUpload.setHeight("22px");
        fileUpload.setWidth("100%");
        fileUpload.setName("file");
        fileUpload.ensureDebugId("import-formatter-ChooseFile");
        readFileContent(fileUpload.getElement());
        fileUpload.addChangeHandler(event -> {
            if (delegate != null) {
                readFileContent(fileUpload.getElement());
                importButton.setEnabled(fileUpload.getFilename() != null);
            }
        });
        uploadForm.add(fileUpload);
    }

    @Override
    public String getFileContent() {
        return fileContent;
    }

    @Override
    public boolean isWorkspaceTarget() {
        return false;
    }

    @UiHandler("importButton")
    public void importButtonClick(ClickEvent event) {
        delegate.onImportButtonClicked();
    }

    private native void readFileContent(Element element) /*-{
        var instance = this;

        function readFileContent(evt) {
            // Check for the various File API support.
            if (!window.File || !window.FileReader || !window.FileList || !window.Blob) {
                instance.@org.eclipse.che.ide.formatter.preferences.FormatterPreferencePageViewImpl::onError(Ljava/lang/String;)
                ('The File APIs are not fully supported in this browser.');
                return;
            }

            var selectedFile = evt.target.files[0];

            var reader = new FileReader();
            reader.onload = function () {
                //getting file's content
                instance.@org.eclipse.che.ide.formatter.preferences.FormatterPreferencePageViewImpl::fileContent = reader.result;
            };

            reader.onerror = function (event) {
                instance.@org.eclipse.che.ide.formatter.preferences.FormatterPreferencePageViewImpl::onError(Ljava/lang/String;)
                ('Error reading file ' + event.target.error.code);
            };

            reader.readAsText(selectedFile);
        }

        element.addEventListener('change', readFileContent, false);
    }-*/;

    private void onError(String message) {
        delegate.showErrorMessage(message);
    }

}
