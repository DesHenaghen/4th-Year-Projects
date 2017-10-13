public class swapLSB {
    public static void main (String[] args) {
        swapLSB lsb = new swapLSB();
        int result = lsb.swapLSB(0, 155);

        System.out.println(Integer.toBinaryString(result));
        System.out.println(result);
    }

    private int swapLSB(int bitToHide, int byt) {
        System.out.println(Integer.toBinaryString(byt));
        System.out.println(Integer.toBinaryString(bitToHide));
        if (bitToHide == 0)
            return byt & ~ 0b1;
        else if (bitToHide == 1)
            return byt | 0b1;
        else
            return byt;
    }
}
