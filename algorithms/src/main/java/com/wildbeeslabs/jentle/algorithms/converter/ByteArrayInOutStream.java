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
package com.wildbeeslabs.jentle.algorithms.converter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * This class extends the ByteArrayOutputStream by providing a method that
 * returns a new ByteArrayInputStream which uses the internal byte array buffer.
 * This buffer is not copied, so no additional memory is used. After creating
 * the ByteArrayInputStream the instance of the ByteArrayInOutStream can not be
 * used anymore.
 * <p>
 * The ByteArrayInputStream can be retrieved using
 * <code>getInputStream()</code>.
 */
public final class ByteArrayInOutStream extends ByteArrayOutputStream {

    /*
     ByteArrayInOutStream baios = new ByteArrayInOutStream();
     ByteArrayInputStream inputStream;
		
     try {
     baios.write(new byte[]{123});			
			
     // The internal buffer of the ByteArrayOutputStream is NOT copied
     // therefore the 'conversion' to a ByteArrayInputStream is fast
     inputStream = baios.getInputStream();
			
     byte[] tmp = new byte[1];
     inputStream.read(tmp);
			
     System.out.println(tmp[0]);
			
     // The streams should be closed in a finally block normally..
     baios.close();
     inputStream.close();
     } catch (IOException e) {
     e.printStackTrace();
     }
     */
    /**
     * Creates a new ByteArrayInOutStream. The buffer capacity is initially 32
     * bytes, though its size increases if necessary.
     */
    public ByteArrayInOutStream() {
        super();
    }

    /**
     * Creates a new ByteArrayInOutStream, with a buffer capacity of the
     * specified size, in bytes.
     *
     * @param size the initial size.
     * @exception IllegalArgumentException if size is negative.
     */
    public ByteArrayInOutStream(int size) {
        super(size);
    }

    /**
     * Creates a new ByteArrayInputStream that uses the internal byte array
     * buffer of this ByteArrayInOutStream instance as its buffer array. The
     * initial value of pos is set to zero and the initial value of count is the
     * number of bytes that can be read from the byte array. The buffer array is
     * not copied. This instance of ByteArrayInOutStream can not be used anymore
     * after calling this method.
     *
     * @return the ByteArrayInputStream instance
     */
    public ByteArrayInputStream getInputStream() {
        final ByteArrayInputStream in = new ByteArrayInputStream(this.buf, 0, this.count);
        // set the buffer of the ByteArrayOutputStream to null so it can't be altered anymore
        this.buf = null;
        return in;
    }
}
