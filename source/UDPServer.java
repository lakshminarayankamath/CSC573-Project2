import java.io.*;
import java.net.*;
import java.util.Random;
 
public class UDPServer
{
    static String sentdata;
    static String flagack="1010101010101010";
    static String temp_chunk=null;
    static int diff;
    static long flen=2095460;
    static long bytesRead=0;
    static String fileName;
    static long exp_ack=1;
    static double p=0.0;
    
    public static void main(String args[])
    {
        DatagramSocket sock;
        if(args.length!=4)
        {
            System.out.println("Usage: p2mpServer PORT# file_name p");
            System.exit(0);
        } 
        int port=Integer.parseInt(args[1]);
        fileName=args[2].trim();
        p=Double.parseDouble(args[3].trim());
        try
        {
            sock = new DatagramSocket(port);
            double st=0.0;
            Random r=new Random(100);
             
            echo("Server socket created. Waiting for incoming data...");
             byte[] buffer = new byte[3500];
             String chunk;
         
            while(bytesRead<flen)
            {
                DatagramPacket incoming = new DatagramPacket(buffer, buffer.length);
                sock.receive(incoming);
                
                byte[] data = incoming.getData();
                String s = new String(data, 0, incoming.getLength());
                String ACK=s.substring(0,32);
                String flag=s.substring(32,49);
                String chksum=s.substring(49,81);
                s=s.substring(81,s.length());
                
                if((st=r.nextDouble())<p)
                {
                 echo("Packet Loss, " + "Sequence Number:" + exp_ack);
                }
                else
                {   
                BufferedWriter bw=new BufferedWriter(new FileWriter("file2.txt",true));  
                temp_chunk=ACK+flag+s;
                int c=CRC(temp_chunk);
                String k=Integer.toBinaryString(c);
                
               int len=k.length();
               int num=Integer.parseInt(ACK);
               
               if(len<32)
               {
               diff=32-len;
               for(int j=1;j<=diff;j++)
               {
                k = k.concat("0");
               }
               }
     
               if(k.matches(chksum))
               {
                if(exp_ack!=num) 
                {
                chunk = ACK + flagack;
                System.out.println("Wrong Packet, Trying Again");
                DatagramPacket dp = new DatagramPacket(chunk.getBytes() , chunk.getBytes().length , incoming.getAddress() , incoming.getPort());
	        sock.send(dp);
                }
                else
                {
                //System.out.println("Seg "+num+"received");
                exp_ack+=1;
                   
                bw.write(s);
                bw.close();
                
                chunk = ACK +flagack;
                DatagramPacket dp = new DatagramPacket(chunk.getBytes() , chunk.getBytes().length , incoming.getAddress() , incoming.getPort());
	        sock.send(dp);
                bytesRead=bytesRead+s.length();
                }
               }
               else
               {
 
               }
            }
          }
           sock.close();
        }
        catch(IOException e)
        {
            System.out.println(e.getMessage());
        }
    }
     
    public static void echo(String msg)
    {
        System.out.println(msg);
    }
    
    public static int CRC(String str)
     {
         int crc = 0xFFFF; 
        int polynomial = 0x1021;  
        byte[] bytes = str.getBytes();

        for (byte b : bytes) 
        {
            for (int i = 0; i < 8; i++) 
            {
                boolean bit = ((b>>(7-i)&1) == 1);
                boolean c15 = ((crc>>15&1) == 1);
                crc <<= 1;
                if (c15 ^ bit) crc ^= polynomial;
            }
        }
        crc &= 0xffff;
        return crc;
     }
}
