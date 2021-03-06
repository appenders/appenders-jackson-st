package org.appenders.st.jackson;

/*-
 * #%L
 * appenders-jackson-st
 * %%
 * Copyright (C) 2020 Appenders Project
 * %%
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
 * #L%
 */

import com.fasterxml.jackson.core.io.DataOutputAsStream;

import java.io.DataOutput;
import java.io.IOException;

public class DataOutputAsStreamDelegate extends DataOutputAsStream {

    private DataOutput delegate;

    public DataOutputAsStreamDelegate(DataOutput dataOutput) {
        super(dataOutput);
        this.delegate = dataOutput;
    }

    /* visible for testing */
    DataOutput getDelegate() {
        return delegate;
    }

    public void setDelegate(DataOutput delegate) {
        this.delegate = delegate;
    }

    @Override
    public void write(int b) throws IOException {
        delegate.write(b);
    }

    @Override
    public void write(byte[] b) throws IOException {
        delegate.write(b);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        delegate.write(b, off, len);
    }

}
