package ru.forxy.fraud.db.dao.cassandra.client;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import org.springframework.beans.factory.InitializingBean;
import ru.forxy.fraud.rest.v1.Transaction;

import java.util.ArrayList;
import java.util.List;

/**
 * Cassandra client implementation for Transaction entity
 */
public class CassandraFraudClient implements ICassandraClient<Transaction>, InitializingBean {

    private PreparedStatement getByKeyStatement;
    private PreparedStatement getPageStatement;
    private PreparedStatement addStatement;
    private PreparedStatement deleteStatement;

    private Session session;

    @Override
    public List<Transaction> getAll() {
        List<Row> rows = getSession().execute("select * from transaction;").all();
        List<Transaction> transactions = new ArrayList<Transaction>(rows.size());
        for (final Row row : rows) {
            transactions.add(toTransaction(row));
        }
        return transactions;
    }

    @Override
    public List<Transaction> getPage(final long start, final long size) {
        return null;
    }

    @Override
    public Transaction getByKey(final Long key) {
        return toTransaction(getSession().execute(new BoundStatement(getByKeyStatement).bind(key)).one());
    }

    @Override
    public boolean existsKey(final Long key) {
        return getSession().execute(new BoundStatement(getByKeyStatement).bind(key)).one() != null;
    }

    @Override
    public boolean exists(final Transaction entity) {
        //return getSession().execute(new BoundStatement(getByKeyStatement).bind(entity.getEmail())).one() != null;
		return false;
    }

    @Override
    public void add(final Transaction transaction) {
        final BoundStatement newTransactionStatement = new BoundStatement(addStatement);
        // @formatter:off
        /*getSession().execute(newTransactionStatement.bind(
                transaction.getEmail(),
                ByteBuffer.wrap(transaction.getPassword()),
                transaction.getLogin(),
                transaction.getFirstName(),
                transaction.getLastName(),
                String.valueOf(transaction.getGender()),
                transaction.getBirthDate(),
                new Date()));*/
        // @formatter:on
    }

    @Override
    public void delete(final Transaction entity) {
        //getSession().execute(new BoundStatement(deleteStatement).bind(entity.getEmail()));
    }

    @Override
    public void deleteByKey(final Long key) {
        getSession().execute(new BoundStatement(deleteStatement).bind(key));
    }

    @Override
    public long count() {
        return getSession().execute("select count(*) from transaction;").one().getLong(0);
    }

    public Session getSession() {
        return session;
    }

    public void setSession(final Session session) {
        this.session = session;
    }

    private Transaction toTransaction(Row row) {
        final Transaction transaction = new Transaction();
        /*transaction.set(row.getString("login"));
        transaction.setGender(row.isNull("gender") ? null : row.getString("gender").charAt(0));
        transaction.setFirstName(row.getString("first_name"));
        transaction.setLastName(row.getString("last_name"));*/
        return transaction;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        getByKeyStatement = session.prepare("select * from transaction where id = ?;");
        addStatement = session.prepare(
                "insert into transaction (id, payload) " +
                        "values (?, ?);");
        deleteStatement = session.prepare("delete from transaction where id = ?;");
    }
}
