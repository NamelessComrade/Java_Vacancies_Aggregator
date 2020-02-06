package project;

import project.model.*;
import project.view.HtmlView;

import java.io.IOException;


public class Aggregator {
    public static void main(String[] args) throws IOException
    {
        HtmlView view = new HtmlView(); // VIEW presents data from MODEL to user. Creates HTML file with result.
        Provider providerHH = new Provider(new HHStrategy()); // PROVIDER wraps strategies. Unifies work with them.
        Provider providerMK = new Provider(new MoikrugStrategy()); // Not included in PROVIDER, because provides no relevant output for Krasnoyarsk.
        Provider providerRR = new Provider(new RosrabotaStrategy()); // RosRabota CAN SHOW ERROR MESSAGE if there will be large amount of requests from this app.
        Model model = new Model(view, providerHH, providerRR); // Gathers data. Takes in one VIEW and any amount of PROVIDERS.
        Controller controller = new Controller(model); // Takes in user input. In this app users input emulated through controller.onCitySelect();
        view.setController(controller); // VIEW also should have CONTROLLER to work.
        view.userCitySelectEmulationMethod(); // Emulates user input.
    }
}
