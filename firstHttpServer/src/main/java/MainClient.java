import java.util.Scanner;

public class MainClient
{
    public static void main(String[] args) throws InterruptedException
    {
        Scanner keyboard = new Scanner(System.in);
        System.out.println("How many users would you like to simulate?");
        try
        {
            int simulations = Integer.valueOf(keyboard.nextLine());
            System.out.println("Starting to attack server");
            System.out.println("Press any key to stop!");
            ClientController controller = new ClientController(simulations);
            if(keyboard.nextLine() != null)
            {
                controller.shutdown();
            }
        }
        catch (NumberFormatException e)
        {
            System.out.println("next time type a number!");
        }
    }
}
