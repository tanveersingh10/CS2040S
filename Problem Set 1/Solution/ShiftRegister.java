///////////////////////////////////
// This is the main shift register class.
// Notice that it implements the ILFShiftRegister interface.
// You will need to fill in the functionality.
///////////////////////////////////

/**
 * class ShiftRegister
 * @author
 * Description: implements the ILFShiftRegister interface.
 */
public class ShiftRegister implements ILFShiftRegister {
    ///////////////////////////////////
    // Create your class variables here
    int[] shiftRegister;
    int tap;

    ///////////////////////////////////
    // TODO:

    ///////////////////////////////////
    // Create your constructor here:
    ///////////////////////////////////
    public ShiftRegister(int size, int tap) {
        this.tap = tap;
        this.shiftRegister = new int[size];
    }

    ///////////////////////////////////
    // Create your class methods here:
    ///////////////////////////////////
    /**
     * setSeed
     * @param seed
     * Description:
     */
    @Override
    public void setSeed(int[] seed) {
        for (int i = 0; i < seed.length; i++) {
            if (seed[i] != 0 && seed[i] != 1) {
                System.out.println("non binary number added");
                return;
            }
        }
        for (int i = 0; i < seed.length; i++) {
            shiftRegister[i] = seed[seed.length - 1 - i];
        }
    }
    /**
     * shift
     * @return
     * Description:
     */
    @Override
    public int shift() {
        int most_significant = shiftRegister[0];
        int tap_index = shiftRegister.length - 1 - tap;
        int tap_result = shiftRegister[tap];
        int number_to_add_in = shiftRegister[0] ^ shiftRegister[tap_index];
        for (int i = 0; i < shiftRegister.length-1; i++) {
            shiftRegister[i] = shiftRegister[i+1];
        }
        shiftRegister[shiftRegister.length-1] = number_to_add_in;
        return number_to_add_in;
    }

    /**
     * generate
     * @param k
     * @return
     * Description:
     */
    @Override
    public int generate(int k) {
        String ans = "";

        for (int i = 0; i < k; i++) {
            ans = ans + Integer.toString(shift());
        }
        int number = Integer.parseInt(ans, 2);
        return number;
    }

    /**
     * Returns the integer representation for a binary int array.
     * @param array
     * @return
     */
    private int toDecimal(int[] array) {
        // TODO:
        return 0;
    }
}
