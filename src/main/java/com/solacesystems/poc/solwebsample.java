package com.solacesystems.poc;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.Resource;
import javax.jms.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;

public class    solwebsample extends HttpServlet {
    private Log log = LogFactory.getLog(solwebsample.class);

    @Resource(name = "jms/ConnectionFactory")
    ConnectionFactory connectionFactory;

    @Resource(name = "jms/Queue")
    Queue queue;
    MessageConsumer queueConsumer;
    private int queueCount = 0;

    @Resource(name = "jms/Topic")
    Topic topic;
    MessageConsumer topicConsumer;
    private int topicCount = 0;

    private Connection conn;
    private Session sess;
    private String status;

    public void destroy() {
        log.info("Destroying solwebsample, closing all Solace consumers and connections.");
        try {
            if (topicConsumer != null)
                topicConsumer.close();
            if (queueConsumer != null)
                queueConsumer.close();
            if (conn != null) {
                conn.stop();
                conn.close();
            }
        }
        catch(JMSException jex) {
        }
    }

    public void init(ServletConfig conf) throws ServletException {
        log.info("Entering solwebsample.init");
        super.init(conf);
        try {
            if (conn == null) {
                log.info("Creating a connection");
                conn = connectionFactory.createConnection();
            }
            if (sess == null) {
                log.info("Creating a session");
                sess = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
            }
            if (topicConsumer == null) {
                log.info("Creating topic consumer");
                topicConsumer = sess.createConsumer(topic);
                topicConsumer.setMessageListener(new MessageListener() {
                    public void onMessage(Message message) {
                        ++topicCount;
                        log.info("GOT TOPIC MESSAGE[" + topicCount + "]" + message);
                    }
                });
            }
            if (queueConsumer == null) {
                log.info("Creating queue consumer");
                queueConsumer = sess.createConsumer(queue);
                queueConsumer.setMessageListener(new MessageListener() {
                    public void onMessage(Message message) {
                        ++queueCount;
                        log.info("GOT QUEUE MESSAGE[" + queueCount + "]" + message);
                    }
                });
            }
            conn.start();

            MessageProducer prod = sess.createProducer(queue);
            for(int i = 0; i < 100; ++i) {
                TextMessage msg = sess.createTextMessage("hello world "+i);
                log.info("Sending a text message to the queue");
                prod.send(msg);
            }
            status = "SUCCESS";
        }
        catch(Exception ne) {
            log.error(ne);
            status = ne.toString();
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException
    {
        response.setContentType("text/html");
        try {
            PrintWriter out = response.getWriter();
            out.println("<html><body>");
            out.println("<h1>Hello Readers</h1>");
            out.println(status);
            out.println("<p>Topic subscription " + topic.getTopicName() + " Count: " + topicCount);
            out.println("<p>Queue consumer " + queue.getQueueName() + " Count: " + queueCount);
            out.println("</body></html>");
        }
        catch(Exception e) {
        }
    }
}