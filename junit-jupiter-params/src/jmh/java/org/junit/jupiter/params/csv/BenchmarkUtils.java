package org.junit.jupiter.params.csv;

abstract class BenchmarkUtils {

	static String[] generateValueArray(int lineCount, int columnCount) {
		return generateTextBlock(lineCount, columnCount).split("\n");
	}

	static String generateTextBlock(int lineCount, int columnCount) {
		StringBuilder sb = new StringBuilder();

		for (int i = 1; i <= lineCount; i++) {
			for (int j = 1; j <= columnCount; j++) {
				sb.append("V_").append(i).append('_').append(j);
				if (j == columnCount) {
					sb.append("\n");
				} else {
					sb.append(",");
				}
			}
		}

		return sb.toString();
	}
}
