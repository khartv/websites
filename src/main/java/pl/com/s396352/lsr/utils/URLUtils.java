package main.java.pl.com.s396352.lsr.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by khartv on 04.05.2018.
 */
public class URLUtils {

    public List<String> getWebsites(List<String> URLList) {
        List<String> result = new ArrayList();
        URL website = null;
        ReadableByteChannel rbc = null;
        for(String u : URLList)
        {
            try
            {
                website = new URL(u);
                BufferedReader in = new BufferedReader(new InputStreamReader(website.openStream()));

                String inputLine;
                StringBuilder output = new StringBuilder();
                while ((inputLine = in.readLine()) != null)
                {
                    output.append(inputLine);
                }
                in.close();
                result.add(output.toString());
            }
            catch(MalformedURLException e)
            {
                e.printStackTrace();
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }

        return result;
    }
}
