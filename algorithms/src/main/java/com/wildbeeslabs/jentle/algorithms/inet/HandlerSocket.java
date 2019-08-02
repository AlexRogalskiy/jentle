package com.wildbeeslabs.jentle.algorithms.inet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.net.Socket;

/**
 * @author <a href="mailto:jesper.pedersen@ironjacamar.org">Jesper Pedersen</a>
 */
class HandlerSocket implements Runnable
{
   /** The handler class */
   private Class handler;

   /** The socket */
   private Socket socket;

   /** The server */
   private HandlerServer server;

   /**
    * Constructor
    * @param handler The handler class
    * @param socket The socket
    * @param server The server
    */
   public HandlerSocket(Class handler, Socket socket, HandlerServer server)
   {
      this.handler = handler;
      this.socket = socket;
      this.server = server;
   }

   /**
    * Run
    */
   public void run()
   {
      ClassLoader oldCl = SecurityActions.getThreadContextClassLoader();
      try
      {
         SecurityActions.setThreadContextClassLoader(SecurityActions.getClassLoader(handler));

         InputStream is = socket.getInputStream();
         OutputStream os = socket.getOutputStream();

         Handler instance = createHandler();
         instance.handle(is, os);
      }
      catch (Throwable t)
      {
         t.printStackTrace(System.err);
      }
      finally
      {
         shutdown();
         SecurityActions.setThreadContextClassLoader(oldCl);
      }
   }

   /**
    * Shutdown
    */
   void shutdown()
   {
      server.done(this);

      try
      {
         if (socket != null)
            socket.close();
      }
      catch (IOException ignore)
      {
         // Ignore
      }
   }

   /**
    * Create handler
    * @exception Throwable Thrown in case of an error
    */
   @SuppressWarnings("unchecked")
   private Handler createHandler() throws Throwable
   {
      Class clz = handler;

      while (clz != Object.class)
      {
         try
         {
            Constructor c = clz.getDeclaredConstructor((Class[])null);
            c.setAccessible(true);

            return (Handler)c.newInstance((Object[])null);
         }
         catch (Throwable t)
         {
            t.printStackTrace(System.err);
         }

         clz = clz.getSuperclass();
      }

      throw new IllegalStateException("Unable to find default constructor");
   }
}
