import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;
import java.net.URI;
import java.net.URISyntaxException;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.LoggerFactory;

import javax.naming.Reference;
import java.net.URL;
import java.security.ProtectionDomain;
import java.util.Properties;
import javax.naming.NamingException;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.broker.TransportConnector;
import org.eclipse.jetty.plus.jndi.Resource;

public class JServer {

    /**
     * logger
     */
    final static org.slf4j.Logger logger = LoggerFactory.getLogger(JServer.class);

    static Server embed_server;

    public static void main(String[] args) throws Exception {
        int port = Integer.parseInt(System.getProperty("port", "8080"));
        embed_server = new Server(port);

        org.eclipse.jetty.webapp.Configuration.ClassList classlist = org.eclipse.jetty.webapp.Configuration.ClassList.setServerDefault(embed_server);
        // this line sets up the server to pick up JNDI related configurations in override-web.xml. This is a direct copy of the documentation.
        classlist.addAfter("org.eclipse.jetty.webapp.FragmentConfiguration", "org.eclipse.jetty.plus.webapp.EnvConfiguration", "org.eclipse.jetty.plus.webapp.PlusConfiguration");
        classlist.addBefore("org.eclipse.jetty.webapp.JettyWebXmlConfiguration", "org.eclipse.jetty.annotations.AnnotationConfiguration");

        ProtectionDomain domain = JServer.class.getProtectionDomain();
        URL location = domain.getCodeSource().getLocation();
        logger.debug("[custom] " + location.toExternalForm());
        WebAppContext webapp = new WebAppContext();
        webapp.setContextPath("/webapp");
        webapp.setLogUrlOnStart(true);
        webapp.setParentLoaderPriority(true);
        webapp.setWar(location.toExternalForm());
        webapp.setDescriptor("WEB-INF/web.xml");
        webapp.setOverrideDescriptor("WEB-INF/override-web.xml");
        webapp.prependServerClass("-org.eclipse.jetty.servlet.,-org.eclipse.jetty.server.");
        webapp.setServer(embed_server);
        embed_server.setHandler(webapp);
        new org.eclipse.jetty.plus.jndi.Resource(webapp, "BeanManager", new Reference("javax.enterprise.inject.spi.BeanManager", "org.jboss.weld.resources.ManagerObjectFactory", null));

        // setUpMySqlResource();
        // setUpEmbeddedJms();
        embed_server.start();
        embed_server.join();
    }

    private static void setUpMySqlResource() {
        /*
         //Register a reference to a mail service scoped to the webapp.
         //This must be linked to the webapp by an entry in web.xml:
         <resource-ref>
         <description>the dataSource thingy</description>
         <res-ref-name>jdbc/stuff</res-ref-name>
         <res-type>javax.sql.DataSource</res-type>
         <res-auth>Container</res-auth>
         </resource-ref>
         */
        // TODO: demonstrate usage ...
        MysqlConnectionPoolDataSource dataPool = new MysqlConnectionPoolDataSource();
        dataPool.setURL("jdbc:mysql://localhost:3306/myresource");
        dataPool.setUser("root");
        dataPool.setPassword("pass");
        try {
            new Resource(null, "jdbc/stuff", dataPool);
        } catch (NamingException ex) {
            logger.info("Someone registered this resource before, so it's expected to be available ...");
        }
    }
    
// TODO: demonstrate usage ...
    private static void setupEmail(WebAppContext webapp) throws NamingException {
        /*
         //Register a reference to a mail service scoped to the webapp.
         // if the webapp variable is null, the resource is server/jvm scoped (?)
         //This must be linked to the webapp by an entry in web.xml:
         <resource-ref>
         <res-ref-name>mail/Session</res-ref-name>
         <res-type>javax.mail.Session</res-type>
         <res-auth>Container</res-auth>
         </resource-ref>
         //At runtime the webapp accesses this as java:comp/env/mail/Session
         */
        org.eclipse.jetty.jndi.factories.MailSessionReference mailref = new org.eclipse.jetty.jndi.factories.MailSessionReference();
        mailref.setUser("CHANGE-ME");
        mailref.setPassword("CHANGE-ME");
        Properties props = new Properties();
        props.put("mail.smtp.auth", "false");
        props.put("mail.smtp.host", "CHANGE-ME");
        props.put("mail.from", "CHANGE-ME");
        props.put("mail.debug", "false");
        mailref.setProperties(props);
        org.eclipse.jetty.plus.jndi.Resource xxxmail = new org.eclipse.jetty.plus.jndi.Resource(webapp, "mail/Session", mailref);
    }

    // TODO: demonstrate usage ...
    private static void setUpEmbeddedJms() throws URISyntaxException, Exception {
        BrokerService broker = new BrokerService();
        TransportConnector connector = new TransportConnector();
        connector.setUri(new URI("tcp://localhost:61616"));
        broker.setBrokerName("SnitchBroker");
        broker.setUseJmx(true);
        broker.addConnector(connector);
        broker.start();
    }

    public static void stopServer() throws Exception {
        if (embed_server != null) {
            embed_server.stop();
            embed_server.join();
            embed_server.destroy();
            embed_server = null;
        }
    }

}
