package com.mss.group3.smartshare.utility;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


/**
 * Created by inder on 2016-02-28.
 * This class will find the driving distance and duration by geo points
 */
public class DistanceAndTimeApiCall {

    //private constructor
    DistanceAndTimeApiCall() {
    }

    double lattitudeOne;
    double lattitudeTwo;
    double longitudeOne;
    double longitudeTwo;
    //in metric unit
    double distance;
    double duration;


    //public constructor
    public DistanceAndTimeApiCall(double lat1, double lon1, double lat2, double lon2) {
        lattitudeOne = lat1;
        lattitudeTwo = lat2;
        longitudeOne = lon1;
        longitudeTwo = lon2;
        distance = 0;
        duration = 0;
    }

    /**
     * execute thread to find distance and duration
     */
    public void calculate()
    {

        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    getDistance(lattitudeOne,longitudeOne, lattitudeTwo, longitudeTwo);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        thread.start();

        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public double getDistance()
    {
        return distance;
    }

    public double getDuration()
    {
        return duration;
    }


    private void getDistance(double lat1, double lon1, double lat2, double lon2) {
        distance = 0;
        duration = 0;
        String result_in_kms = "";
        String url = "http://maps.google.com/maps/api/directions/xml?origin="
                + lat1 + "," + lon1 + "&destination=" + lat2 + "," + lon2 + "&sensor=false&units=metric";
        String tag[] = {"duration", "distance"};
        HttpResponse response = null;
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();
            HttpPost httpPost = new HttpPost(url);
            response = httpClient.execute(httpPost, localContext);
            InputStream is = response.getEntity().getContent();
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(is);
            if (doc != null) {
                NodeList nl;
                ArrayList args = new ArrayList();
                for (String s : tag) {
                    nl = doc.getElementsByTagName(s);
                    if (nl.getLength() > 0) {
                        Node node = nl.item(nl.getLength() - 1);
                        args.add(node.getTextContent());
                    } else {
                        args.add(" - ");
                    }
                }

                //notofication for outside 

                String timeFind[] = String.valueOf(args.get(0)).split("\\r?\\n");
                String distanceFind[] = String.valueOf(args.get(1)).split("\\r?\\n");

                for (String s:timeFind) {
                    if(s.contains("hour") || s.contains("min"))
                    {

                        String[] splited = s.split(" ");
                        if(splited.length > 0)
                        {
                            int length = splited.length;

                            boolean found = false;
                            for(int i = 0; i <length; i++)
                            {
                                if(splited[i].contains("min"))
                                {
                                    duration += Double.parseDouble(splited[i-1]);
                                    continue;
                                }

                                if(splited[i].contains("hour"))
                                {
                                    duration += Double.parseDouble(splited[length -4])*60;
                                }
                            }
                        }
                    }
                }

                for (String s:distanceFind) {
                    if (s.contains("Km") || s.contains("m")) {

                        String[] splited = s.split(" ");
                        if (splited.length > 0) {
                            int length = splited.length;

                            boolean found = false;
                            for (int i = 0; i < length; i++) {
                                if (splited[i].contains("km")) {
                                    distance += Double.parseDouble(splited[i - 1]) * 1000;
                                    continue;
                                }

                                if (splited[i].contains("m")) {
                                    distance += Double.parseDouble(splited[i - 1]);
                                }
                            }
                        }
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
