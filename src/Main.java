import java.util.Scanner;

public class Main {
    static Scanner reader = new Scanner(System.in);

    public static void main(String[] args) {
        int number = reader.nextInt();
        reader.nextLine();
        String netAddress = reader.nextLine();
        int pow = calculation(number);

        if(pow < 9){
            for (int i = 8 - pow; i < 8; i++){
                subnet[3][i] = 0;
            }
        }
        if(pow > 8){
            int octet2 = pow - 8;

            for (int i = 8 - octet2; i < 8; i++){
                subnet[2][i] = 0;
            }
        }

        if (pow > 24){
            int octet4 = pow - 24;
            for (int i = 8 - octet4; i < 8; i++){
                subnet[0][i] = 0;
            }

            for (int i = 1; i < 4; i++){
                for (int j = 0; j < 8; j++){
                    subnet[i][j] = 0;
                }
            }
        }
        else if(pow > 16 && pow < 25){
            int octet3 = pow - 16;
            for (int i = 8 - octet3; i < 8; i++){
                subnet[1][i] = 0;
            }

            for (int i = 2; i < 4; i++){
                for (int j = 0; j < 8; j++){
                    subnet[i][j] = 0;
                }
            }
        }
        else if(pow > 8 && pow < 17){
            int octet2 = pow - 8;
            for (int i = 8 - octet2; i < 8; i++){
                subnet[2][i] = 0;
            }

            for (int i = 3; i < 4; i++){
                for (int j = 0; j < 8; j++){
                    subnet[i][j] = 0;
                }
            }
        }
        else{
            for (int i = 8 - pow; i < 8; i++){
                subnet[3][i] = 0;
            }
        }



        for (int i = 0; i < 4; i++){
            for (int j = 0; j < 8; j++){
                subnetString += String.valueOf(subnet[i][j]);
            }
            if (i < 3) subnetString += '.';
        }

        int possible = (int) (Math.pow(2, pow) - 2);

        String[] netAddressInput = netAddress.split("\\.");

        String octet1 = netAddressInput[0];
        String octet2 = netAddressInput[1];
        String octet3 = netAddressInput[2];
        String octet4 = netAddressInput[3];

//        if (pow < 9){
//            if ((254 - Integer.parseInt(octet4)) >= possible){
//                    ipRange = octet1 + "." + octet2 + "." + octet3 + "." + (Integer.parseInt(octet4) + 1) + "  -  " + (Integer.parseInt(octet4) + possible);
//                    broadcast = octet1 + "." + octet2 + "." + octet3+ "." + (Integer.parseInt(octet4) + possible + 1);
//                    System.out.println(ipRange);
//                    System.out.println(broadcast);
//            }else{
//                    ipRange = octet1 + "." + octet2 + "." + octet3 + "." + (255 - possible) + "  -  254";
//                    broadcast = octet1 + "." + octet2 + "." + octet3+ ".255";
//                    System.out.println(ipRange);
//                    System.out.println(broadcast);
//            }
//        }else if(pow > 8 && pow < 17){
//            System.out.println(possible);
//            int octet3Digit = possible/255 - 1;
//            octet4 = String.valueOf(254 - octet3Digit);
//            ipRange = octet1 + "." + octet2 + "." + octet3 + ".1  -  " + octet1 + "." + octet2 + "." + (Integer.parseInt(octet3) + octet3Digit) + ".254" ;
//            broadcast = octet1 + "." + octet2 + "." + (Integer.parseInt(octet3) + octet3Digit) + ".255";
//            System.out.println(ipRange);
//            System.out.println(broadcast);
//        }

        String fullIpAddress = calculation2(Integer.parseInt(octet1)) + '.' + calculation2(Integer.parseInt(octet2)) + '.' + calculation2(Integer.parseInt(octet3)) + '.' + calculation2(Integer.parseInt(octet4));


        String andOperation = "";
        //String broadcastAddress = "";

        for (int i = 0; i < 35; i++){
            andOperation += compare(fullIpAddress.charAt(i), subnetString.charAt(i));
        }

        StringBuilder broadcastAddress = new StringBuilder(andOperation);

        for (int i = 34; i > 34 - pow; i--){
            if (broadcastAddress.charAt(i) == '.') {
                continue;
            }else {
                broadcastAddress.setCharAt(i, '1');
            }
        }

        String broadCastAddress = String.valueOf(broadcastAddress);


        String[] fullIpAddress2 = fullIpAddress.split("\\.");
        String[] subnetString2 = subnetString.split("\\.");
        String[] andOperation2 = andOperation.split("\\.");
        String[] broadcastAddress2 = broadCastAddress.split("\\.");


        System.out.println("IP Address decimal:   " +  binaryToDecimal(fullIpAddress2[0]) + '.' + binaryToDecimal(fullIpAddress2[1]) + '.' + binaryToDecimal(fullIpAddress2[2]) + '.' + binaryToDecimal(fullIpAddress2[3]));

        System.out.println("IP Address:          " + fullIpAddress);
        System.out.println("Subnet Mask:         " + subnetString);
        System.out.println("Network Address:     " + andOperation);
        System.out.println("Broadcast Address:   " + broadcastAddress);

        //String andOperation2 = andOperation.replace(".", "");
    }

    static byte[][] subnet = {{1,1,1,1,1,1,1,1},{1,1,1,1,1,1,1,1},{1,1,1,1,1,1,1,1},{1,1,1,1,1,1,1,1}};
    static byte[][] inputIpAddress = {{1,1,1,1,1,1,1,1},{1,1,1,1,1,1,1,1},{1,1,1,1,1,1,1,1},{1,1,1,1,1,1,1,1}};

    static String subnetString = "";

    public static int calculation(int number) {
        int pow = 1;
        while(number > Math.pow(2, pow) - 2){
            pow++;
        }

        return pow;
    }

    public static String calculation2(int number){
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

    static int binaryToDecimal(String number){
        int decimal = 0;

        for(int i = 0; i < 8; i++){
            if(number.charAt(i) == '1'){
                decimal += decimalArray[i];
            }

            //System.out.println(number.charAt(i));
        }

        return decimal;
    }
}