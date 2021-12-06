package io.github.twentyfirst.rpglepp.parser;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.github.twentyfirst.rpglepp.api.CopyBookReader;
import io.github.twentyfirst.rpglepp.api.SourceFile;
import io.github.twentyfirst.rpglepp.exception.MissingCopyBookException;
import io.github.twentyfirst.rpglepp.tools.Files;

public class DefaultCopyBookReader implements CopyBookReader {

	private static String[] COPY_EXTENSIONS = new String[] { ".RPGLECOPY", ".RPGLE" };

    private static Pattern NAME_PATTERN = Pattern.compile("(?:[A-Za-z/*]+,)?(\\w+)");

    private List<String> copyBookPath;

    public DefaultCopyBookReader(List<String> copyBookPath) {
        this.copyBookPath = copyBookPath;
    }

    @Override
    public SourceFile read(String copyBookName){
        Matcher m = NAME_PATTERN.matcher(copyBookName);
        if ( ! m.matches() || m.group(1) == null ) {
            throw new IllegalArgumentException(copyBookName + ": invalid copybook name");
        }
        String baseName = m.group(1).toUpperCase();
        for ( String ext: COPY_EXTENSIONS ) {
            try {
                String fileName = baseName + ext;
                String text = Files.readFile(fileName, copyBookPath);
                return new SourceFile(fileName, text);
            } catch (IOException e) {
            }
        }
        throw new MissingCopyBookException(baseName);
    }
    
}
