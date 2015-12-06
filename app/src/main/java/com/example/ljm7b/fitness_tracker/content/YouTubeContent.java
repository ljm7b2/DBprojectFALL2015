package com.example.ljm7b.fitness_tracker.content;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/***********************************************************************************
 * The MIT License (MIT)

 * Copyright (c) 2015 Scott Cooper

 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:

 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 ***********************************************************************************/


/**
 * A Helper class for providing mock data to the app.
 * In a real world scenario you would either hard code the video ID's in the strings file or
 * retrieve them from a web service.
 */
public class YouTubeContent {

    /**
     * An array of YouTube videos
     */
    public static List<YouTubeVideo> ITEMS = new ArrayList<>();

    /**
     * A map of YouTube videos, by ID.
     */
    public static Map<String, YouTubeVideo> ITEM_MAP = new HashMap<>();


    //area to create different playlists
    //YOGA




        static {



                addItem(new YouTubeVideo("MmLypsvVBqQ", "How To WALL FLIP - Free Running Tutorial"));
                addItem(new YouTubeVideo("q5nyrD4eM64", "Jillian Michaels: Yoga Meltdown Level 1"));
                addItem(new YouTubeVideo("mEyoH5FV03s", "Clean, Part 1, How To, Olympic Weightlifting"));
                addItem(new YouTubeVideo("3_GHdAs3DCY", "8 Muscle Building Exercises for Beginners"));
                addItem(new YouTubeVideo("1fztE4mK7C0", "20 Minute Full Body Stretching Routine for Flexibility & Pain Relief, How to Stretch, Beginners Yoga"));
                addItem(new YouTubeVideo("r_sdocwSpQw", "Heavy Bag Boxing Drills - The 30-30-30"));
                addItem(new YouTubeVideo("fcN37TxBE_s", "Fat Burning Cardio Workout - 37 Minute Fitness Blender Cardio Workout at Home"));
                addItem(new YouTubeVideo("XHSSLASOwu8", "Judo: Introduction for Beginners"));
                addItem(new YouTubeVideo("oUqgPSZmhro", "7 Styles of Barbell Curls For Bigger Biceps"));
                addItem(new YouTubeVideo("2Gj3d4E-qeM", "THE HISTORY OF COMPUTER HACKING"));
                addItem(new YouTubeVideo("Fb3gn5GsvRk", "Introduction to Quantum Computers"));
                addItem(new YouTubeVideo("YX40hbAHx3s", "P vs. NP and the Computational Complexity Zoo"));
                addItem(new YouTubeVideo("uDBZnaoJVlk", "IBM Watson: Smartest Machine ever built"));
                addItem(new YouTubeVideo("34sEX6VM9sU", "Life in The Universe Documentary"));


    }


    private static void addItem(final YouTubeVideo item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    /**
     * A POJO representing a YouTube video
     */
    public static class YouTubeVideo {
        public String id;
        public String title;




        public YouTubeVideo(String id, String content) {
            this.id = id;
            this.title = content;
        }

        @Override
        public String toString() {
            return title;
        }
    }
}