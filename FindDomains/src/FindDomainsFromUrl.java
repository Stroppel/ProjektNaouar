import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

public class FindDomainsFromUrl {

	private static ArrayList<String> checkLineContainsA(String rLine) {
		ArrayList<String> linksFound = new ArrayList<String>();
		if (rLine.contains("<a")) {
			String[] aPartsArray = rLine.split("<a");
			ArrayList<String> httpLinksFound = FindDomainsFromUrl.findHttpLinks(aPartsArray);
			if (!httpLinksFound.isEmpty()) {
				linksFound.addAll(httpLinksFound);
			}
		}
		return linksFound;
	}

	private static ArrayList<String> findHttpLinks(String[] array) {
		List<String> linesList = Arrays.asList(array);
		ArrayList<String> allLinks = new ArrayList<>();
		for (String oneLine : linesList) {
			if (oneLine.contains("href=\"http") == true) {
				String[] splitedLine = oneLine.split("\"");
				for (String onePart : splitedLine) {
					if (onePart.contains("http")) {
						allLinks.add(onePart);
					}
				}
			}
		}
		return allLinks;
	}

	private static Set<String> filterDomain(ArrayList<String> linksFound) {
		Set<String> domainSet = new HashSet<String>();
		for (String link : linksFound) {
			String partofLink = link.substring(4);
			if (partofLink.charAt(0) == 's') {
				domainSet.add(link.substring(8).split("/")[0]);
			} else {
				domainSet.add(link.substring(7).split("/")[0]);
			}
		}
		return domainSet;
	}

	public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException {

		String givenUrl = "https://wawalove.wp.pl";
		URL url = new URL(givenUrl);
		BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
		String rLine = null;
		ArrayList<String> linksFound = new ArrayList<>();
		while ((rLine = reader.readLine()) != null) {
			linksFound.addAll(checkLineContainsA(rLine));
		}
		Set<String> expectedDomains = null; // List of domains without duplicates
		if (!linksFound.isEmpty()) {
			expectedDomains = filterDomain(linksFound); 
		}
		for (String domain : expectedDomains) {
			System.out.println(domain);
		}
	}
}
