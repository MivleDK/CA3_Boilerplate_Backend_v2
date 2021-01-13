package utils;

import java.io.IOException;
import utils.HttpUtils;

public class FetchTester {                  //Testing file to check the return from a fetch url

    public static void main(String[] args) throws IOException {

        String chuck = HttpUtils.fetchData("https://api.chucknorris.io/jokes/random");
        String dad = HttpUtils.fetchData("https://icanhazdadjoke.com");

        System.out.println("JSON fetched from chucknorris:");
        System.out.println(chuck);
        System.out.println("JSON fetched from dadjokes:");
        System.out.println(dad);
    }
}
