import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

class Player
{
    public static void main(String[] args)
    {
        Random RNG = new Random(36);
        Scanner in = new Scanner(System.in);

        String color = in.next(); in.nextLine();
        System.err.println("color: " + color);
        int mult = color.equals("red") ? -1 : 1;

        while (true)
        {
            String last_action = in.nextLine();
            System.err.println("last action: "+last_action);

            int actions = in.nextInt(); in.nextLine();

            ArrayList<String> forward = new ArrayList<>();
            ArrayList<String> neutral = new ArrayList<>();
            ArrayList<String> backward = new ArrayList<>();
            for (int y = 0; y < actions; ++y)
            {
                String line = in.nextLine();
                String[] splitted = line.trim().split(" ");
                int y1 = mult * Integer.parseInt(splitted[1]);
                int y2 = mult * Integer.parseInt(splitted[3]);
                //System.err.println(line);
                //legals.add(line);
                if (y1 < y2) forward.add(line);
                if (y1 == y2) neutral.add(line);
                if (y1 > y2) backward.add(line);
            }

            System.err.println("actions forward: "+forward.size());
            for (String a:forward) System.err.println(a);
            System.err.println("actions neutral: "+neutral.size());
            for (String a:neutral) System.err.println(a);
            System.err.println("actions backward: "+backward.size());
            for (String a:backward) System.err.println(a);

            if (forward.size() != 0) System.out.println(forward.get(RNG.nextInt(forward.size())) );
            else if (neutral.size() != 0) System.out.println(neutral.get(RNG.nextInt(neutral.size())) );
            else System.out.println(backward.get(RNG.nextInt(backward.size())) );

        }
    }
}
