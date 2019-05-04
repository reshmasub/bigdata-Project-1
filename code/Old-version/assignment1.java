package bigdataAssignment1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

public class assignment1 {

	public static void main(String[] args) {

		final long startTime = System.currentTimeMillis(); // To calculate the time for program run to compare the
															// performance

		System.out.println("PROGRAM STARTED \n\n");
		HashMap<String, Integer> wordCountMap = new HashMap<String, Integer>();

		BufferedReader reader = null;

		try {

			reader = new BufferedReader(new FileReader("/Users/reshmasubramaniam/Downloads/DataSet/data_1GB.txt"));

			// Reading the first line into currentLine

			String currentLine = reader.readLine();

			while (currentLine != null) {
				// splitting the currentLine into words

				String[] words = currentLine.split(" ");

				for (String word : words) {
					// if word is already present in wordCountMap, then update its count

					if (wordCountMap.containsKey(word)) {
						wordCountMap.put(word, wordCountMap.get(word) + 1);
					}

					// else insert the word as key and 1 as value
					else {
						wordCountMap.put(word, 1);
					}
				}

				// Reading next line into currentLine

				currentLine = reader.readLine();
			}

			// Get all the entries of wordCountMap in the form of Set

			Set<Entry<String, Integer>> entrySet = wordCountMap.entrySet();

			// Create a List by passing the entrySet

			List<Entry<String, Integer>> list = new ArrayList<Entry<String, Integer>>(entrySet);

			// Sort the list in the decreasing order of values

			Collections.sort(list, new Comparator<Entry<String, Integer>>() {
				public int compare(Entry<String, Integer> e1, Entry<String, Integer> e2) {
					return (e2.getValue().compareTo(e1.getValue()));
				}
			});

			System.out.println("OUTPUT :");
			StringBuffer frequentWords = new StringBuffer();
			int count = 0;
			for (Entry<String, Integer> entry : list) {
				if (entry.getValue() > 1) {
					++count;
					if (count <= 100) {
						frequentWords.append(count + "." + entry.getKey() + " : " + entry.getValue() + "\n");
						System.out.println(count + "." + entry.getKey() + " : " + entry.getValue());
					} else {
						break;
					}
				}
			}
			final long duration = System.currentTimeMillis() - startTime;
			System.out.println("Duration: " + duration / 1000 + " Seconds ");
			frequentWords.append("Duration: " + duration / 1000 + " Seconds ");
			outputFileHelper(frequentWords);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close(); // Close the reader
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void outputFileHelper(StringBuffer sb) throws IOException {

		BufferedWriter writer = new BufferedWriter(new FileWriter("output_32GB.txt"));
		writer.write(sb.toString());
		writer.close();
	}
}