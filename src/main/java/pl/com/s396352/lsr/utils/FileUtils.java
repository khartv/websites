package main.java.pl.com.s396352.lsr.utils;

import net.sourceforge.jFuzzyLogic.FIS;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by khartv on 04.05.2018.
 */
public class FileUtils {
    public List<String> getURLsList()
    {
        BufferedReader reader = null;
        List<String> list = new ArrayList();
        try
        {
            InputStream in = getClass().getResourceAsStream("/main/resources/links.txt");
            reader = new BufferedReader(new InputStreamReader(in));

            String line = null;
            while ((line = reader.readLine()) != null && !line.startsWith("#"))
            {
                list.add(line);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (reader != null)
            {
                try
                {
                    reader.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }

        return list;
    }

    public FIS getUrlSimFis()
    {
        return getFis("/main/resources/fcl/url_simmilarity.fcl");
    }
    public FIS getStrSimFis()
    {
        return getFis("/main/resources/fcl/structural_simmilarity.fcl");
    }
    public FIS getGenSimFis()
    {
        return getFis("/main/resources/fcl/general_simmilarity.fcl");
    }

    private FIS getFis(String fileName)
    {

        InputStream in = getClass().getResourceAsStream(fileName);
        FIS fis = FIS.load(in, true);
        //FIS fis = FIS.load(fileName,true);

        // Error while loading?
        if( fis == null ) {
            System.err.println("Can't load file: '" + fileName + "'");
            return null;
        }

        return fis;
    }
}
