import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPCient 
{
    static DatagramSocket[] sock; 
    static DatagramSocket s2;
    static int bytesRead=0;
    static String chunk=null;
    static String header_chunk=null;
    static String s=null;
    static int MSS;
    static byte[] buffer;
    static int port;
    static String app=null;
    static String flag="0101010101010101";
    static String Ack;
    static String ackflag;
    static long num=0;
    static int diff;
    static boolean p=true;
    public static void main(String[] args) throws IOException, InterruptedException 
    {
        if(args.length!=7)
        {
            System.out.println("Usage: p2mpClient server-1 server-2 server-3 PORT# file_name MSS");
            System.exit(0);
        }
        int numClients=args.length-4;
        sock= new DatagramSocket[numClients];
        
        for(int j=0;j<numClients;j++)
        {
            sock[j]= new DatagramSocket();
        }

        port = Integer.parseInt(args[numClients+1].trim());
        String fname=args[numClients+2].trim();
        File fileName=new File(fname);
        MSS=Integer.parseInt(args[numClients+3].trim());
        
        StopWatch sw=new StopWatch();
          
            InetAddress[] host = new InetAddress[numClients];
            
            for(int j=0;j<numClients;j++)
            {
                System.out.println("Enter the IP of server "+args[j+1]);
                BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
                host[j]=InetAddress.getByName(br.readLine().trim()); 
            }
                
            buffer = new byte [MSS];
            FileInputStream fis = new FileInputStream(fileName);
            BufferedInputStream bis = new BufferedInputStream(fis); 
            Thread t[]=new Thread[numClients];
            ServerHandler sh[]=new ServerHandler[numClients];
           
    sw.start();
    while ((bytesRead = bis.read(buffer)) != -1&&p==true)
    {
     p=false;   
     ++num;
     app=String.format("%032d\n",num);
     header_chunk =app+flag+new String(buffer, 0, bytesRead);
     int c=CRC(header_chunk);
     String k=Integer.toBinaryString(c);
     int len=k.length();
     
     if(len<32)
     {
         diff=32-len;
         for(int i=1;i<=diff;i++)
     {
      k = k.concat("0");
     }
     }
     chunk=app+flag+k+new String(buffer, 0, bytesRead);
     
     int j;
     for(j=0;j<numClients;j++)
     {
          sh[j]=new ServerHandler(host[j],j,sock[j]);
          t[j]=new Thread(sh[j]);
          t[j].start();
     }
     for(j=0;j<numClients;j++)
     {
        t[j].join();
     }
     p=true;
   }
        sw.stop();
        System.out.println("Time :" + sw.getElapsedTime()/1000 + " sec   ");
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
