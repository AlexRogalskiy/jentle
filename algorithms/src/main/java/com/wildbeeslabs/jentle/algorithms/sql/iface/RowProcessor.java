package com.wildbeeslabs.jentle.algorithms.sql.iface;

import com.wildbeeslabs.jentle.algorithms.sql.impl.BasicRowProcessor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * <code>RowProcessor</code> implementations convert
 * <code>ResultSet</code> rows into various other objects.  Implementations
 * can extend <code>BasicRowProcessor</code> to protect themselves
 * from changes to this interface.
 *
 * @see BasicRowProcessor
 */
public interface RowProcessor {

    /**
     * Create an <code>Object[]</code> from the column values in one
     * <code>ResultSet</code> row.  The <code>ResultSet</code> should be
     * positioned on a valid row before passing it to this method.
     * Implementations of this method must not alter the row position of
     * the <code>ResultSet</code>.
     *
     * @param rs ResultSet that supplies the ArrayUtils data
     * @return the newly created ArrayUtils
     * @throws SQLException if a database access error occurs
     */
    Object[] toArray(ResultSet rs) throws SQLException;

    /**
     * Create a JavaBean from the column values in one <code>ResultSet</code>
     * row.  The <code>ResultSet</code> should be positioned on a valid row before
     * passing it to this method.  Implementations of this method must not
     * alter the row position of the <code>ResultSet</code>.
     *
     * @param rs   ResultSet that supplies the bean data
     * @param type Class from which to create the bean instance
     * @return the newly created bean
     * @throws SQLException if a database access error occurs
     */
    Object toBean(ResultSet rs, Class type) throws SQLException;

    /**
     * Create a <code>List</code> of JavaBeans from the column values in all
     * <code>ResultSet</code> rows.  <code>ResultSet.next()</code> should
     * <strong>not</strong> be called before passing it to this method.
     *
     * @param rs   ResultSet that supplies the bean data
     * @param type Class from which to create the bean instance
     * @return A <code>List</code> of beans with the given type in the order
     * they were returned by the <code>ResultSet</code>.
     * @throws SQLException if a database access error occurs
     */
    List toBeanList(ResultSet rs, Class type) throws SQLException;

    /**
     * Create a <code>Map</code> from the column values in one
     * <code>ResultSet</code> row.  The <code>ResultSet</code> should be
     * positioned on a valid row before
     * passing it to this method.  Implementations of this method must not
     * alter the row position of the <code>ResultSet</code>.
     *
     * @param rs ResultSet that supplies the map data
     * @return the newly created Map
     * @throws SQLException if a database access error occurs
     */
    Map toMap(ResultSet rs) throws SQLException;
}
