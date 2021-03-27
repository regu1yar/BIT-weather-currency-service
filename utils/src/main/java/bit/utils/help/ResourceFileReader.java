package bit.utils.help;

import java.io.IOException;

public interface ResourceFileReader {
    String getResourceFileContent(String filePath) throws IOException;
}
