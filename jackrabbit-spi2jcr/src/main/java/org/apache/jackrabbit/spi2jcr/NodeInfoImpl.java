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
package org.apache.jackrabbit.spi2jcr;

import org.apache.jackrabbit.spi.commons.conversion.NamePathResolver;
import org.apache.jackrabbit.spi.commons.conversion.NameException;
import org.apache.jackrabbit.spi.commons.name.NameConstants;
import org.apache.jackrabbit.spi.Name;

import javax.jcr.RepositoryException;
import javax.jcr.Node;
import javax.jcr.PropertyIterator;
import javax.jcr.NamespaceException;
import javax.jcr.nodetype.NodeType;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * <code>NodeInfoImpl</code> implements a <code>NodeInfo</code> on top of a JCR
 * repository.
 */
class NodeInfoImpl extends org.apache.jackrabbit.spi.commons.NodeInfoImpl {

    /**
     * Creates a new node info for the given <code>node</code>.
     *
     * @param node       the JCR node.
     * @param idFactory  the id factory.
     * @param resolver
     * @throws RepositoryException if an error occurs while reading from
     *                             <code>node</code>.
     */
    public NodeInfoImpl(Node node,
                        IdFactoryImpl idFactory,
                        NamePathResolver resolver)
            throws RepositoryException, NameException {
        super(node.getName().length() == 0 ? null : idFactory.createNodeId(node.getParent(), resolver),
                node.getName().length() == 0 ? NameConstants.ROOT : resolver.getQName(node.getName()),
                resolver.getQPath(node.getPath()),
                idFactory.createNodeId(node, resolver), node.getIndex(),
                resolver.getQName(node.getPrimaryNodeType().getName()),
                getNodeTypeNames(node.getMixinNodeTypes(), resolver),
                getPropertyIds(node.getReferences(), resolver, idFactory),
                getPropertyIds(node.getProperties(), resolver, idFactory));
    }

    /**
     * Returns the qualified names of the passed node types using the namespace
     * resolver to parse the names.
     *
     * @param nt         the node types
     * @param resolver
     * @return the qualified names of the node types.
     * @throws NameException   if a node type returns an illegal name.
     * @throws NamespaceException if the name of a node type contains a
     *                            prefix that is not known to <code>resolver</code>.
     */
    private static Name[] getNodeTypeNames(NodeType[] nt,
                                           NamePathResolver resolver)
            throws NameException, NamespaceException {
        Name[] names = new Name[nt.length];
        for (int i = 0; i < nt.length; i++) {
            Name ntName = resolver.getQName(nt[i].getName());
            names[i] = ntName;
        }
        return names;
    }

    /**
     * Returns property ids for the passed JCR properties.
     *
     * @param props      the JCR properties.
     * @param resolver
     * @param idFactory  the id factory.
     * @return the property ids for the passed JCR properties.
     * @throws RepositoryException if an error occurs while reading from the
     *                             properties.
     */
    private static Iterator getPropertyIds(PropertyIterator props,
                                              NamePathResolver resolver,
                                              IdFactoryImpl idFactory)
            throws RepositoryException {
        List references = new ArrayList();
        while (props.hasNext()) {
            references.add(idFactory.createPropertyId(props.nextProperty(), resolver));
        }
        return references.iterator();
    }
}
