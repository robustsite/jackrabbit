/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.jackrabbit.spi.commons.nodetype.constraint;

import javax.jcr.NamespaceException;
import javax.jcr.PropertyType;
import javax.jcr.RepositoryException;
import javax.jcr.nodetype.ConstraintViolationException;

import org.apache.jackrabbit.spi.Name;
import org.apache.jackrabbit.spi.QValue;
import org.apache.jackrabbit.spi.commons.conversion.NameException;
import org.apache.jackrabbit.spi.commons.conversion.NamePathResolver;
import org.apache.jackrabbit.spi.commons.conversion.NameResolver;
import org.apache.jackrabbit.spi.commons.nodetype.InvalidConstraintException;

/**
 * <code>ReferenceConstraint</code> ...
 */
class ReferenceConstraint extends ValueConstraint {

    private final Name ntName;

    static ReferenceConstraint create(String qualifiedDefinition) {
        // constraint format: String representation of qualified name
        return new ReferenceConstraint(qualifiedDefinition, NAME_FACTORY.create(qualifiedDefinition));
    }

    static ReferenceConstraint create(String definition, NameResolver resolver)
            throws InvalidConstraintException {
        // constraint format: JCR name in prefix form
        try {
            Name name = resolver.getQName(definition);
            return new ReferenceConstraint(name.toString(), name);
        } catch (NameException e) {
            String msg = "Invalid name constraint: " + definition;
            log.debug(msg);
            throw new InvalidConstraintException(msg, e);
        } catch (NamespaceException e) {
            String msg = "Invalid name constraint: " + definition;
            log.debug(msg);
            throw new InvalidConstraintException(msg, e);
        }
    }

    private ReferenceConstraint(String qualifiedDefinition, Name ntName) {
        super(qualifiedDefinition);
        this.ntName = ntName;
    }

    /**
     * Uses {@link NamePathResolver#getJCRName(Name)} to convert the
     * qualified <code>Name</code> into a JCR name.
     *
     * @see ValueConstraint#getDefinition(NamePathResolver)
     * @param resolver
     */
    public String getDefinition(NamePathResolver resolver) {
        try {
            return resolver.getJCRName(ntName);
        } catch (NamespaceException e) {
            // should never get here, return raw definition as fallback
            return getQualifiedDefinition();
        }
    }

    /**
     * @see ValueConstraint#check(QValue)
     */
    void check(QValue value) throws ConstraintViolationException, RepositoryException {
        if (value == null) {
            throw new ConstraintViolationException("Null value does not satisfy the constraint '" + getQualifiedDefinition() + "'");
        }
        switch (value.getType()) {
            case PropertyType.REFERENCE:
            case PropertyType.WEAKREFERENCE:
                // TODO check value constraint (requires a session)
                log.warn("validation of reference constraint is not yet implemented");
                return;

            default:
                String msg = "Reference constraint can not be applied to value of type: "
                        + PropertyType.nameFromValue(value.getType());
                log.debug(msg);
                throw new RepositoryException(msg);
        }
    }

}
