package com.wildbeeslabs.jentle.algorithms.inet;

import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * Privileged Blocks
 * 
 * @author <a href="mailto:jesper.pedersen@ironjacamar.org">Jesper Pedersen</a>
 */
class SecurityActions
{
   /**
    * Get the classloader.
    * @param c The class
    * @return The classloader
    */
   static ClassLoader getClassLoader(final Class<?> c)
   {
      if (System.getSecurityManager() == null)
         return c.getClassLoader();

      return AccessController.doPrivileged(new PrivilegedAction<ClassLoader>()
      {
         public ClassLoader run()
         {
            return c.getClassLoader();
         }
      });
   }

   /**
    * Get the context classloader.
    * @return The classloader
    */
   static ClassLoader getThreadContextClassLoader()
   {
      if (System.getSecurityManager() == null)
         return Thread.currentThread().getContextClassLoader();

      return AccessController.doPrivileged(new PrivilegedAction<ClassLoader>()
      {
         public ClassLoader run()
         {
            return Thread.currentThread().getContextClassLoader();
         }
      });
   }

   /**
    * Set the context classloader.
    * @param cl classloader
    */
   static void setThreadContextClassLoader(final ClassLoader cl)
   {
      if (System.getSecurityManager() == null)
      {
         Thread.currentThread().setContextClassLoader(cl);
      }
      else
      {
         AccessController.doPrivileged(new PrivilegedAction<Object>()
         {
            public Object run()
            {
               Thread.currentThread().setContextClassLoader(cl);

               return null;
            }
         });
      }
   }
}
