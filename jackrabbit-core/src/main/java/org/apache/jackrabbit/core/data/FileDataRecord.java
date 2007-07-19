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
package org.apache.jackrabbit.core.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * Data record that is based on a normal file.
 */
class FileDataRecord extends AbstractDataRecord {

    /**
     * The file that contains the binary stream.
     */
    private final File file;

    /**
     * Creates a data record based on the given identifier and file.
     *
     * @param identifier data identifier
     * @param file file that contains the binary stream
     */
    public FileDataRecord(DataIdentifier identifier, File file) {
        super(identifier);
        assert file.isFile();
        this.file = file;
    }

    /**
     * Returns the length of the file.
     *
     * @return file length
     */
    public long getLength() {
        return file.length();
    }

    /**
     * Returns an input stream for reading the file.
     *
     * @return file input stream
     * @throws IOException if the file could not be opened
     */
    public InputStream getStream() throws IOException {
        return new FileInputStream(file);
    }

}
