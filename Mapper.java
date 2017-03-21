import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 15619 Project 1.2
 * Author: Zhiyi (Lisa) Ding, zhiyid
 * Date: Feb 04, 2017
 */
public class Mapper {

    private static String[] namespace = {"Media:", "Special:", "Talk:", "User:",
            "User_talk:", "File:", "Gadget_definition_talk:", "Gadget_definition:",
            "Gadget_talk:", "TimedText:", "Gadget:", "TimedText_talk:",
            "Draft:", "Draft_talk:", "Template:", "Template_talk:", "Help:",
            "Help_talk:", "Category:",  "Category_talk:", "Portal:",  "Portal_talk:",
            "Wikipedia:", "Wikipedia_talk:", "Education_Program:",
            "File_talk:", "Education_Program_talk:", "MediaWiki:",
            "MediaWiki_talk:", "Book:", "Book_talk:", "Module:", "Module_talk:"};
    private static String[] mediaSuffix = {".jpg", ".gif", ".png", ".tif", ".jpeg",
            ".xcf", ".mid", ".ogg", ".ogv", ".svg", ".oga", ".wav", ".ico", ".txt",
             ".tiff", ".djvu", ".flac", ".opus", ".webm"};
    private static String[] specialPages = {"404.php", "Main_Page", "-"};

    private static Set<String> namespaceSet = new HashSet<>(Arrays.asList(namespace));
    private static Set<String> specialPagesSet = new HashSet<>(Arrays.asList(specialPages));

    public static void main (String args[]) {

        String line;

        // Initialise an empty array for for page_title and count_views after pre processing
        ArrayList<String[]> result = new ArrayList<>();

        try {
            // Read file from input in UTF-8
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            //BufferedReader br = new BufferedReader(new FileReader("data/pageviews-20161109-000000"));

            // Go through each line and clean the data sets
            // Remove rows based on the following 1-8 rules
            outerLoop:
            while ((line = br.readLine()) != null) {

                // We do data pre-processing processes as followings:
                // 1. URL normalization:
                // Decode the string. For example: Special%3ASearch --> Special:Search
                // Using PercentDecoder.java to deal with string decoding
                if (line.contains("%")) {
                    line = PercentDecoder.decode(line);
                }

                // 2. Dirty data handling:
                // Remove lines that don't have four columns
                // Split lines into 4 fields by space
                String[] field = line.split("\\s+");
                if (field.length != 4) {
                    continue;
                }

                String domain_code = field[0];
                String page_title = field[1];
                String count_views = field[2];

                // 3. Desktop/mobile Wikipedia sites:
                // Keep the rows only if the first column is exactly en or en.m
                if (!domain_code.equals("en") && !domain_code.equals("en.m")) {
                    continue;
                }

                // 4. Wikipedia namespaces
                // Remove special pages in Wikipedia, which have particular namespaces
                if (page_title.contains(":")){
                    if (namespaceSet.contains(page_title.split(":")[0] + ":")) {
                        continue;
                    }
                }

                // 5. Wikipedia article title limitation:
                // Filter out pages which title starting with lowercase English character.
                if (page_title.substring(0,1).matches("[abcdefghijklmnopqrstuvwxyz]")){
                    continue;
                }

                // 6. Miscellaneous filename extensions:
                // filter media files ending with specific suffixes
                for (String n : mediaSuffix) {
                    if (page_title.endsWith(n)) {
                        continue outerLoop;
                    }
                }

                // 7. Special pages:
                // Filter other special pages
                if (specialPagesSet.contains(page_title)) {
                    continue;
                }

                // 8. Wikipedia Disambiguation:
                // Filter pages with suffix _(disambiguation)
                if (page_title.matches(".*\\_\\(disambiguation\\)")) {
                    continue;
                }

                // Add page_title and count_views to the result,
                // if the data has not been filtered.
                String[] pair = {page_title, count_views};
                result.add(pair);
            }

            // Get the input filename from within a Mapper
            // s3://cmucc-datasets/wikipediatraf/201611/pageviews-20161109-000000.gz

            String fileName = System.getenv("mapreduce_map_input_file");
            //String fileName = "pageviews-20161109-000000";
            String date = fileName.split("-")[2];

            for (String[] s : result) {
                System.out.println(s[0] + "\t" + date + "\t" + s[1]);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
