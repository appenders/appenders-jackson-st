package com.fasterxml.jackson.core.json;

import org.appenders.st.jackson.ReusableUTF8JsonGenerator;

public class UTF8JsonGeneratorAccess {

    public static int outputTail(ReusableUTF8JsonGenerator gen) {
        return gen._outputTail;
    }

    public static byte[] outputBuffer(ReusableUTF8JsonGenerator gen) {
        return gen._outputBuffer;
    }
}
