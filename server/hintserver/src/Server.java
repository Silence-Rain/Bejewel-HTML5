import java.net.ServerSocket;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStreamReader; 

public class Server {
    public static void main(String[] args) throws Exception{
        ServerSocket server = new ServerSocket(9992);

        while(true){
            //等待客户端的连接，如果没有获取连接
            Socket client = server.accept(); 
            //为每个客户端连接开启一个线程
            new ServerThread(client);
        }

    }
} 