package helpers.msgprotocol;

import helpers.Constants;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class DataTransfer {
    /**
     *  Class for sending data to and from the client and server.
     */
    public static String receiveMessage(Socket sock) throws IOException {
        /**
         * @action Used to receive message from client
         * @param Socket sock - client's Socket value
         * @comment receive xml transport messages
         * @return contents of server's message
         */
        DataInputStream dis = new DataInputStream(new BufferedInputStream(sock.getInputStream()));
        byte[]header = new byte[Constants.HEADER_SIZE];

        dis.readFully(header,0,4);
        int size = DecodeHeader(header);
        byte[] data = new byte[size];
        dis.readFully(data,0,size);

        return new String(data, StandardCharsets.UTF_8);
    }
    public static void sendMessage(Socket sock, String message) throws IOException {
        /**
         * @action Used to send message to client
         * @param Socket sock - client's Socket value
         * @param message String containing message to be sent to server
         * @comment send xml transport message
         * @return none
         */

        DataOutputStream dos = new DataOutputStream(sock.getOutputStream());
        byte[] byteData= message.getBytes(StandardCharsets.UTF_8);
        int length = byteData.length;
        byte[] sendHeader = EncodeHeader(length,Constants.HEADER_SIZE);
        byte[] dataWithHeader = Arrays.copyOf(sendHeader,sendHeader.length + byteData.length);
        System.arraycopy(byteData,0,dataWithHeader,sendHeader.length, byteData.length);
        dos.write(dataWithHeader);
        dos.flush();
    }

    public static byte[] EncodeHeader(int msglen_, int size_){
        // Used to encode an integer to an array of bytes.
        // In this case encode the XML messages length to a 4 byte array.

        byte[] header = new byte[size_];
        for (int i = (size_ - 1); i >= 0; --i)
        {
            header[i] = (byte)(msglen_ & 0xFF);
            msglen_ >>= 8;
        }

        return header;
    }
    public static int DecodeHeader(byte[] bytes_)
    {
        // Used to decode an array of bytes to an integer.
        // In this case decode the XML messages length.

        int total = 0;
        for (int i = 0; i < bytes_.length; ++i)
        {
            // Need to be careful here as Java byte primitive range is -127 to +127
            total |=  (bytes_[(bytes_.length - 1) - i] & 0xFF) << (8 * i);
        }

        return total;
    }
}
