package camel.jmstofile;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.impl.DefaultCamelContext;

import javax.jms.ConnectionFactory;

public class JmsToFileExample {

    public static void main(String args[]) throws Exception {
        CamelContext context = new DefaultCamelContext();

        // connect to embedded ActiveMQ JMS broker
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vm://localhost");
        // make sure activemq is started ./activemq start
        // add the broker as a component
        context.addComponent("jms", JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));

        // add our route to the CamelContext
        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() {
                // connect to message queue and then copy he messages as files to data directory
                from("activemq:queue:incomingOrders")
                        .to("file:data"); // connected to activeMQ and put message into it
            }
        });

        context.start();
        Thread.sleep(10000); // need sleep to keep JVM running until the job is done
        context.stop();
    }

}
