import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Scanner;

public class Main {
    static Scanner reader = new Scanner(System.in);

    public static void main(String[] args) {
        JTextField input1 = new JTextField();
        JTextField input2 = new JTextField();
        JLabel label1 = new JLabel("IP Range: ");
        JLabel label2 = new JLabel("IP Address: ");
        label1.setFont(new Font("serif", Font.BOLD, 20));
        label2.setFont(new Font("serif", Font.BOLD, 20));
        input1.setPreferredSize(new Dimension(250, 35));
        input2.setPreferredSize(new Dimension(236, 35));
        input1.setFont(new Font("rockwell", Font.BOLD, 20));
        input2.setFont(new Font("rockwell", Font.BOLD, 20));

        JPanel panel1 = new JPanel();
        panel1.add(label1);
        panel1.add(input1);
        JPanel panel2 = new JPanel();
        panel2.add(label2);
        panel2.add(input2);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(panel1);
        mainPanel.add(panel2);

        int choice = JOptionPane.showConfirmDialog(null, mainPanel, "Network Range Calculator", JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (choice == 2) System.exit(0);

        int number = Integer.parseInt(input1.getText());
        String netAddress = input2.getText();

        System.out.println();
        char networkClass;
        int maxCapacityPower = calculation(number);
        double totalHosts = totalHosts(maxCapacityPower, 1);
        double usableHosts = totalHosts(maxCapacityPower, 2);

        String[] netAddressInput = netAddress.split("\\.");

        String octet1 = netAddressInput[0];
        String octet2 = netAddressInput[1];
        String octet3 = netAddressInput[2];
        String octet4 = netAddressInput[3];

        String ipAddressBinary = decimalToBinary(Integer.parseInt(octet1)) + '.' + decimalToBinary(Integer.parseInt(octet2)) + '.' + decimalToBinary(Integer.parseInt(octet3)) + '.' + decimalToBinary(Integer.parseInt(octet4));


        if (Integer.parseInt(octet1) < 128) networkClass = 'A';
        else if (Integer.parseInt(octet1) < 192) networkClass = 'B';
        else if (Integer.parseInt(octet1) < 224) networkClass = 'C';
        else if (Integer.parseInt(octet1) < 240) networkClass = 'D';
        else networkClass = 'E';


        byte[][] subnetMaskArray = subnetMaskGenerator(maxCapacityPower);
        String subnetMaskBinary = subnetArrayToString(subnetMaskArray);


        String networkAddress = networkAddressGenerator(ipAddressBinary, subnetMaskBinary);

        StringBuffer broadcastAddress = new StringBuffer(networkAddress);

        int extra = 0;

        if (maxCapacityPower > 8 && maxCapacityPower < 17) extra = 1;
        else if (maxCapacityPower > 16 && maxCapacityPower < 25) extra = 2;
        else if (maxCapacityPower > 24) extra = 3;

        for (int i = 34; i > 34 - maxCapacityPower - extra; i--) {
            if (broadcastAddress.charAt(i) == '.') {
                continue;
            } else {
                broadcastAddress.setCharAt(i, '1');
            }
        }

        String broadCastAddress = String.valueOf(broadcastAddress);


        String[] fullIpAddressDisplay = ipAddressBinary.split("\\.");
        String[] subnetStringDisplay = subnetMaskBinary.split("\\.");
        String[] networkAddressDisplay = networkAddress.split("\\.");
        String[] broadcastAddressDisplay = broadCastAddress.split("\\.");

        String output;

        output = "<html><pre><font color='red'>IP Address:</font>                          " + binaryToDecimal(fullIpAddressDisplay[0]) + '.' + binaryToDecimal(fullIpAddressDisplay[1]) + '.' + binaryToDecimal(fullIpAddressDisplay[2]) + '.' + binaryToDecimal(fullIpAddressDisplay[3]) + "<br>";
        output += "<font color = 'red'>Network Address:</font>                     " + binaryToDecimal(networkAddressDisplay[0]) + '.' + binaryToDecimal(networkAddressDisplay[1]) + '.' + binaryToDecimal(networkAddressDisplay[2]) + '.' + binaryToDecimal(networkAddressDisplay[3]) + "<br>";
        output += "<font color = 'red'>Usable Host IP Range:</font>                " + binaryToDecimal(networkAddressDisplay[0]) + '.' + binaryToDecimal(networkAddressDisplay[1]) + '.' + binaryToDecimal(networkAddressDisplay[2]) + '.' + (binaryToDecimal(networkAddressDisplay[3]) + 1) + "   -  " + binaryToDecimal(broadcastAddressDisplay[0]) + '.' + binaryToDecimal(broadcastAddressDisplay[1]) + '.' + binaryToDecimal(broadcastAddressDisplay[2]) + '.' + (binaryToDecimal(broadcastAddressDisplay[3]) - 1) + "<br>";
        output += "<font color = 'red'>Broadcast Address:</font>                   " + binaryToDecimal(broadcastAddressDisplay[0]) + '.' + binaryToDecimal(broadcastAddressDisplay[1]) + '.' + binaryToDecimal(broadcastAddressDisplay[2]) + '.' + binaryToDecimal(broadcastAddressDisplay[3]) + "<br>";
        output += "<font color = 'red'>Subnet Mask:</font>                         " + binaryToDecimal(subnetStringDisplay[0]) + '.' + binaryToDecimal(subnetStringDisplay[1]) + '.' + binaryToDecimal(subnetStringDisplay[2]) + '.' + binaryToDecimal(subnetStringDisplay[3]) + "<br>";
        output += "<font color = 'red'>Total Number of Hosts:</font>               " + (int) totalHosts + "<br>";
        output += "<font color = 'red'>Number of Usable Hosts:</font>              " + (int) usableHosts + "<br>";
        output += "<font color = 'red'>IP Class:</font>                            " + networkClass + "<br>";
        output += "<font color = 'red'>Short:</font>                               " + netAddress + " /" + (32 - maxCapacityPower) + "<br>";
        output += "<font color = 'green'>------------------------------------------------------------------------------<br></font>";
        output += "<font color = 'red'>IP Address Binary:</font>                   " + ipAddressBinary + "<br>";
        output += "<font color = 'red'>Subnet Mask Binary:</font>                  " + subnetMaskBinary + "<br>";
        output += "<font color = 'red'>Network Address Binary:</font>              " + networkAddress + "<br>";
        output += "<font color = 'red'>Broadcast Address Binary:</font>            " + broadcastAddress + "</pre></html>";

        JLabel outputLabel = new JLabel(output);
        outputLabel.setFont(new Font("calibri", Font.BOLD, 20));
        outputLabel.setBorder(new EmptyBorder(20, 20, 20, 20));

//        JOptionPane.showMessageDialog(null, outputLabel, "Output", JOptionPane.PLAIN_MESSAGE);
        JFrame outputDialog = new JFrame();
        outputDialog.add(outputLabel);
        outputDialog.pack();
        outputDialog.setVisible(true);
        outputDialog.setLocationRelativeTo(null);
    }

    public static int calculation(int number) {
        int power = 1;
        while(number > Math.pow(2, power) - 2){
            power++;
        }

        return power;
    }

    static double totalHosts(int pow, int choice){
        double result = 0;

        if (choice == 1) {
            result = Math.pow(2, pow);
        }else if(choice == 2){
            result = Math.pow(2, pow) - 2;
        }

        return result;
    }

    public static String decimalToBinary(int number){
        String binary = "";

        for (int i = 128; i > 0; i = i / 2){
            if (number - i >= 0){
                binary += "1";
                number = number - i;
            }else{
                binary += "0";
            }
        }

        return binary;
    }

    public static char compare(char one, char two){
        char three;
        if(one == '1' && two == '1'){
            three = '1';
        }else{
            three = '0';
        }

        if (one == '.'){
            three = '.';
        }

        return three;
    }

    static int[] decimalArray = {128, 64, 32, 16, 8, 4, 2, 1};

    static int binaryToDecimal(String number) {
        int decimal = 0;

        for (int i = 0; i < 8; i++) {
            if (number.charAt(i) == '1') {
                decimal += decimalArray[i];


                //System.out.println(number.charAt(i));
            }
        }
        return decimal;

    }

        static String subnetArrayToString( byte[][] subnetArray){
            String subnetString = "";

            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 8; j++) {
                    subnetString += String.valueOf(subnetArray[i][j]);
                }
                if (i < 3) subnetString += '.';
            }

            return subnetString;
        }

        static String networkAddressGenerator (String ipAddressBinary, String subnetMask){
            String networkAddress = "";

            for (int i = 0; i < 35; i++) {
                networkAddress += compare(ipAddressBinary.charAt(i), subnetMask.charAt(i));
            }


            return networkAddress;
        }

        static byte[][] subnetMaskGenerator ( int maxCapacityPower){
            byte[][] subnetArray = {{1, 1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1, 1}};

            if (maxCapacityPower > 24) {
                int Octet4 = maxCapacityPower - 24;
                for (int i = 8 - Octet4; i < 8; i++) {
                    subnetArray[0][i] = 0;
                }

                for (int i = 1; i < 4; i++) {
                    for (int j = 0; j < 8; j++) {
                        subnetArray[i][j] = 0;
                    }
                }
            } else if (maxCapacityPower > 16) {
                int Octet3 = maxCapacityPower - 16;
                for (int i = 8 - Octet3; i < 8; i++) {
                    subnetArray[1][i] = 0;
                }

                for (int i = 2; i < 4; i++) {
                    for (int j = 0; j < 8; j++) {
                        subnetArray[i][j] = 0;
                    }
                }
            } else if (maxCapacityPower > 8) {
                int Octet2 = maxCapacityPower - 8;
                for (int i = 8 - Octet2; i < 8; i++) {
                    subnetArray[2][i] = 0;
                }

                for (int i = 3; i < 4; i++) {
                    for (int j = 0; j < 8; j++) {
                        subnetArray[i][j] = 0;
                    }
                }
            } else {
                for (int i = 8 - maxCapacityPower; i < 8; i++) {
                    subnetArray[3][i] = 0;
                }
            }

            return subnetArray;
        }}