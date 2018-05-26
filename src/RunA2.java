/*  Spencer Hamish Voorend svoo432 897480273
RunA2.java runs the assignment implementing Runnable.
*/

/**
 * Runs A2.java by creating a A2 instance.
 */

public class RunA2 implements Runnable {
    public void run() {
        A2 a = new A2();
    }
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new RunA2());
    }
}