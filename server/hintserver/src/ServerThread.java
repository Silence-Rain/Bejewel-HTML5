import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStreamReader; 
import java.io.IOException; 
import java.net.Socket;

public class ServerThread implements Runnable {

    private Socket client = null;
    private Thread th = null;

    public ServerThread(Socket client){
        this.client = client;
        this.th = new Thread(this);
        this.th.start();
    }

    @Override
    public void run() {

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            PrintWriter out = new PrintWriter(client.getOutputStream());
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
            System.out.println("ERROR!!!!");
            System.out.println(e);

            e.printStackTrace();
            th.stop();
        }

    }

}