import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class PlayerRandom
{
    public static void main(String[] args)
    {
        Random RNG = new Random(36);
        Scanner in = new Scanner(System.in);

        String color = in.next(); in.nextLine();
        System.err.println("color: " + color);

        while (true)
        {
            String last_action = in.nextLine();
            System.err.println("last action: "+last_action);

            int actions = in.nextInt(); in.nextLine();
            System.err.println("actions: "+actions);
            ArrayList<String> legals = new ArrayList<>();
            for (int y = 0; y < actions; ++y)
            {
                String line = in.nextLine();
                System.err.println(line);
                legals.add(line);
            }

//            int i = RNG.nextInt(actions);
//            System.out.println(legals.get(i) + " hello :)");
            System.out.println("random");
        }
    }
}
