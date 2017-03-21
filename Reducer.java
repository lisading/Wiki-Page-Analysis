import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * 15619 Project 1.2
 * Author: Zhiyi (Lisa) Ding, zhiyid
 * Date: Feb 04, 2017
 */
public class Reducer {

    // Define a constant:
    // Required page-view threshold is 100,000; Change the number for testing purpose.
    private static final int PAGE_VIEW_THRESHOLD = 100000;

    public static void main (String args[]) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            //BufferedReader br = new BufferedReader(new FileReader("output.txt"));

            String input;

            // Current page title
            String current_page_title = null;

            int[] current_count_views = new int[31];

            // There are 30 days in 2016.11. Initialize count_view of each date to 0.
            for (int i = 1; i < 31; i++) {
                current_count_views[i] = 0;
            }

            while ((input = br.readLine()) != null) {
                try {
                    String[] line = input.split("\t");
                    String page_title = line[0];
                    int date = Integer.parseInt(line[1]);
                    int count_views = Integer.parseInt(line[2]);

                    if (current_page_title != null && current_page_title.equals(page_title)) {
                        current_count_views[date - 20161100] += count_views;
                    } else {
                        if (current_page_title != null) {
                            int total_views = 0;
                            for (int i = 1; i < 31; i++) {
                                total_views += current_count_views[i];
                            }
                            // For every article that has over page-views threshold (100,000)
                            // Print line containing per date page-view data as output
                            if (total_views > PAGE_VIEW_THRESHOLD) {
                                String output = "";

                                output = output + total_views + "\t" + current_page_title + "\t";
                                int i;
                                for (i = 1; i < 30; i++) {
                                    output = output + (i + 20161100) + ":" + current_count_views[i] + "\t";
                                }
                                i = 30;
                                output = output + (i + 20161100) + ":" + current_count_views[i];

                                System.out.println(output);
                            }
                        }

                        current_page_title = page_title;

                        for (int i = 1; i < 31; i++) {
                            current_count_views[i] = 0;
                        }
                        current_count_views[date - 20161100] = count_views;
                    }

                } catch (NumberFormatException ignored) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}