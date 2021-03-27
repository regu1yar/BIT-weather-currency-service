package bit.utils.help;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ResourceFileReaderImpl implements ResourceFileReader {
    @Override
    public String getResourceFileContent(String filePath) throws IOException {
        InputStream inputStream = null;
        String content;
        try {
            inputStream = this.getClass().getResourceAsStream(filePath);
            content = readFromInputStream(inputStream);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return content;
    }

    private String readFromInputStream(InputStream inputStream)
            throws IOException {
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br
                     = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        return resultStringBuilder.toString();
    }
}
