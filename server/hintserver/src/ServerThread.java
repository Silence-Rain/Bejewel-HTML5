import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStreamReader; 
import java.net.Socket;

public class ServerThread implements Runnable {

    private Socket client = null;

    public ServerThread(Socket client){
        this.client = client;
        new Thread(this).start();
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            PrintWriter out = new PrintWriter(client.getOutputStream());//将输出流包装为打印流

            String matrix = in.readLine();
            List list = new List();
            int[] pos = list.test();

            out.println(Integer.toString(pos[0]) + " " + Integer.toString(pos[1])); 
            out.flush(); 

            in.close();
            out.close();
            client.close();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}