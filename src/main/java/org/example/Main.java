package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        List<String> teams = new ArrayList<>();
        teams.add("Maccabi Haifa ");
        teams.add("Hapoel Be'er-Sheva ");
        teams.add("Maccabi Tel-Aviv  ");
        teams.add("Bnei-Sakhnin  ");
        teams.add("Maccabi Netanya  ");
        teams.add("Hapoel Hadera");
        teams.add("Hapoel Tel-Aviv   ");
        teams.add("Ironi Kiriat-Shmona  ");
        teams.add("Hapoel Haifa  ");
        teams.add("M.S. Ashdod     ");
        teams.add("Hapoel Jerusalem  ");
        teams.add("Beitar Jerusalem     ");
        teams.add("Maccabi Petah-Tikva ");
        teams.add("Hapoel Nof HaGalil ");
        Scanner scanner = new Scanner(System.in);
        Document doc = null;
        String htmlCode;
        try {
            doc = Jsoup.connect("https://www.rsssf.org/tablesi/isra2022.html").get();
            System.out.println(doc);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (doc != null) {
            htmlCode = doc.toString();
            System.out.println("Enter the Team name");
            String team = scanner.nextLine();
            for (int i = 1; i <= 33; i++) {
                String roundNum = "Round " + i;
                String nextRoundNum = "Round " + (i + 1);
                boolean hasThisTeam = checkTeam(teams, team);
                String[] parts = new String[3];
                if (hasThisTeam) {
                    String[] searchArea = htmlCode.substring(htmlCode.indexOf(roundNum), htmlCode.indexOf(nextRoundNum)).split("\n");
                    String teamPresenceLine = searchTheLine(searchArea, team);
                    if (teamPresenceLine != "") {
                        Pattern pattern = Pattern.compile("(.*?)(\\d+-\\d+)(.*)"); // Pattern to match

                        Matcher matcher = pattern.matcher(teamPresenceLine);

                        if (matcher.find()) { // If a match is found
                            parts[0] = matcher.group(1).trim(); // First part until the numbers
                            parts[1] = matcher.group(2).trim(); // Second part (the numbers)
                            parts[2] = matcher.group(3).trim(); // Third part after the numbers
                        }
                        String result = getResult(parts, team);
                        System.out.println(team + " , (" + result + ") in " + roundNum);
                    }
                }

            }

        }
    }


    private static String getResult(String[]parts, String team) {
        String result = "Draw";
        String[] scores = parts[1].split("-");
        int scoreR = Integer.parseInt(scores[1]);
        int scoreL = Integer.parseInt(scores[0]);
        String teamL = parts[0];
        String teamR = parts[2];
        if (scoreR > scoreL) {
            if (teamR.equals(team)) {
                result = "Win";
            } else {
                result = "Lose";
            }
        }else if (scoreR<scoreL){
            if (teamL.equals(team)){
                result = "Win";
            }else {
                result = "Lose";
            }
        }
        return result;
    }

    private static String searchTheLine(String[] searchArea, String team) {
        String result = "";
        for (String s : searchArea){
            if (s.contains(team)){
                result = s;
                break;
            }
        }
        return result;
    }

    private static boolean checkTeam(List<String> teams, String team) {
        boolean result = false;
        for (String s : teams) {
            result = s.contains(team) || s.equalsIgnoreCase(team);
            if (result){
                break;
            }
        }
        return result;
    }
}