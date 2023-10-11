package com.example.messengerappdemo.connection;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class MulticastClient {
    private MulticastSocket mcSocket;
    private byte[] mcBuf = new byte[1024];
    private byte[] mcRcvBuf = new byte[1024];
    private InetAddress grpAddress;
    private int mcPort;


    public MulticastClient(int port) {
        try {
            this.mcPort = port;
            mcSocket = new MulticastSocket(mcPort);
        } catch (IOException e) {
            e.printStackTrace();
            mcSocket = null;
        }
    }

    public String GetMCAddress(){
        return grpAddress.toString().replace("/","");
    }

    public void SendGroupMessage(String grpMsg) {
        try {
            if (mcSocket != null) {
                mcBuf = grpMsg.getBytes();
                DatagramPacket grpPacket = new DatagramPacket(mcBuf, mcBuf.length, grpAddress, mcPort);
                this.mcSocket.send(grpPacket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public DatagramPacket ReceiveGroupMessage(){
        try {
            DatagramPacket rcvPacket = new DatagramPacket(mcRcvBuf, mcRcvBuf.length);
            this.mcSocket.receive(rcvPacket);
            return rcvPacket;
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }
    public void SendLogON() {
        SendGroupMessage("LOGON");
    }
    public void SendLogOFF() {
        SendGroupMessage("LOGOFF");
    }
    public void Join(String grpAddress) {
        try {
            InetAddress mcIP = InetAddress.getByName(grpAddress);
            if (mcSocket!=null){
                this.grpAddress = mcIP;
                mcSocket.joinGroup(mcIP);
                SendLogON();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void Leave() {
        try {
            if (mcSocket!=null){
                SendLogOFF();
                mcSocket.leaveGroup(grpAddress);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
