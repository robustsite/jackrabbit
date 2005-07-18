/*
 * Copyright 2004-2005 The Apache Software Foundation or its licensors,
 *                     as applicable.
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
package org.apache.jackrabbit.chain.command;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.Writer;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Value;

import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.apache.jackrabbit.chain.ContextHelper;

/**
 * Dumps stored data from the current working node
 */
public class DumpToFile implements Command
{
    /** target file */
    private String file;

    public boolean execute(Context ctx) throws Exception
    {
        Writer w = new BufferedWriter(new FileWriter(file)) ;
        PrintWriter out = new PrintWriter(w) ;
        dump(out, ContextHelper.getCurrentNode(ctx));
        w.flush() ;
        w.close() ;
        return false;
    }

    public static void dump(PrintWriter out, Node n) throws RepositoryException
    {
        out.println(n.getPath()) ;
        PropertyIterator pit = n.getProperties();
        while (pit.hasNext())
        {
            Property p = pit.nextProperty();
            out.print(p.getPath() + "=");
            if (p.getDefinition().isMultiple())
            {
                Value[] values = p.getValues();
                for (int i = 0; i < values.length; i++)
                {
                    if (i > 0)
                        out.println(",");
                    out.println(values[i].getString());
                }
            } else
            {
                out.print(p.getString());
            }
            out.println();
        }
        NodeIterator nit = n.getNodes();
        while (nit.hasNext())
        {
            Node cn = nit.nextNode();
            dump(out, cn);
        }
    }

    public String getFile()
    {
        return file;
    }

    public void setFile(String file)
    {
        this.file = file;
    }
}
