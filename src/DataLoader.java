import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;
import java.util.stream.Collectors;

public class DataLoader {

    /**
     * Reads the CSV at filePath and builds a MovieGraph.
     * Each line becomes one Movie (with shared Person flyweights),
     * then we link them by any shared people.
     */
    public static MovieGraph loadMovieData(String filePath) {
        // create the graph that we’ll fill
        MovieGraph movieGraph = new MovieGraph();

        // try-with-resources so the file always closes
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            String[] headers = null;
            boolean firstLine = true;

            // read each line of the CSV
            while ((line = br.readLine()) != null) {
                // the first line is the header row
                if (firstLine) {
                    headers = line.split(",");
                    firstLine = false;
                    continue;  // skip to the next line
                }

                // split into columns, handling quoted commas
                String[] values = parseCsvLine(line);

                // only parse if we got the expected number of columns
                if (values.length >= headers.length) {
                    Movie movie = parseMovieFromCsv(headers, values);
                    if (movie != null) {
                        movieGraph.addMovie(movie);
                    }
                }
            }
        } catch (IOException e) {
            // if anything goes wrong, print the stack trace for debugging
            e.printStackTrace();
        }

        // return the fully built graph
        return movieGraph;
    }

    /**
     * Turns one CSV row (headers + values) into a Movie object.
     * We use MovieFlyweight.getPerson(...) to share Person instances.
     */
    private static Movie parseMovieFromCsv(String[] headers, String[] values) {
        try {
            // map each column name to its index for easy lookup
            Map<String, Integer> headerMap = new HashMap<>();
            for (int i = 0; i < headers.length; i++) {
                headerMap.put(headers[i].trim(), i);
            }

            // basic fields
            String title = getValue("original_title", headerMap, values);
            int year = parseYear(getValue("release_date", headerMap, values));
            List<String> genres = Arrays.asList(getValue("genres", headerMap, values).split("\\|"));

            // safely parse each role
            List<MovieFlyweight.Person> directors = Stream.of(getValue("director",
                            headerMap, values).split("\\|"))
                    .map(String::trim).filter(name -> !name.isEmpty())
                    .map(name -> MovieFlyweight.getPerson(name, "director"))
                    .collect(Collectors.toList());

            List<MovieFlyweight.Person> actors = Stream.of(getValue("cast",
                            headerMap, values).split("\\|"))
                    .map(String::trim).filter(name -> !name.isEmpty())
                    .map(name -> MovieFlyweight.getPerson(name, "actor"))
                    .collect(Collectors.toList());



            return new Movie(
                    title,
                    year,
                    genres,
                    directors,
                    actors
            );

        } catch (Exception e) {
            System.err.println("Error parsing movie: " + e.getMessage());
            return null;
        }
    }


    /**
     * Safely fetches a column value by header name.
     * Returns empty string if the column is missing.
     */
    private static String getValue(String header,
                                   Map<String, Integer> headerMap,
                                   String[] values) {
        Integer index = headerMap.get(header);
        return (index != null && index < values.length)
                ? values[index]
                : "";
    }

    /**
     * Parses a date string like "2020-05-01" into its year (2020).
     * If the date is malformed, returns 0.
     */
    private static int parseYear(String date) {
        try {
            if (date == null || date.isEmpty()) {
                return 0;
            }

            // Handle different date formats:
            // 1. "6/9/2015" (month/day/year)
            // 2. "2015-06-09" (ISO format)
            // 3. Just "2015" (year only)

            if (date.contains("/")) {
                // Format: MM/DD/YYYY
                String[] parts = date.split("/");
                if (parts.length == 3) {
                    return Integer.parseInt(parts[2]);
                }
            } else if (date.contains("-")) {
                // Format: YYYY-MM-DD
                return Integer.parseInt(date.substring(0, 4));
            }

            // Try parsing as plain year
            return Integer.parseInt(date);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * Splits a CSV line into fields, respecting quoted commas.
     * E.g. "One, Two",Three → ["One, Two", "Three"]
     */
    private static String[] parseCsvLine(String line) {
        List<String> parts = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder sb = new StringBuilder();

        for (char c : line.toCharArray()) {
            if (c == '"') {
                inQuotes = !inQuotes;  // toggle quote mode
            } else if (c == ',' && !inQuotes) {
                // comma outside quotes: finish this field
                parts.add(sb.toString());
                sb = new StringBuilder();
            } else {
                sb.append(c);
            }
        }
        // add the last field
        parts.add(sb.toString());
        return parts.toArray(new String[0]);
    }
}
