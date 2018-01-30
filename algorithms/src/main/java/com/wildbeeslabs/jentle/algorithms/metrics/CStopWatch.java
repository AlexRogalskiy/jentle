/*
 * The MIT License
 *
 * Copyright 2018 WildBees Labs.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.wildbeeslabs.jentle.algorithms.metrics;

import java.text.NumberFormat;
import java.util.LinkedList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import org.apache.commons.lang3.StringUtils;

/**
 *
 * Custom stop watch algorithm implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString
public class CStopWatch {

    /**
     * Identifier of this stop watch
     */
    private final String id;
    /**
     * Task list keeping flag
     */
    private boolean keepTaskList = true;
    /**
     * List of TaskInfo objects
     */
    private final List taskList = new LinkedList();
    /**
     * Start time of the current task
     */
    private long startTimeMillis;
    /**
     * Is the stop watch currently running?
     */
    private boolean running;
    /**
     * Name of the current task
     */
    private String currentTaskName;
    /**
     * Last TaskInfo instance
     */
    private CTaskInfo lastTaskInfo;
    /**
     * Total tasks count performed
     */
    private int taskCount;
    /**
     * Total task running time (in milliseconds)
     */
    private long totalTimeMillis;

    /**
     * Construct a new stop watch.
     */
    public CStopWatch() {
        this(StringUtils.EMPTY);
    }

    /**
     * Constructs a new stop watch with the given id.
     *
     * @param id identifier for this stop watch. Handy when we have output from
     * multiple stop watches and need to distinguish between them.
     */
    public CStopWatch(final String id) {
        this.id = id;
    }

    /**
     * Determines whether the CTaskInfo array is built over time. Set this to
     * "false" when using a StopWatch for millions of intervals, or the task
     * info structure will consume excessive memory (default is "true").
     *
     * @param keepTaskList task list keeping flag
     */
    public void setKeepTaskList(boolean keepTaskList) {
        this.keepTaskList = keepTaskList;
    }

    /**
     * Starts an unnamed task. The results are undefined if {@link #stop()} or
     * timing methods are called without invoking this method.
     *
     * @see #stop()
     */
    public void start() throws IllegalStateException {
        start(StringUtils.EMPTY);
    }

    /**
     * Starts a named task. The results are undefined if {@link #stop()} or
     * timing methods are called without invoking this method.
     *
     * @param taskName the name of the task to start
     * @see #stop()
     */
    public void start(final String taskName) throws IllegalStateException {
        if (this.running) {
            throw new IllegalStateException("Can't start StopWatch: it's already running");
        }
        this.startTimeMillis = System.currentTimeMillis();
        this.running = true;
        this.currentTaskName = taskName;
    }

    /**
     * Stops the current task. The results are undefined if timing methods are
     * called without invoking at least one pair
     * {@link #start()} / {@link #stop()} methods.
     *
     */
    public void stop() throws IllegalStateException {
        if (!this.running) {
            throw new IllegalStateException("Can't stop StopWatch: it's not running");
        }
        long lastTime = System.currentTimeMillis() - this.startTimeMillis;
        this.totalTimeMillis += lastTime;
        this.lastTaskInfo = new CTaskInfo(this.currentTaskName, lastTime);
        if (this.keepTaskList) {
            this.taskList.add(this.lastTaskInfo);
        }
        ++this.taskCount;
        this.running = false;
        this.currentTaskName = null;
    }

    /**
     * Returns whether the stop watch is currently running.
     *
     * @return current stopwatch state (true - is running, false - otherwise)
     */
    public boolean isRunning() {
        return this.running;
    }

    /**
     * Returns the time taken by the last task.
     *
     * @return time taken by the last task performed
     */
    public long getLastTaskTimeMillis() throws IllegalStateException {
        if (this.lastTaskInfo == null) {
            throw new IllegalStateException("No tests run: can't get last interval");
        }
        return this.lastTaskInfo.getTimeMillis();
    }

    /**
     * Returns the total time in milliseconds for all tasks.
     *
     * @return total time in milliseconds for all tasks
     */
    public long getTotalTimeMillis() {
        return this.totalTimeMillis;
    }

    /**
     * Returns the total time in seconds for all tasks.
     *
     * @return total time in seconds for all tasks
     */
    public double getTotalTimeSeconds() {
        return totalTimeMillis / 1000.0;
    }

    /**
     * Returns the number of tasks timed.
     *
     * @return number of tasks performed
     */
    public int getTaskCount() {
        return this.taskCount;
    }

    /**
     * Returns an array of the data of tasks performed.
     *
     * @return array of tasks performed
     */
    public CTaskInfo[] getTaskInfo() {
        if (!this.keepTaskList) {
            throw new UnsupportedOperationException("Task info is not being kept!");
        }
        return (CTaskInfo[]) this.taskList.toArray(new CTaskInfo[this.taskList.size()]);
    }

    /**
     * Returns a short description of the total running time.
     *
     * @return description summary
     */
    public String summary() {
        return String.format("(%s) id=%s, running time (in millis)=%s", this.getClass().getName(), this.id, this.getTotalTimeMillis());
    }

    /**
     * Returns a string with a table describing all tasks performed.
     *
     * @return summary table of all tasks performed
     */
    public String print() {
        final StringBuffer sb = new StringBuffer(this.summary());
        sb.append('\n');
        if (!this.keepTaskList) {
            sb.append("No task info kept");
        } else {
            final CTaskInfo[] tasks = getTaskInfo();
            sb.append("-----------------------------------------\n");
            sb.append("ms     %     Task name\n");
            sb.append("-----------------------------------------\n");

            final NumberFormat nf = NumberFormat.getNumberInstance();
            nf.setMinimumIntegerDigits(5);
            nf.setGroupingUsed(false);

            final NumberFormat pf = NumberFormat.getPercentInstance();
            pf.setMinimumIntegerDigits(3);
            pf.setGroupingUsed(false);

            for (CTaskInfo task : tasks) {
                sb.append(nf.format(task.getTimeMillis())).append("  ");
                sb.append(pf.format(task.getTimeSeconds() / getTotalTimeSeconds())).append("  ");
                sb.append(task.getTaskName()).append("\n");
            }
        }
        return sb.toString();
    }

    /**
     * Return an informative string describing all tasks performed For custom
     * reporting, call <code>getTaskInfo()</code> and use the task info
     * directly.
     *
     * @return format information on tasks performance
     */
    public String toFormatString() {
        final StringBuffer sb = new StringBuffer(this.summary());
        if (this.keepTaskList) {
            final CTaskInfo[] tasks = getTaskInfo();
            for (final CTaskInfo task : tasks) {
                sb.append("; [").append(task.getTaskName()).append("] took ").append(task.getTimeMillis());
                long percent = Math.round((100.0 * task.getTimeSeconds()) / getTotalTimeSeconds());
                sb.append(" = ").append(percent).append("%");
            }
        } else {
            sb.append("no task info kept");
        }
        return sb.toString();
    }

    /**
     * Inner class to store data on task execution.
     */
    @Data
    @EqualsAndHashCode(callSuper = false)
    @ToString
    public static class CTaskInfo {

        private final String taskName;
        private final long timeMillis;

        private CTaskInfo(final String taskName, long timeMillis) {
            this.taskName = taskName;
            this.timeMillis = timeMillis;
        }

        /**
         * Returns the time in seconds of the current task.
         *
         * @return time in seconds of the current task
         */
        public double getTimeSeconds() {
            return this.getTimeMillis() / 1000.0;
        }
    }
}
