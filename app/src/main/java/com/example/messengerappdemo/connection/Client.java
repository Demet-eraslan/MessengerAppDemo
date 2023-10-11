
package com.example.messengerappdemo.connection;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.io.IOException;
import java.net.*;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Client {
    private DatagramSocket socket;
    private byte[] rcvBuf = new byte[1024];


    private static final String IPV4_REGEX =
            "^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
                    "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
                    "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
                    "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";

    private static final Pattern IPv4_PATTERN = Pattern.compile(IPV4_REGEX);



    //public String myIp = InetAddress.getByName(InetAddress.getLocalHost().toString()).toString().replace("/","");
    public Client(int port) throws UnknownHostException {
        try {
            if (this.socket == null) {
                this.socket = new DatagramSocket(null);
                this.socket.setReuseAddress(true);
                this.socket.setBroadcast(true);
                this.socket.bind(new InetSocketAddress(port));
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void SendMessage(String msg, String rcvIPAddress, int rcvPort) {
        try {
            if (this.socket != null) {
                byte[] sndBuf = msg.getBytes();
                DatagramPacket sndDatagram = new DatagramPacket(sndBuf, sndBuf.length, InetAddress.getByName(rcvIPAddress), rcvPort);
                this.socket.send(sndDatagram);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Connect(InetAddress rcvIPAddress, int rcvPort) {
        try {
            this.socket.connect(rcvIPAddress, rcvPort);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Disconnect() {
        try {
            this.socket.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Close() {
        try {
            this.socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public DatagramPacket Receive() {
        try {
            DatagramPacket rcvPacket = new DatagramPacket(rcvBuf, rcvBuf.length);
            this.socket.receive(rcvPacket);
            return rcvPacket;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getMyIPAddress() {

        String myIP="127.0.0.1";
        try {
            Enumeration<NetworkInterface> networkInterfaceEnumeration = NetworkInterface.getNetworkInterfaces();
            while( networkInterfaceEnumeration.hasMoreElements()){
                for ( InterfaceAddress interfaceAddress : networkInterfaceEnumeration.nextElement().getInterfaceAddresses())
                    if ( interfaceAddress.getAddress().isSiteLocalAddress())
                        myIP = interfaceAddress.getAddress().getHostAddress();
            }
            return myIP;
        } catch (SocketException ex) {
            ex.printStackTrace();
            return myIP;
        }
    }
    public Boolean ValidateIP(String ip) throws UnknownHostException {
        if (ip == null) {
            return false;
        }
        Matcher matcher = IPv4_PATTERN.matcher(ip);
        return matcher.matches();
    }
}



