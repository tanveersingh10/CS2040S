public class HelloWorld {
    public static void main(String[] args) {
        String name = "Tanveer";
        String joke = "There are only 10 types of programmers. Those who understand binary and those who don't.";
        int oneA = 3125;
        String oneB = "it returns argA to the power of argB.";
        bruh(name, joke, oneA, oneB);


    }

    static void bruh(String name, String joke, int oneA, String oneB) {
        System.out.println("Hi my name is " + name + ". My fav coding joke is: " + joke +
                ". The ans to 1a is " + oneA + " and for 1b " + oneB);
    }
}
