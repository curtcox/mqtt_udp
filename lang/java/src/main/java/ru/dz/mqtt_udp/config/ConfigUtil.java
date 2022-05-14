package ru.dz.mqtt_udp.config;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;

final class ConfigUtil {

    static NetworkInterface getNetworkInterface() throws IOException {
        InetAddress ip = InetAddress.getLocalHost();
        System.out.println("Current IP address : " + ip.getHostAddress());
        return NetworkInterface.getByInetAddress(ip);
    }

    static String getMachineMacAddressString(NetworkInterface network) throws IOException {
        byte[] mac = network.getHardwareAddress();

        //System.out.print("Current MAC address : ");

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mac.length; i++) {
            //sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
            sb.append(String.format("%02X", mac[i]) );
        }
        System.out.println(sb);
        return sb.toString();
    }

    static String getMachineMacAddressString() throws IOException {
        return getMachineMacAddressString(getNetworkInterface());
    }

    // Attempt to make some GUID-like thing
    static String makeId() {
        long time = System.currentTimeMillis();
        try {
            return ConfigUtil.getMachineMacAddressString()+":"+String.format("%X", time);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
