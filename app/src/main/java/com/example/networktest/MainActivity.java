package com.example.networktest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.*;
import java.net.*;

public class MainActivity extends AppCompatActivity{
    //Define necessary variables
    Socket clientSocket;
    Button btnSend;
    Button btnCalc;
    EditText userInput;
    TextView answerFromSrv;
    TextView result;
    String serverOutput;
    public static final int SERVER_PORT = 53212;
    public static final String SERVER_URL = "se2-isys.aau.at";
    //-------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnCalc = findViewById(R.id.btn_Calc);
        result = findViewById(R.id.res_View);
        btnSend = findViewById(R.id.btn_Send);
        userInput = findViewById(R.id.inpt_ImmNumb);
        answerFromSrv = findViewById(R.id.srvr_Ans_View);
    }


    //Calculating Function
        public void calculation(View view) {

                    String stringInput = convertToString(userInput);

                    int int_user_input = Integer.parseInt(stringInput);
                    boolean check_Addition = true;
                    int quersummme = 0;
                   // while (int_user_input > 11){
                         //quersummme = 0;
                         while (int_user_input > 0){
                             if(check_Addition){quersummme += int_user_input % 10;}
                             else {quersummme -= int_user_input % 10;}
                             check_Addition = !check_Addition;
                             int_user_input /= 10;
                         }
                         //int_user_input = quersummme;
                   // }

            if (quersummme % 2 == 0){ result.setText("Is Even!");
            }else{ result.setText("Is Odd!"); }
    }
    //----------------------------------------------------------------------------------------------

           //Sending Function
           public void sendToServer(View view) {

               Thread thread = new Thread(new Runnable() {
                   @Override
                   public void run() {
                       try {
                           //Create connection to server
                           clientSocket = new Socket(SERVER_URL, SERVER_PORT);
                           //-------------------------------------------------

                           //Create necessary streams inFromServer/outToServer
                           DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
                           BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                           //-------------------------------------------------

                           //Send and receive information
                           outToServer.writeBytes(convertToString(userInput) + '\n');
                           serverOutput = inFromServer.readLine();
                           //-------------------------------------

                           //Set answer on view
                           answerFromSrv.setText(serverOutput);
                           //---------------------------------

                           //Close all streams
                           inFromServer.close();
                           outToServer.close();
                           clientSocket.close();
                           //-------------------

                       } catch (UnknownHostException e) {
                           serverOutput = "! Unknown Host !";
                           e.printStackTrace();
                       } catch (IOException e) {
                           serverOutput = "! Server Error !";
                           e.printStackTrace();
                       }
                   }
               });

               thread.start();

               try {
                   thread.join();
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }

           }
      //-------------------------------------------------------------------------------------

       public String convertToString(EditText userInput){
        return userInput.getText().toString();
    }

}