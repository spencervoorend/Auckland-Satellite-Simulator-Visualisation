/*  Spencer Hamish Voorend svoo432 897480273
IPSort.java sorts the IP addresses
Adapted from https://www.experts-exchange.com/questions/21642813/Sorting-a-list-of-IP-addresses.html
*/

import java.net.InetAddress;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

/**
 * Uses a TreeMap and InetAddress to Sort IP Addresses
 */
public class IPSort {
    private Map sorted = new TreeMap();

    private static long ipToLong(String ip) {
        long n = 0;
        try {
            byte[] address = InetAddress.getByName(ip).getAddress();
            for (int i = 0; i < address.length; i++) {
                long x = address[i];
                if (x < 0) {
                    x += 256;
                }
                n += x << ((3 - i) * 8);
            }
            return n;
        } catch (Exception UnknownHostException) {
            return n;
        }
    }

    /**
     * Adds an IP address to the treemap for sorting.
     *
     * @param ip
     */
    public void add(String ip) {
        try {
            sorted.put(new Long(ipToLong(ip)), ip);
        } catch (Exception UnknownHostException) {
        }
    }

    /**
     * Returns a String Array of the sorted list of IP addresses.
     *
     * @return targetArray;
     */
    public String[] processed() {
        Collection<String> values = sorted.values();
        String[] targetArray = values.toArray(new String[values.size()]);
        return targetArray;

    }
}
