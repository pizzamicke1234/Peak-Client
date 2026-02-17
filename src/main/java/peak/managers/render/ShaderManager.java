package peak.managers.render;

import org.lwjgl.opengl.GL20;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ShaderManager {
    private final int programId;

    public ShaderManager(String fragmentShaderLoc) {
        int program = GL20.glCreateProgram();
        try {
            int fragmentShader = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
            GL20.glShaderSource(fragmentShader, readFile(fragmentShaderLoc));
            GL20.glCompileShader(fragmentShader);

            if (GL20.glGetShaderi(fragmentShader, GL20.GL_COMPILE_STATUS) == 0) {
                System.err.println("Shader Error: " + GL20.glGetShaderInfoLog(fragmentShader, 1024));
            }

            GL20.glAttachShader(program, fragmentShader);
            GL20.glLinkProgram(program);
        } catch (Exception e) { e.printStackTrace(); }
        this.programId = program;
    }

    public void use() { GL20.glUseProgram(programId); }
    public void stop() { GL20.glUseProgram(0); }

    public void setUniform(String name, float... args) {
        int loc = GL20.glGetUniformLocation(programId, name);
        if (loc == -1) return;

        switch (args.length) {
            case 1: GL20.glUniform1f(loc, args[0]); break;
            case 2: GL20.glUniform2f(loc, args[0], args[1]); break;
            case 3: GL20.glUniform3f(loc, args[0], args[1], args[2]); break;
            case 4: GL20.glUniform4f(loc, args[0], args[1], args[2], args[3]); break;
        }
    }

    private String readFile(String location) {
        StringBuilder source = new StringBuilder();
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(location);
            if (inputStream == null) {
                System.err.println("Shader-File not found: " + location);
                return "";
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                source.append(line).append("\n");
            }
            reader.close();
        } catch (IOException e) {
            System.err.println("Error reading Shaders!");
            e.printStackTrace();
        }
        return source.toString();
    }

}
