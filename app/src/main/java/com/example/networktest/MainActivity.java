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
    }


    //Calculating Function
        public void calculation(View view) {
            userInput = findViewById(R.id.inpt_ImmNumb);
            btnCalc = findViewById(R.id.btn_Calc);
            result = findViewById(R.id.res_View);

                    Boolean checkMinus = false;
                    String stringInput = convertToString(userInput);
                    System.out.println(stringInput);
                    char[] userInpt_toChar = stringInput.toCharArray();
                    int quersummme = 0;
                    for (int index = 0; index < stringInput.length(); index++) {
                        if (checkMinus) {
                            quersummme += Character.getNumericValue(userInpt_toChar[index]);
                            checkMinus = false;
                        } else {
                            quersummme -= Character.getNumericValue(userInpt_toChar[index]);
                            checkMinus = true;
                        }
                    }

                    result.setText(quersummme);
    }



        //-------------------------------------------------
        //Sending Function

    public void sendToServer(View view) {
        btnSend = findViewById(R.id.btn_Send);
        userInput = findViewById(R.id.inpt_ImmNumb);
        answerFromSrv = findViewById(R.id.srvr_Ans_View);

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
                }catch (UnknownHostException e) {
                    serverOutput = "! Unknown Host !" ;
                    e.printStackTrace();
                }catch (IOException e){
                    serverOutput = "! Server Error !" ;
                    e.printStackTrace();
                }
            }
        });

        thread.start();

        try {
            thread.join();
        }catch (InterruptedException e){
            e.printStackTrace();
        }

}


    public String convertToString(EditText userInput){
        String user_Input_converted = userInput.getText().toString();
        return user_Input_converted;
    }

}