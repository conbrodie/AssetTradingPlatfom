package common.message;

import common.Constants;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 *     Class used to send and receive messages between the client and server.
 */
public class DataTransfer {
    public static String receiveMessage(Socket sock) throws IOException {
        /**
         * @action Used to receive message from client
         * @param Socket sock - client's Socket value
         * @comment receive free text transport messages
         * @return contents of server's message
         */

        DataInputStream dis = new DataInputStream(new BufferedInputStream(sock.getInputStream()));
        byte[] header = new byte[Constants.HEADER_SIZE];

        dis.readFully(header, 0, 4);
        int size = DecodeHeader(header); // get the length of the data following the header
        byte[] data = new byte[size];
        dis.readFully(data, 0, size);

        return new String(data, StandardCharsets.UTF_8);
    }

    public static void sendMessage(Socket sock, String message) throws IOException {
        /**
         * @action Used to send message to client
         * @param Socket sock - client's Socket value
         * @param message String containing message to be sent to server
         * @comment send free text transport message
         * @return none
         */

        DataOutputStream dos = new DataOutputStream(sock.getOutputStream());
        byte[] byteData= message.getBytes(StandardCharsets.UTF_8);
        int length = byteData.length;
        byte[] sendHeader = EncodeHeader(length, Constants.HEADER_SIZE); // set the length of msg data
        // Prepare the message to be sent "header - 4 bytes (no of message bytes), followed by message data bytes"
        byte[] dataWithHeader = Arrays.copyOf(sendHeader, sendHeader.length + byteData.length);
        System.arraycopy(byteData, 0,  dataWithHeader, sendHeader.length, byteData.length);
        dos.write(dataWithHeader); // now send to server...
        dos.flush();
    }

    public static byte[] EncodeHeader(int msglen_, int size_)
    {
        // Used to encode an integer to an array of bytes.
        // In this case encode the messages length to a 4 byte array.

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
        // In this case decode the messages length.

        int total = 0;
        for (int i = 0; i < bytes_.length; ++i)
        {
            // Need to be careful here as Java byte primitive range is -127 to +127
            total |=  (bytes_[(bytes_.length - 1) - i] & 0xFF) << (8 * i);
        }

        return total;
    }
}
