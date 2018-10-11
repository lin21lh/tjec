
package com.freework.freedbm.cfg.id;

import java.sql.Connection;
import java.sql.SQLException;

import java.io.Serializable;

import com.freework.base.exception.AppException;
import com.freework.freedbm.DTO;

/**
 * The general contract between a class that generates unique
 * identifiers and the <tt>Session</tt>. It is not intended that
 * this interface ever be exposed to the application. It <b>is</b>
 * intended that users implement this interface to provide
 * custom identifier generation strategies.<br>
 * <br>
 * Implementors should provide a public default constructor.<br>
 * <br>
 * Implementations that accept configuration parameters should
 * also implement <tt>Configurable</tt>.
 * <br>
 * Implementors <em>must</em> be threadsafe
 *
 * @author Gavin King
 * @see PersistentIdentifierGenerator
 * @see Configurable
 */
public interface IdentifierGenerator {

	/**
	 * Generate a new identifier.
	 * @param conn
	 * @param object the entity or toplevel collection for which the id is being generated
	 *
	 * @return a new identifier
	 * @throws AppException
	 */
	public Serializable generate(Connection conn, DTO object) 
	throws SQLException;

}
