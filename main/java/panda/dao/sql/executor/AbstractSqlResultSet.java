package panda.dao.sql.executor;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;

import panda.dao.sql.SqlExecutor;
import panda.dao.sql.SqlResultSet;
import panda.dao.sql.SqlUtils;

/**
 * AbstractSqlResultSet
 * @author yf.frank.wang@gmail.com
 */
public abstract class AbstractSqlResultSet implements SqlResultSet {

	protected SqlExecutor executor;
	protected ResultSet resultSet;

	/**
	 * Constructor
	 * 
	 * @param executor the sql executor
	 * @param resultSet the Result
	 */
	protected AbstractSqlResultSet(SqlExecutor executor, ResultSet resultSet) {
		this.executor = executor;
		this.resultSet = resultSet;
	}

	/**
	 * @return the resultSet
	 */
	public ResultSet getResultSet() {
		return resultSet;
	}

	// ---------------------------------------------------------------------
	// Traversal/Positioning
	// ---------------------------------------------------------------------
	/**
	 * Retrieves whether the cursor is before the first row in this <code>ResultSet</code> object.
	 * 
	 * @return <code>true</code> if the cursor is before the first row; <code>false</code> if the
	 *         cursor is at any other position or the result set contains no rows
	 * @exception SQLException if a database access error occurs
	 */
	public boolean isBeforeFirst() throws SQLException {
		return resultSet.isBeforeFirst();
	}

	/**
	 * Retrieves whether the cursor is after the last row in this <code>ResultSet</code> object.
	 * 
	 * @return <code>true</code> if the cursor is after the last row; <code>false</code> if the
	 *         cursor is at any other position or the result set contains no rows
	 * @exception SQLException if a database access error occurs
	 */
	public boolean isAfterLast() throws SQLException {
		return resultSet.isAfterLast();
	}

	/**
	 * Retrieves whether the cursor is on the first row of this <code>ResultSet</code> object.
	 * 
	 * @return <code>true</code> if the cursor is on the first row; <code>false</code> otherwise
	 * @exception SQLException if a database access error occurs
	 */
	public boolean isFirst() throws SQLException {
		return resultSet.isFirst();
	}

	/**
	 * Retrieves whether the cursor is on the last row of this <code>ResultSet</code> object. Note:
	 * Calling the method <code>isLast</code> may be expensive because the JDBC driver might need to
	 * fetch ahead one row in order to determine whether the current row is the last row in the
	 * result set.
	 * 
	 * @return <code>true</code> if the cursor is on the last row; <code>false</code> otherwise
	 * @exception SQLException if a database access error occurs
	 */
	public boolean isLast() throws SQLException {
		return resultSet.isLast();
	}

	/**
	 * Moves the cursor to the front of this <code>ResultSet</code> object, just before the first
	 * row. This method has no effect if the result set contains no rows.
	 * 
	 * @exception SQLException if a database access error occurs or the result set type is
	 *                <code>TYPE_FORWARD_ONLY</code>
	 */
	public void beforeFirst() throws SQLException {
		resultSet.beforeFirst();
	}

	/**
	 * Moves the cursor to the end of this <code>ResultSet</code> object, just after the last row.
	 * This method has no effect if the result set contains no rows.
	 * 
	 * @exception SQLException if a database access error occurs or the result set type is
	 *                <code>TYPE_FORWARD_ONLY</code>
	 */
	public void afterLast() throws SQLException {
		resultSet.afterLast();
	}

	/**
	 * Moves the cursor to the first row in this <code>ResultSet</code> object.
	 * 
	 * @return <code>true</code> if the cursor is on a valid row; <code>false</code> if there are no
	 *         rows in the result set
	 * @exception SQLException if a database access error occurs or the result set type is
	 *                <code>TYPE_FORWARD_ONLY</code>
	 */
	public boolean first() throws SQLException {
		return resultSet.first();
	}

	/**
	 * Moves the cursor to the last row in this <code>ResultSet</code> object.
	 * 
	 * @return <code>true</code> if the cursor is on a valid row; <code>false</code> if there are no
	 *         rows in the result set
	 * @exception SQLException if a database access error occurs or the result set type is
	 *                <code>TYPE_FORWARD_ONLY</code>
	 */
	public boolean last() throws SQLException {
		return resultSet.last();
	}

	/**
	 * Retrieves the current row number. The first row is number 1, the second number 2, and so on.
	 * 
	 * @return the current row number; <code>0</code> if there is no current row
	 * @exception SQLException if a database access error occurs
	 */
	public int getRow() throws SQLException {
		return resultSet.getRow();
	}

	/**
	 * Moves the cursor to the given row number in this <code>ResultSet</code> object.
	 * 
	 * <p>
	 * If the row number is positive, the cursor moves to the given row number with respect to the
	 * beginning of the result set. The first row is row 1, the second is row 2, and so on.
	 * 
	 * <p>
	 * If the given row number is negative, the cursor moves to an absolute row position with
	 * respect to the end of the result set. For example, calling the method
	 * <code>absolute(-1)</code> positions the cursor on the last row; calling the method
	 * <code>absolute(-2)</code> moves the cursor to the next-to-last row, and so on.
	 * 
	 * <p>
	 * An attempt to position the cursor beyond the first/last row in the result set leaves the
	 * cursor before the first row or after the last row.
	 * 
	 * <p>
	 * <B>Note:</B> Calling <code>absolute(1)</code> is the same as calling <code>first()</code>.
	 * Calling <code>absolute(-1)</code> is the same as calling <code>last()</code>.
	 * 
	 * @param row the number of the row to which the cursor should move. A positive number indicates
	 *            the row number counting from the beginning of the result set; a negative number
	 *            indicates the row number counting from the end of the result set
	 * @return <code>true</code> if the cursor is on the result set; <code>false</code> otherwise
	 * @exception SQLException if a database access error occurs, or the result set type is
	 *                <code>TYPE_FORWARD_ONLY</code>
	 */
	public boolean absolute(int row) throws SQLException {
		return resultSet.absolute(row);
	}

	/**
	 * Moves the cursor a relative number of rows, either positive or negative. Attempting to move
	 * beyond the first/last row in the result set positions the cursor before/after the the
	 * first/last row. Calling <code>relative(0)</code> is valid, but does not change the cursor
	 * position.
	 * 
	 * <p>
	 * Note: Calling the method <code>relative(1)</code> is identical to calling the method
	 * <code>next()</code> and calling the method <code>relative(-1)</code> is identical to calling
	 * the method <code>previous()</code>.
	 * 
	 * @param rows an <code>int</code> specifying the number of rows to move from the current row; a
	 *            positive number moves the cursor forward; a negative number moves the cursor
	 *            backward
	 * @return <code>true</code> if the cursor is on a row; <code>false</code> otherwise
	 * @exception SQLException if a database access error occurs, there is no current row, or the
	 *                result set type is <code>TYPE_FORWARD_ONLY</code>
	 */
	public boolean relative(int rows) throws SQLException {
		return resultSet.relative(rows);
	}

	/**
	 * Moves the cursor to the previous row in this <code>ResultSet</code> object.
	 * 
	 * @return <code>true</code> if the cursor is on a valid row; <code>false</code> if it is off
	 *         the result set
	 * @exception SQLException if a database access error occurs or the result set type is
	 *                <code>TYPE_FORWARD_ONLY</code>
	 */
	public boolean previous() throws SQLException {
		return resultSet.previous();
	}

	/**
	 * Moves the cursor down one row from its current position. A <code>ResultSet</code> cursor is
	 * initially positioned before the first row; the first call to the method <code>next</code>
	 * makes the first row the current row; the second call makes the second row the current row,
	 * and so on.
	 * 
	 * <P>
	 * If an input stream is open for the current row, a call to the method <code>next</code> will
	 * implicitly close it. A <code>ResultSet</code> object's warning chain is cleared when a new
	 * row is read.
	 * 
	 * @return <code>true</code> if the new current row is valid; <code>false</code> if there are no
	 *         more rows
	 * @exception SQLException if a database access error occurs
	 */
	public boolean next() throws SQLException {
		return resultSet.next();
	}

	// ---------------------------------------------------------------------
	// Row Updates
	// ---------------------------------------------------------------------
	/**
	 * Retrieves whether the current row has been updated. The value returned depends on whether or
	 * not the result set can detect updates.
	 * 
	 * @return <code>true</code> if both (1) the row has been visibly updated by the owner or
	 *         another and (2) updates are detected
	 * @exception SQLException if a database access error occurs
	 * @see DatabaseMetaData#updatesAreDetected
	 */
	public boolean rowUpdated() throws SQLException {
		return resultSet.rowUpdated();
	}

	/**
	 * Retrieves whether the current row has had an insertion. The value returned depends on whether
	 * or not this <code>ResultSet</code> object can detect visible inserts.
	 * 
	 * @return <code>true</code> if a row has had an insertion and insertions are detected;
	 *         <code>false</code> otherwise
	 * @exception SQLException if a database access error occurs
	 * 
	 * @see DatabaseMetaData#insertsAreDetected
	 */
	public boolean rowInserted() throws SQLException {
		return resultSet.rowInserted();
	}

	/**
	 * Retrieves whether a row has been deleted. A deleted row may leave a visible "hole" in a
	 * result set. This method can be used to detect holes in a result set. The value returned
	 * depends on whether or not this <code>ResultSet</code> object can detect deletions.
	 * 
	 * @return <code>true</code> if a row was deleted and deletions are detected; <code>false</code>
	 *         otherwise
	 * @exception SQLException if a database access error occurs
	 * 
	 * @see DatabaseMetaData#deletesAreDetected
	 */
	public boolean rowDeleted() throws SQLException {
		return resultSet.rowDeleted();
	}

	/**
	 * Inserts the contents of the insert row into this <code>ResultSet</code> object and into the
	 * database. The cursor must be on the insert row when this method is called.
	 * 
	 * @exception SQLException if a database access error occurs, if this method is called when the
	 *                cursor is not on the insert row, or if not all of non-nullable columns in the
	 *                insert row have been given a value
	 */
	public void insertRow() throws SQLException {
		resultSet.insertRow();
	}

	/**
	 * Updates the underlying database with the new contents of the current row of this
	 * <code>ResultSet</code> object. This method cannot be called when the cursor is on the insert
	 * row.
	 * 
	 * @exception SQLException if a database access error occurs or if this method is called when
	 *                the cursor is on the insert row
	 */
	public void updateRow() throws SQLException {
		resultSet.updateRow();
	}

	/**
	 * Deletes the current row from this <code>ResultSet</code> object and from the underlying
	 * database. This method cannot be called when the cursor is on the insert row.
	 * 
	 * @exception SQLException if a database access error occurs or if this method is called when
	 *                the cursor is on the insert row
	 */
	public void deleteRow() throws SQLException {
		resultSet.deleteRow();
	}

	/**
	 * Refreshes the current row with its most recent value in the database. This method cannot be
	 * called when the cursor is on the insert row.
	 * 
	 * <P>
	 * The <code>refreshRow</code> method provides a way for an application to explicitly tell the
	 * JDBC driver to refetch a row(s) from the database. An application may want to call
	 * <code>refreshRow</code> when caching or prefetching is being done by the JDBC driver to fetch
	 * the latest value of a row from the database. The JDBC driver may actually refresh multiple
	 * rows at once if the fetch size is greater than one.
	 * 
	 * <P>
	 * All values are refetched subject to the transaction isolation level and cursor sensitivity.
	 * If <code>refreshRow</code> is called after calling an updater method, but before calling the
	 * method <code>updateRow</code>, then the updates made to the row are lost. Calling the method
	 * <code>refreshRow</code> frequently will likely slow performance.
	 * 
	 * @exception SQLException if a database access error occurs or if this method is called when
	 *                the cursor is on the insert row
	 */
	public void refreshRow() throws SQLException {
		resultSet.refreshRow();
	}

	/**
	 * Cancels the updates made to the current row in this <code>ResultSet</code> object. This
	 * method may be called after calling an updater method(s) and before calling the method
	 * <code>updateRow</code> to roll back the updates made to a row. If no updates have been made
	 * or <code>updateRow</code> has already been called, this method has no effect.
	 * 
	 * @exception SQLException if a database access error occurs or if this method is called when
	 *                the cursor is on the insert row
	 */
	public void cancelRowUpdates() throws SQLException {
		resultSet.cancelRowUpdates();
	}

	/**
	 * Moves the cursor to the insert row. The current cursor position is remembered while the
	 * cursor is positioned on the insert row.
	 * 
	 * The insert row is a special row associated with an updatable result set. It is essentially a
	 * buffer where a new row may be constructed by calling the updater methods prior to inserting
	 * the row into the result set.
	 * 
	 * Only the updater, getter, and <code>insertRow</code> methods may be called when the cursor is
	 * on the insert row. All of the columns in a result set must be given a value each time this
	 * method is called before calling <code>insertRow</code>. An updater method must be called
	 * before a getter method can be called on a column value.
	 * 
	 * @exception SQLException if a database access error occurs or the result set is not updatable
	 */
	public void moveToInsertRow() throws SQLException {
		resultSet.moveToInsertRow();
	}

	/**
	 * Moves the cursor to the remembered cursor position, usually the current row. This method has
	 * no effect if the cursor is not on the insert row.
	 * 
	 * @exception SQLException if a database access error occurs or the result set is not updatable
	 */
	public void moveToCurrentRow() throws SQLException {
		resultSet.moveToCurrentRow();
	}

	// ---------------------------------------------------------------------
	// Properties
	// ---------------------------------------------------------------------
	/**
	 * Gives a hint as to the direction in which the rows in this <code>ResultSet</code> object will
	 * be processed. The initial value is determined by the <code>Statement</code> object that
	 * produced this <code>ResultSet</code> object. The fetch direction may be changed at any time.
	 * 
	 * @param direction an <code>int</code> specifying the suggested fetch direction; one of
	 *            <code>ResultSet.FETCH_FORWARD</code>, <code>ResultSet.FETCH_REVERSE</code>, or
	 *            <code>ResultSet.FETCH_UNKNOWN</code>
	 * @exception SQLException if a database access error occurs or the result set type is
	 *                <code>TYPE_FORWARD_ONLY</code> and the fetch direction is not
	 *                <code>FETCH_FORWARD</code>
	 * @see #getFetchDirection
	 */
	public void setFetchDirection(int direction) throws SQLException {
		resultSet.setFetchDirection(direction);
	}

	/**
	 * Retrieves the fetch direction for this <code>ResultSet</code> object.
	 * 
	 * @return the current fetch direction for this <code>ResultSet</code> object
	 * @exception SQLException if a database access error occurs
	 * @see #setFetchDirection
	 */
	public int getFetchDirection() throws SQLException {
		return resultSet.getFetchDirection();
	}

	/**
	 * Gives the JDBC driver a hint as to the number of rows that should be fetched from the
	 * database when more rows are needed for this <code>ResultSet</code> object. If the fetch size
	 * specified is zero, the JDBC driver ignores the value and is free to make its own best guess
	 * as to what the fetch size should be. The default value is set by the <code>Statement</code>
	 * object that created the result set. The fetch size may be changed at any time.
	 * 
	 * @param rows the number of rows to fetch
	 * @exception SQLException if a database access error occurs or the condition
	 *                <code>0 <= rows <= Statement.getMaxRows()</code> is not satisfied
	 * @see #getFetchSize
	 */
	public void setFetchSize(int rows) throws SQLException {
		resultSet.setFetchSize(rows);
	}

	/**
	 * Retrieves the fetch size for this <code>ResultSet</code> object.
	 * 
	 * @return the current fetch size for this <code>ResultSet</code> object
	 * @exception SQLException if a database access error occurs
	 * @see #setFetchSize
	 */
	public int getFetchSize() throws SQLException {
		return resultSet.getFetchSize();
	}

	/**
	 * Retrieves the type of this <code>ResultSet</code> object. The type is determined by the
	 * <code>Statement</code> object that created the result set.
	 * 
	 * @return <code>ResultSet.TYPE_FORWARD_ONLY</code>,
	 *         <code>ResultSet.TYPE_SCROLL_INSENSITIVE</code>, or
	 *         <code>ResultSet.TYPE_SCROLL_SENSITIVE</code>
	 * @exception SQLException if a database access error occurs
	 */
	public int getType() throws SQLException {
		return resultSet.getType();
	}

	/**
	 * Retrieves the concurrency mode of this <code>ResultSet</code> object. The concurrency used is
	 * determined by the <code>Statement</code> object that created the result set.
	 * 
	 * @return the concurrency type, either <code>ResultSet.CONCUR_READ_ONLY</code> or
	 *         <code>ResultSet.CONCUR_UPDATABLE</code>
	 * @exception SQLException if a database access error occurs
	 */
	public int getConcurrency() throws SQLException {
		return resultSet.getConcurrency();
	}

	/**
	 * Retrieves the first warning reported by calls on this <code>ResultSet</code> object.
	 * Subsequent warnings on this <code>ResultSet</code> object will be chained to the
	 * <code>SQLWarning</code> object that this method returns.
	 * 
	 * <P>
	 * The warning chain is automatically cleared each time a new row is read. This method may not
	 * be called on a <code>ResultSet</code> object that has been closed; doing so will cause an
	 * <code>SQLException</code> to be thrown.
	 * <P>
	 * <B>Note:</B> This warning chain only covers warnings caused by <code>ResultSet</code>
	 * methods. Any warning caused by <code>Statement</code> methods (such as reading OUT
	 * parameters) will be chained on the <code>Statement</code> object.
	 * 
	 * @return the first <code>SQLWarning</code> object reported or <code>null</code> if there are
	 *         none
	 * @exception SQLException if a database access error occurs or this method is called on a
	 *                closed result set
	 */
	public SQLWarning getWarnings() throws SQLException {
		return resultSet.getWarnings();
	}

	/**
	 * Clears all warnings reported on this <code>ResultSet</code> object. After this method is
	 * called, the method <code>getWarnings</code> returns <code>null</code> until a new warning is
	 * reported for this <code>ResultSet</code> object.
	 * 
	 * @exception SQLException if a database access error occurs
	 */
	public void clearWarnings() throws SQLException {
		resultSet.clearWarnings();
	}

	/**
	 * Retrieves the name of the SQL cursor used by this <code>ResultSet</code> object.
	 * 
	 * <P>
	 * In SQL, a result table is retrieved through a cursor that is named. The current row of a
	 * result set can be updated or deleted using a positioned update/delete statement that
	 * references the cursor name. To insure that the cursor has the proper isolation level to
	 * support update, the cursor's <code>SELECT</code> statement should be of the form
	 * <code>SELECT FOR UPDATE</code>. If <code>FOR UPDATE</code> is omitted, the positioned updates
	 * may fail.
	 * 
	 * <P>
	 * The JDBC API supports this SQL feature by providing the name of the SQL cursor used by a
	 * <code>ResultSet</code> object. The current row of a <code>ResultSet</code> object is also the
	 * current row of this SQL cursor.
	 * 
	 * <P>
	 * <B>Note:</B> If positioned update is not supported, a <code>SQLException</code> is thrown.
	 * 
	 * @return the SQL name for this <code>ResultSet</code> object's cursor
	 * @exception SQLException if a database access error occurs
	 */
	public String getCursorName() throws SQLException {
		return resultSet.getCursorName();
	}

	/**
	 * Retrieves the number, types and properties of this <code>ResultSet</code> object's columns.
	 * 
	 * @return the description of this <code>ResultSet</code> object's columns
	 * @exception SQLException if a database access error occurs
	 */
	public ResultSetMetaData getMetaData() throws SQLException {
		return resultSet.getMetaData();
	}

	/**
	 * Releases this <code>ResultSet</code> object's database and JDBC resources immediately instead
	 * of waiting for this to happen when it is automatically closed.
	 * 
	 * <P>
	 * <B>Note:</B> A <code>ResultSet</code> object is automatically closed by the
	 * <code>Statement</code> object that generated it when that <code>Statement</code> object is
	 * closed, re-executed, or is used to retrieve the next result from a sequence of multiple
	 * results. A <code>ResultSet</code> object is also automatically closed when it is garbage
	 * collected.
	 * 
	 * @exception SQLException if a database access error occurs
	 */
	public void close() throws SQLException {
		Statement st = resultSet.getStatement();
		resultSet.close();
		st.close();
	}

	/**
	 * safe close the <code>ResultSet</code> and <code>Statement</code>
	 */
	public void safeClose() {
		Statement st = null;
		try {
			st = resultSet.getStatement();
		}
		catch (SQLException e) {
		}
		finally {
			SqlUtils.safeClose(resultSet, st);
		}
	}
}
