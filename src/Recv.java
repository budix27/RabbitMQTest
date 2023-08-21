import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Recv {

    //private final static String QUEUE_NAME = "Pacom.Server";
    private final static String QUEUE_NAME = "AUSYDWS166113_JMeter_bus_1";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        //System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
        
        
        
  	      DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");           
            System.out.println( message );
            FileOutputStream fos = null;
            File file;
            
            try {  
            	file = new File ("C:/Apps/TESTPLAN/RMQoutput.txt");
            	fos = new FileOutputStream(file);
            	  
            	  fos.write(message.getBytes());
              	  fos.flush();
            } catch (IOException e) {
        	      e.printStackTrace();
        	    }  
            finally {
          	  try {
          	     if (fos != null) 
          	     {
          		 fos.close();
          	     }
                    } 
          	  catch (IOException ioe) {
          		ioe.printStackTrace();
          	  }
                 }
            
  	      };  	      
  	      channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> { });
  	      
  	        	      
  	       
    }
    
    
}