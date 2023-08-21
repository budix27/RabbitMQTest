import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class JSONSend 
{
 //private final static String QUEUE_NAME = "json-example";
 private final static String QUEUE_NAME = "jmeterQueue";
 private ConnectionFactory factory = null;
 
 public JSONSend() 
 {
  // TODO Auto-generated constructor stub
 }
 
 @SuppressWarnings("unchecked")
 public void run() throws Exception
 {
     factory = new ConnectionFactory(); 
     factory.setHost("localhost"); 
     //factory.setPort(5676);
     
     System.out.println("connected to rabbitMQ on localhost ...");
     Connection connection = factory.newConnection();
     Channel channel = connection.createChannel();

     channel.queueDeclare(QUEUE_NAME, true, false, false, null);
     for (int i = 1; i <= 10; i++)
     {
      JSONObject obj = new JSONObject();
      
      obj.put("name", String.format("Person%s", i));
      obj.put("age", new Integer(35+i));
      
      Map<String, Object> headers = new HashMap<String, Object>();
      headers.put("Content-Type","application/json");
      
        
      
      channel.basicPublish("", QUEUE_NAME, 
    		  new AMQP.BasicProperties.Builder()
    		  .contentType("application/json")
              .headers(headers)
              .build(), 
              obj.toJSONString().getBytes()); 
      System.out.println(" [x] Sent '" + obj.toJSONString() + "'");
     }
     
     channel.close();
     connection.close();
 }
 
 /**
  * @param args
  * @throws Exception 
  */
 public static void main(String[] args) throws Exception 
 {
  // TODO Auto-generated method stub
  JSONSend test = new JSONSend();
  test.run();
 }

}