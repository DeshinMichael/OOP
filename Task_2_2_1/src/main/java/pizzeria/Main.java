package pizzeria;

import com.fasterxml.jackson.databind.ObjectMapper;
import pizzeria.config.PizzeriaConfig;
import pizzeria.lifecycle.Pizzeria;

import java.io.InputStream;

public class Main {
    public static void main(String[] args) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        InputStream is = Main.class.getClassLoader().getResourceAsStream("config.json");
        if (is == null) {
            System.err.println("config.json not found in resources!");
            return;
        }
        PizzeriaConfig config = mapper.readValue(is, PizzeriaConfig.class);
        Pizzeria pizzeria = new Pizzeria(config);
        pizzeria.start();
    }
}
