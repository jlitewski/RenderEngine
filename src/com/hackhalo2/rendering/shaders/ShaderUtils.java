package com.hackhalo2.rendering.shaders;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;

import org.lwjgl.opengl.ContextCapabilities;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GLContext;

import com.hackhalo2.rendering.RenderEngine;


public class ShaderUtils {
	
	public static String glslVersion = GL11.glGetString(GL20.GL_SHADING_LANGUAGE_VERSION); //GLSL Version
	public static int maxDrawBuffers = GL11.glGetInteger(GL20.GL_MAX_DRAW_BUFFERS); //Maximum Draw Buffers Supported
	public static int maxColorAttachments = GL11.glGetInteger(GL30.GL_MAX_COLOR_ATTACHMENTS); //Maximum Color Attachments
	
	private ShaderUtils() { }
	
	/* Taken with love from Slick2D */
	public static boolean isSupported() {
		ContextCapabilities c = GLContext.getCapabilities();
		return c.GL_ARB_shader_objects && c.GL_ARB_vertex_shader && c.GL_ARB_fragment_shader;
	}

	protected static int[] compileShader(ShaderObject shader) {
		int vs = GL20.glCreateShader(GL20.GL_VERTEX_SHADER),
				fs = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);

		//The Vertex Shader Compile Test
		System.out.println("Compiling the vertex shader...");
		if(RenderEngine._debug) {
			System.out.println();
			System.out.println("Appending the raw shader to the log...");
			System.out.println(shader.vertex());
			System.out.println();
		}

		GL20.glShaderSource(vs, shader.vertex());
		GL20.glCompileShader(vs);
		compileLogCheck(vs);

		//The Fragment Shader Compile Test
		System.out.println("Compiling the fragment shader...");
		if(RenderEngine._debug) {
			System.out.println();
			System.out.println("Appending the raw shader to the log...");
			System.out.println(shader.fragment());
			System.out.println();
		}

		GL20.glShaderSource(fs, shader.fragment());
		GL20.glCompileShader(fs);
		compileLogCheck(fs);

		return new int[] {vs, fs};
	}

	private static void compileLogCheck(int shader) {
		String infoLog = GL20.glGetShaderInfoLog(shader, GL20.glGetShader(shader, GL20.GL_INFO_LOG_LENGTH));

		if(GL20.glGetShader(shader, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE)
			throw new RuntimeException("Failure in compiling the shader.\nError log:\n"+infoLog);
		else {
			System.out.println("Compiling the shader was successful.");
			if(infoLog != null && !(infoLog = infoLog.trim()).isEmpty())
				System.out.println("Log:\n" + infoLog);
		}
	}

	protected static void linkProgram(int program) {
		GL20.glLinkProgram(program);

		String infoLog = GL20.glGetProgramInfoLog(program, GL20.glGetProgram(program, GL20.GL_INFO_LOG_LENGTH));

		if(GL20.glGetProgram(program, GL20.GL_LINK_STATUS) == GL11.GL_FALSE)
			throw new RuntimeException("Failure in linking program. Error log:\n" + infoLog);
		else {
			System.out.println("Linking the program was successful.");
			if(infoLog != null && !(infoLog = infoLog.trim()).isEmpty())
				System.out.println(" Log:\n" + infoLog);
		}
	}

	protected static CharSequence getTextFromURI(URI file) {
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(file.toURL().openStream(), "UTF-8"));
			String line;
			StringBuilder buffer = new StringBuilder();
			while ((line = in.readLine()) != null) {
				buffer.append(line).append('\n');
			}
			// Chomp the last newline
			buffer.deleteCharAt(buffer.length() - 1);
			return buffer;
		} catch(Exception e) {
			throw new RuntimeException("Something went wrong!");
		} finally {
			try { 
				in.close();
			} catch (IOException e) { }
		}
	}

}
