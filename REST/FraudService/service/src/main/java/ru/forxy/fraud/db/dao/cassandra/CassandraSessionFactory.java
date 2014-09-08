package ru.forxy.fraud.db.dao.cassandra;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import org.springframework.beans.factory.InitializingBean;

import java.util.List;

/**
 * Custom cassandra session factory
 */
public class CassandraSessionFactory implements InitializingBean {

    private List<String> nodes;

    private String keyspace;

    private Cluster cluster;
    private Session session;

    private void connect() {
        if (session == null) {
            Cluster.Builder clusterBuilder = Cluster.builder();
            for (String node : nodes) {
                clusterBuilder.addContactPoint(node);
            }
            cluster = clusterBuilder.build();
            session = cluster.connect();
            /*session.execute("CREATE KEYSPACE IF NOT EXISTS "+ getKeyspace() +
                    " WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 3 }");*/
        }
    }

    public Session getSession() {
        return session;
    }

    public String getKeyspace() {
        return keyspace;
    }

    public void setNodes(List<String> nodes) {
        this.nodes = nodes;
    }

    public Cluster getCluster() {
        return cluster;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        connect();
    }
}