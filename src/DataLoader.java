import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class DataLoader {
    public static MovieGraph loadMovieData(String filePath) {
        MovieGraph movieGraph = new MovieGraph();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            String[] headers = null;
            boolean firstLine = true;

            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    headers = line.split(",");
                    firstLine = false;
                    continue;
                }

                String[] values = parseCsvLine(line);

                // Ensure we have enough columns
                if (values.length >= headers.length) {
                    Movie movie = parseMovieFromCsv(headers, values);
                    if (movie != null) {
                        movieGraph.addMovie(movie);
                    }
                }
            }

            movieGraph.buildConnections();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return movieGraph;
    }

    private static Movie parseMovieFromCsv(String[] headers, String[] values) {
        try {
            // Map headers to indices
            Map<String, Integer> headerMap = new HashMap<>();
            for (int i = 0; i < headers.length; i++) {
                headerMap.put(headers[i].trim(), i);
            }

            // Extract basic info
            String title = getValue("original_title", headerMap, values);
            int year = parseYear(getValue("release_date", headerMap, values));

            // Extract lists from pipe-separated strings
            List<String> genres = Arrays.asList(
                    getValue("genres", headerMap, values).split("\\|"));

            List<String> directors = Arrays.asList(
                    getValue("directors", headerMap, values).split("\\|"));

            List<String> actors = Arrays.asList(
                    getValue("actors", headerMap, values).split("\\|"));

            List<String> writers = Arrays.asList(
                    getValue("writers", headerMap, values).split("\\|"));

            List<String> cinematographers = Arrays.asList(
                    getValue("cinematographers", headerMap, values).split("\\|"));

            List<String> composers = Arrays.asList(
                    getValue("composers", headerMap, values).split("\\|"));

            return new Movie(
                    title,
                    year,
                    genres,
                    directors,
                    actors,
                    writers,
                    cinematographers,
                    composers
            );

        } catch (Exception e) {
            System.err.println("Error parsing movie: " + e.getMessage());
            return null;
        }
    }

    private static String getValue(String header, Map<String, Integer> headerMap, String[] values) {
        Integer index = headerMap.get(header);
        return (index != null && index < values.length) ? values[index] : "";
    }

    private static int parseYear(String date) {
        try {
            return date.length() >= 4 ? Integer.parseInt(date.substring(0, 4)) : 0;
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private static String[] parseCsvLine(String line) {
        List<String> values = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder sb = new StringBuilder();

        for (char c : line.toCharArray()) {
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                values.add(sb.toString());
                sb = new StringBuilder();
            } else {
                sb.append(c);
            }
        }

        values.add(sb.toString());
        return values.toArray(new String[0]);
    }
}