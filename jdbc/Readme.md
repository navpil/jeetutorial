# JDBC

Jdbc is not strictly Java EE, since it is included in Java SE, but it is often used in EE environments.

The you can read the [official JDBC tutorial](https://docs.oracle.com/javase/tutorial/jdbc/index.html)

But here is some basic stuff:

## Terminology

DataSource is a "pointer" to the DB.

Connections are what they are - physical connections to the DB. So if network is bad, connection may die.

Statements do the actual work - updating, creating etc.

They relate to each other: `DataSource.getConnection().createStatement()`

Connections and Statements are Autoclosables.

## Create connection

Connection can be created by:
 - Simple `DriverManager.getConnection`
 - Manual creation `ds = new JDBCDataSource()` and then `ds.getConnection`
 - DataSource taken from JNDI and then `ds.getConnection`

In order to create connection you need three things: 
 
 1) username
 2) password
 3) connection url
    
Each database has its own connection url, but usually they conform to the format:

    jdbc:<dbms-name>:<physical-location><database-name>

For example hsqldb in-memory with the database/schema named 'candies' will be
    
    jdbc:hsqldb:mem:candies
    
And postgres which is running on localhost can be

    jdbc:postgresql://localhost:12345/candies


## JNDI

JNDI is a separate topic, but because JNDI is very often mentioned in the context for JDBC I have some examples with it.

JNDI (among other things) allows to setup JDBC Data Source outside the code and then code will simply get the DataSource when it needs it.

(When there is a CDI example, probably JNDI will be mentioned there too).

## Connection pools

Creating connection for each request is slow and keeping the connection alive forever is error-prone. That's why you'd always better use some kind of connection pool. Comparison of connection pools [here](https://beansroasted.wordpress.com/2017/07/29/connection-pool-analysis/)
In case link is broken, here is a list:
    
 1) Apache Commons DBCP2
 2) C3P0
 3) Tomcat JDBC
 4) HikariCP 

Last two are best according to the given link.

## Statements

In order to do something with the DB you need to create statements out of connection.

    Statement s = connection.createStatement() 

executeQuery() will return some result list

    s.executeQuery("select * from users")
    
executeUpdate() will do something with DB and return number of affected rows (or 0 for DDL)

    s.executeUpdate("drop all tables");
    
execute() executes whatever. You may ask for results out of statement later
    
    if (s.execute("select * from users")) {
        s.getResultSet();
    } else {
        s.getUpdateCount();
    }

## Prepared statements

Not to manually construct sql strings, which is prone to SQL injections, you'd better use prepared statements.
They are used in `UsersStringDao.java` for deleting, reading and inserting

## ResultSet

Refer to `UserStringDao.java`

You may refer to columns by index or by name.

Result sets by default can only be read forward and without updates. 
But this can be overriden by setting ResultSet Type and ResultSet Concurrency.
Read more [here](https://docs.oracle.com/javase/tutorial/jdbc/basics/retrieving.html)

There is also a `RowSet` which provide more functionality than `ResultSet` and they can be:

 - JdbcRowSet
 - CachedRowSet
 - WebRowSet
 - JoinRowSet
 - FilteredRowSet
 
Refer to [oracle tutorial](https://docs.oracle.com/javase/tutorial/jdbc/basics/rowset.html) for description

## Transactions

Default transaction isolation level is set by DB. It can be one of the following (weakest first):

 - TRANSACTION_NONE
 - TRANSACTION_READ_UNCOMMITTED
 - TRANSACTION_READ_COMMITTED
 - TRANSACTION_REPEATABLE_READ
 - TRANSACTION_SERIALIZABLE

Can be changed by `connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE)`

By default connection has autocommit set to true. Which means that every execution is a separate transaction.

In order to manually control transaction you need to:

    try {
        connection.setAutoCommit(false);
        ... do some executions ...
        //Commit manually
        connection.commit();
    } catch (Exception e) {
        //Something went wrong
        connection.rollback();
    } finally {
        //revert to default behavior
        connection.setAutoCommit(true);
    }
    
There are such things as `Savepoint` which you can create out of connection and rollback to them:

    Savepoint save = connection.setSavepoint();
    ...
    connection.rollback(save);
 
## Other

JDBC can handle different types of advanced objects and also there is a special handling of big objects, such as CLOB.