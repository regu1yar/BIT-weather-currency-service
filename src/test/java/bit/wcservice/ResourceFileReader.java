package bit.wcservice;

import java.io.IOException;

public interface ResourceFileReader {
    String getResourceFileContent(String filePath) throws IOException;
}
