package com.wildbeeslabs.jentle.algorithms.inet;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:jesper.pedersen@ironjacamar.org">Jesper Pedersen</a>
 */
public class EISServer
{
   /** The handler server */
   private static Map<String, HandlerServer> handlerServers =
      Collections.synchronizedMap(new HashMap<String, HandlerServer>());

   /* The handler class name */
   private String handlerClassName;

   /* The class loader */
   private ClassLoader classLoader;

   /* The host */
   private String host;

   /* The port */
   private int port;

   /* The control port */
   private int controlPort;

   /**
    * Constructor
    */
   public EISServer()
   {
      this.handlerClassName = null;
      this.classLoader = null;
      this.host = "localhost";
      this.port = 1400;
      this.controlPort = 1401;
   }

   /**
    * Set the handler class name
    * @param v The name
    */
   public void setHandlerClassName(String v)
   {
      this.handlerClassName = v;
   }

   /**
    * Set class loader
    * @param v The value
    */
   public void setClassLoader(ClassLoader v)
   {
      this.classLoader = v;
   }

   /**
    * Set the host name
    * @param v The name
    */
   public void setHost(String v)
   {
      this.host = v;
   }

   /**
    * Set the port
    * @param v The value
    */
   public void setPort(int v)
   {
      this.port = v;
   }

   /**
    * Startup
    * @exception Throwable Thrown if an error occurs
    */
   public void startup() throws Throwable
   {
      if (handlerClassName == null || handlerClassName.trim().equals(""))
         throw new IllegalStateException("HandlerClassName isn't defined");

      shutdown();

      ClassLoader cl = classLoader;

      if (cl == null)
         cl = EISServer.class.getClassLoader();

      Class handler = Class.forName(handlerClassName, true, cl);

      if (!Handler.class.isAssignableFrom(handler))
         throw new IllegalArgumentException("The specified handler class doesn't implement the interface");

      HandlerServer handlerServer = new HandlerServer(host, port, handler);
      handlerServer.start();

      handlerServers.put(getKey(), handlerServer);
   }

   /**
    * Shutdown
    * @exception Throwable Thrown if an error occurs
    */
   public void shutdown() throws Throwable
   {
      HandlerServer handlerServer = handlerServers.remove(getKey());

      if (handlerServer != null)
         handlerServer.stop();
   }

   /**
    * Get the key
    * @return The value
    */
   private String getKey()
   {
      return host + ":" + port;
   }
}
