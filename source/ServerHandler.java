import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class ServerHandler extends UDPCient implements Runnable 
{
    InetAddress host;
    int j;
    DatagramSocket d;
    public ServerHandler(InetAddress host,int j,DatagramSocket sock)
    {
        d=sock;
        this.host=host;
        this.j=j;
    }

    @Override
    public void run()
    {
     boolean proceed=false;
     
     while(proceed==false)
    {
     DatagramPacket dp = new DatagramPacket(chunk.getBytes(),chunk.getBytes().length,host,port);
     try
     {
     d.setSoTimeout(450);
     d.send(dp);
     
     byte[] rep = new byte[100];
     DatagramPacket reply = new DatagramPacket(rep, rep.length);
     d.receive(reply);
     byte[] data = reply.getData();
     s = new String(data, 0, reply.getLength());
     Ack=s.substring(0,32);
     ackflag=s.substring(32,48);
     int rec_ack=Integer.parseInt(Ack);
 
     if(rec_ack==num)
     {
         proceed=true;
     }
     else
     {
       proceed=false;
     }
     }
     catch(SocketException e)
        {
            System.out.println("TimeOut, Retransmitting Segment " + num + " to Host " + j);
        }
     catch(IOException e)
        {
            System.out.println("TimeOut, Retransmitting Segment "+ num + " to Host " + j);
        }
     } 
    }
}
