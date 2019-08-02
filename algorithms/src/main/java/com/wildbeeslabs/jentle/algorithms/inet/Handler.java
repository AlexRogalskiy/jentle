package com.wildbeeslabs.jentle.algorithms.inet;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * This interface represents a session between a resource adapter
 * and an Enterprise Information System
 *
 * Once the <code>handle</code> method returns the socket where
 * the communication takes place is closed
 *
 * @author <a href="mailto:jesper.pedersen@ironjacamar.org">Jesper Pedersen</a>
 */
public interface Handler
{
   /**
    * Handle an interaction with a client
    * @param is The input stream
    * @param os The output stream
    */
   public void handle(InputStream is, OutputStream os);
}
