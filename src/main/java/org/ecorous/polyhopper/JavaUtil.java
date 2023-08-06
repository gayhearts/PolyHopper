package org.ecorous.polyhopper;

import java.util.Arrays;
import java.util.List;

public class JavaUtil {
	public List<String> getList(String... args) {
		return Arrays.stream(args).toList();
	}
}
