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
package org.eclipse.che.api.workspace.server.spi.dummy;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

import org.eclipse.che.api.workspace.server.spi.RuntimeInfrastructure;

public class DummyInfrastructureModule extends AbstractModule {
    @Override
    protected void configure() {
        Multibinder<RuntimeInfrastructure> mb = Multibinder.newSetBinder(binder(), RuntimeInfrastructure.class);
        mb.addBinding().to(DummyRuntimeInfrastructure.class);
    }
}
