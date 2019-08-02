package com.wildbeeslabs.jentle.algorithms.inet;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author <a href="mailto:jesper.pedersen@ironjacamar.org">Jesper Pedersen</a>
 */
class HandlerServer implements Runnable {
    /**
     * The bind address
     */
    private String bindAddress;

    /**
     * The bind port
     */
    private int bindPort;

    /**
     * The handler class
     */
    private Class handler;

    /**
     * Is the server running ?
     */
    private AtomicBoolean running;

    /**
     * The server socket
     */
    private ServerSocket ss;

    /**
     * The executor service
     */
    private ExecutorService executorService;

    /**
     * Active sockets
     */
    private Set<HandlerSocket> active;

    /**
     * Constructor
     *
     * @param bindAddress The bind address
     * @param bindPort    The bind port
     * @param handler     The handler
     * @throws IOException Thrown if a server socket can not be created
     */
    public HandlerServer(String bindAddress, int bindPort, Class handler) throws IOException {
        this.bindAddress = bindAddress;
        this.bindPort = bindPort;
        this.handler = handler;
        this.running = new AtomicBoolean(false);
        this.ss = null;
        this.executorService = null;
        this.active = Collections.synchronizedSet(new HashSet<HandlerSocket>());
    }

    /**
     * Start
     *
     * @throws Throwable Thrown if an error occurs
     */
    public void start() throws Throwable {
        if (!running.get()) {
            InetSocketAddress address = new InetSocketAddress(bindAddress, bindPort);

            ss = new ServerSocket();
            ss.bind(address);

            executorService = Executors.newCachedThreadPool();

            running.set(true);
            executorService.submit(this);
        }
    }

    /**
     * Stop
     *
     * @throws Throwable Thrown if an error occurs
     */
    public void stop() throws Throwable {
        running.set(false);

        for (HandlerSocket hs : active) {
            hs.shutdown();
        }

        if (ss != null) {
            try {
                ss.close();
            } catch (IOException ioe) {
                // Ignore
            }
            ss = null;
        }

        if (executorService != null) {
            try {
                executorService.shutdown();
            } catch (Throwable t) {
                // Nothing to do
            }
            executorService = null;
        }
    }

    /**
     * Run
     */
    public void run() {
        while (running.get()) {
            try {
                Socket socket = ss.accept();

                HandlerSocket hs = new HandlerSocket(handler, socket, this);
                active.add(hs);
                executorService.submit(hs);
            } catch (Exception e) {
                if (running.get())
                    e.printStackTrace(System.err);
            }
        }
    }

    /**
     * Done
     *
     * @param hs The handlet socket
     */
    void done(HandlerSocket hs) {
        active.remove(hs);
    }
}
