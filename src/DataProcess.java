/*  Spencer Hamish Voorend svoo432 897480273
DataProcess.java wrangles the data to allow for the manual graphics generation in the A2.Java file.
*/
import java.io.File;
import java.util.*;

/**Creates a DataProcess object. This object handles most of the data wrangling the assignment.
 *
 */


public class DataProcess {
    public List<Double> timed;
    public List<Double> bytesd;
    public ArrayList<Double> bytestotal;
    public List<String> destinationips;
    public List<String> sourceips;
    private String f;
    private Scanner data;
    private Set<String> destinationunique = new HashSet<>();
    private Set<String> sourceunique = new HashSet<>();
    private IPSort sorteddest;
    private IPSort sortedsource;

    /**
     * Constructor for DataProcess. Process wrangles the data by reading each line, splitting by tabs and storing the
     * data in ArrayLists. It handles blank or missing data lines by skipping them and adding 0.0 or null values in its
     * place.
     *
     * @param f
     */

    public DataProcess(String f) {
        this.f = f;
        process(f);

    }

    private void process(String f) {
        try {
            data = new Scanner(new File(f));
            timed = new ArrayList<>();
            bytesd = new ArrayList<>();
            destinationips = new ArrayList<>();
            sourceips = new ArrayList<>();
        } catch (Exception e) {
            System.out.println("Couldn't find file!"); // <-- Add error to GUI
            return;
        }
        while (data.hasNext()) {
            try {
                String line = data.nextLine();
                String[] details = line.split("\\t");
                String time = details[1];
                timed.add(Double.parseDouble(time));
                String bytes = details[7];
                bytesd.add(Double.parseDouble(bytes));
                String destinationip = details[4];
                destinationunique.add(destinationip); //add destination ip to hashset
                String sourceip = details[2];
                sourceunique.add(sourceip); //add source ip to hashset
                sourceips.add(sourceip);
                destinationips.add(destinationip);
            } catch (Exception ArrayIndexOutOfBoundsException) { //skips with missing data
                bytesd.add(0.0);
                sourceips.add(null);
                destinationips.add(null);
            }

        }
        //can generalise further to another class//
        /*Source IP Sort*/

        List sortedsourceunique = new ArrayList(sourceunique);
        sortedsource = new IPSort();
        for (Object i : sortedsourceunique) {
            sortedsource.add(i.toString());
        }
        /*Destination IP Sort*/
        List sorteddestinationunique = new ArrayList(destinationunique);
        sorteddest = new IPSort();
        for (Object i : sorteddestinationunique) {
            sorteddest.add(i.toString());
        }


    }

    /**
     * process2 processes the data to find the packet data sums for each IP address. It returns an array list with the
     * sums for the IP specified in 2 second intervals.
     *
     * @param ip
     * @return bytestotal
     */

    public ArrayList<Double> process2(String ip) { //computes an array of sum values for each IP by host type
        double startTime = timed.get(0);
        int totalsindex = 0;
        int index = 0;
        int bytestotalsize = (int) Math.ceil((timed.get(timed.size() - 1) - startTime) / 2);
        bytestotal = new ArrayList<>(bytestotalsize + 1);
        for (int i = 0; i < bytestotalsize; i++) {
            bytestotal.add(0.0);
        } //pre-populates array with 0.0s
        for (Double time : timed) {
            if (time >= startTime + 2) {
                startTime += 2;
                totalsindex += 1;

            }
            double oldval = bytestotal.get(totalsindex);
            if ((destinationips.get(index) != null && destinationips.get(index).equals(ip)) | (sourceips.get(index) != null && sourceips.get(index).equals(ip))) {
                if (time >= startTime && time < startTime + 2) {
                    bytestotal.set(totalsindex, (oldval + bytesd.get(index)));

                }
            }
            index += 1;

        }
        return bytestotal;

    }

    /**
     * Returns the unique destination IP addresses for the combobox in a List of Strings.
     *
     * @return sorteddest.processed
     */
    public String[] destreturn() {
        return sorteddest.processed();
    }

    /**
     * Returns the unique source IP addresses for the combobox in a List of Strings.
     *
     * @return sorteddest.processed
     */
    public String[] sourcereturn() {
        return sortedsource.processed();

    }

}

