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
package org.apache.jackrabbit.jcr2spi.operation;

import org.apache.jackrabbit.jcr2spi.state.ItemState;
import org.apache.jackrabbit.spi.ItemId;
import org.apache.jackrabbit.spi.NodeId;

import javax.jcr.AccessDeniedException;
import javax.jcr.RepositoryException;
import javax.jcr.UnsupportedRepositoryOperationException;
import javax.jcr.version.VersionException;

/**
 * <code>Remove</code>...
 */
public class Remove extends AbstractOperation {

    private ItemId removeId;
    private NodeId parentId;

    private Remove(ItemId removeId, NodeId parentId) {
        this.removeId = removeId;
        this.parentId = parentId;
        addAffectedItemId(removeId);
        addAffectedItemId(parentId);
    }

    //----------------------------------------------------------< Operation >---
    /**
     *
     * @param visitor
     */
    public void accept(OperationVisitor visitor) throws AccessDeniedException, UnsupportedRepositoryOperationException, VersionException, RepositoryException {
        visitor.visit(this);
    }

    //----------------------------------------< Access Operation Parameters >---

    public ItemId getRemoveId() {
        return removeId;
    }

    public NodeId getParentId() {
        return parentId;
    }

    //------------------------------------------------------------< Factory >---
    public static Operation create(ItemState state) {
        Remove rm = new Remove(state.getId(), state.getParentState().getNodeId());
        return rm;
    }
}