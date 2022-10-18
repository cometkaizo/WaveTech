package me.cometkaizo.util;
/*
import me.hydrophobia.subspacebubble.resources.IllegalResourceException;
import org.jetbrains.annotations.Nullable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;*/

@SuppressWarnings("unused")
public final class JSONUtils {
/*
    private static final JSONParser jsonParser = new JSONParser();

    // TODO: 2022-10-07 cleanup
    private JSONUtils() {

    }



    public static JSONObject parse(File file) {
        Objects.requireNonNull(file, "File cannot be null");
        if (!file.exists()) throw new IllegalArgumentException("File does not exist");
        if (file.isDirectory()) throw new IllegalArgumentException("File cannot be directory");

        try (FileReader reader = new FileReader(file)) {
            return (JSONObject) jsonParser.parse(reader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new IllegalResourceException("File could not be parsed", e);
        }

    }


    /**
     * Gets the value that is mapped to the key, otherwise {@code null}
     * @param file the file to find the value in
     * @param key the key to which a value is mapped
     * @return the value to which the value is mapped. Can either be a {@link JSONObject}, {@link JSONArray}, or {@code null}
     *
    @Nullable
    public static Object read(File file, String key) {
        return parse(file).get(key);

    }

    public static boolean write(File file, JSONObject json) {

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(json.toJSONString());
            writer.flush();
            return true;
        } catch (IOException i) {
            LogUtils.report(i, "an IOException occurred while trying to write JSON");
            return false;
        }

    }

    public static boolean write(File file, JSONArray json) {

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(json.toJSONString());
            writer.flush();
            return true;
        } catch (IOException i) {
            LogUtils.report(i, "an IOException occurred while trying to write JSON");
            return false;
        }

    }
*/
}
