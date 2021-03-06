/*
 * Copyright 2013 Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the @author tags. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package org.keycloak.subsystem.extension;

import java.util.List;
import org.jboss.as.controller.AbstractAddStepHandler;
import org.jboss.as.controller.AttributeDefinition;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.ServiceVerificationHandler;
import org.jboss.dmr.ModelNode;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.ADD;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP;
import org.jboss.msc.service.ServiceController;

/**
 * Add a new realm.
 *
 * @author Stan Silvert ssilvert@redhat.com (C) 2013 Red Hat Inc.
 */
public class RealmAddHandler extends AbstractAddStepHandler {

    public static RealmAddHandler INSTANCE = new RealmAddHandler();

    private RealmAddHandler() {}

    @Override
    protected void populateModel(ModelNode operation, ModelNode model) throws OperationFailedException {
        // TODO: localize exception. get id number
        if (!operation.get(OP).asString().equals(ADD)) {
            throw new OperationFailedException("Unexpected operation for add realm. operation=" + operation.toString());
        }

        for (AttributeDefinition attrib : RealmDefinition.ALL_ATTRIBUTES) {
            attrib.validateAndSet(operation, model);
        }

        if (!RealmDefinition.validateTruststoreSetIfRequired(model.clone())) {
            //TODO: externalize message
            throw new OperationFailedException("truststore and truststore-password must be set if both ssl-not-required and disable-trust-maanger are false.");
        }
    }

    @Override
    protected void performRuntime(OperationContext context, ModelNode operation, ModelNode model, ServiceVerificationHandler verificationHandler, List<ServiceController<?>> newControllers) throws OperationFailedException {
        KeycloakAdapterConfigService ckService = KeycloakAdapterConfigService.find(context);
        ckService.addRealm(operation, model);
    }
}
