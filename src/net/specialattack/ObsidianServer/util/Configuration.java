
package net.specialattack.ObsidianServer.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;

import net.specialattack.ObsidianServer.Server;

public class Configuration {
    private HashMap<String, String> values;
    private File file;

    public Configuration(File file) {
        this.values = new HashMap<String, String>();
        if (file == null) {
            throw new InvalidParameterException("file can not be null!");
        }

        this.file = file.getAbsoluteFile();

        this.loadConfig();
    }

    public void loadConfig() {
        if (!this.file.exists()) {
            Server.log.log(Level.INFO, "Creating new config file...");

            this.file.getParentFile().mkdirs();

            try {
                this.file.createNewFile();
            }
            catch (IOException e) {
                Server.log.log(Level.SEVERE, "Failed loading config!", e);
                return;
            }
        }

        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(this.file));
        }
        catch (FileNotFoundException e) {
            Server.log.log(Level.SEVERE, "Failed loading config!", e);
            return;
        }

        String line = "";

        try {
            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.startsWith("#")) {
                    continue;
                }

                int index = line.indexOf("=");

                if (index <= 0) {
                    continue;
                }

                String name = line.substring(0, index).trim();

                String value = line.substring(index + 1).trim();

                this.values.put(name, value);
            }

            reader.close();
        }
        catch (IOException e) {
            Server.log.log(Level.SEVERE, "Failed loading config!", e);
            return;
        }
    }

    public void saveConfig() {
        if (!this.file.exists()) {
            Server.log.log(Level.INFO, "Creating new config file...");

            this.file.getParentFile().mkdirs();

            try {
                this.file.createNewFile();
            }
            catch (IOException e) {
                Server.log.log(Level.SEVERE, "Failed saving config!", e);
                return;
            }
        }

        BufferedWriter writer;
        try {
            writer = new BufferedWriter(new FileWriter(this.file));
        }
        catch (FileNotFoundException e) {
            Server.log.log(Level.SEVERE, "Failed saving config!", e);
            return;
        }
        catch (IOException e) {
            Server.log.log(Level.SEVERE, "Failed saving config!", e);
            return;
        }

        Iterator<String> iterator = this.values.keySet().iterator();

        try {
            while (iterator.hasNext()) {
                String name = iterator.next();
                writer.write(name);
                writer.write("=");
                writer.write(this.values.get(name));

                writer.newLine();
            }

            writer.flush();
            writer.close();
        }
        catch (IOException e) {
            Server.log.log(Level.SEVERE, "Failed saving config!", e);
            return;
        }
    }

    public void set(String name, Object obj) {
        this.values.put(name, obj.toString());

        this.saveConfig();
    }

    public String getString(String name, String def) {
        if (!this.values.containsKey(name)) {
            this.set(name, def);

            return def;
        }
        else {
            String value = this.values.get(name);

            return value;
        }
    }

    public boolean getBoolean(String name, boolean def) {
        if (!this.values.containsKey(name)) {
            this.set(name, def);

            return def;
        }
        else {
            String value = this.values.get(name);

            boolean result = def;

            if (value.equalsIgnoreCase("true")) {
                result = true;
            }
            if (value.equalsIgnoreCase("false")) {
                result = false;
            }
            if (value.equalsIgnoreCase("1")) {
                result = true;
            }
            if (value.equalsIgnoreCase("0")) {
                result = false;
            }
            if (value.equalsIgnoreCase("yes")) {
                result = true;
            }
            if (value.equalsIgnoreCase("no")) {
                result = false;
            }

            return result;
        }
    }

    public int getInt(String name, int def) {
        if (!this.values.containsKey(name)) {
            this.set(name, def);

            return def;
        }
        else {
            String value = this.values.get(name);

            int result = 0;
            try {
                result = Integer.parseInt(value);
            }
            catch (NumberFormatException ex) {
                this.set(name, def);

                return def;
            }
            return result;
        }
    }
}
