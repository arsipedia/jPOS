package org.jpos.util;

import org.junit.jupiter.api.Test;

import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

public class BaseLogEventWriterTest {

    @Test
    void testShouldClosePrintStreamOnClose() {
        LogEventWriter writer = new TestLogEventWriter();
        PrintStream p = mock(PrintStream.class);
        writer.setPrintStream(p);
        writer.close();
        verify(p).close();
    }

    @Test
    void testShouldNotAttemptCloseOnNullPrintStream() {
        LogEventWriter writer = new TestLogEventWriter();
        writer.setPrintStream(null);
        assertDoesNotThrow(writer::close);
    }

    @Test
    void testShouldSetPrintStreamNullOnClose() {
        BaseLogEventWriter writer = new BaseLogEventWriter() {
            @Override
            public synchronized void close() {
                super.close();
            }
        };
        writer.setPrintStream(new PrintStream(System.out));
        writer.close();
        assertNull(writer.p);
    }

    @Test
    void testShouldCallDumpOnLogEventAndFlushPrintStream() {
        LogEventWriter writer = new TestLogEventWriter();
        PrintStream p = mock(PrintStream.class);
        writer.setPrintStream(p);
        LogEvent ev = mock(LogEvent.class);
        writer.write(ev);
        verify(ev).dump(any(), anyString());
        verify(p).flush();
    }

    @Test
    void testShouldNotThrowExceptionOnWriteIfPrintStreamOrLogEventIsNull() {
        LogEventWriter writer = new TestLogEventWriter();
        writer.setPrintStream(null);
        assertDoesNotThrow(() -> writer.write(new LogEvent()));
        writer.setPrintStream(new PrintStream(System.out));
        assertDoesNotThrow(() -> writer.write(null));
    }

    static class TestLogEventWriter extends BaseLogEventWriter { }
}
