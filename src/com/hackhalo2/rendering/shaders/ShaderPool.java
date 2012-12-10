package com.hackhalo2.rendering.shaders;

import java.net.URI;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.lwjgl.opengl.GL20;

public class ShaderPool {
	private static Map<String, Shader> aliasMap = new HashMap<String, Shader>();
	private static boolean supported = ShaderUtils.isSupported();
	private static boolean init = false;

	//Prevent initialization
	private ShaderPool() { }
	
	public static void init() {
		System.out.println();
		System.out.println("Initializing the ShaderPool...");
		if(!supported) throw new RuntimeException("Shader Objects are not supported on your hardware");
		
		System.out.println("Supported GLSL Version: "+ShaderUtils.glslVersion);
		System.out.println("Max Draw Buffers: "+ShaderUtils.maxDrawBuffers);
		System.out.println("Max Color Attachments: "+ShaderUtils.maxColorAttachments);
		System.out.println();
	}

	public static Shader getShader(String alias) {
		if(aliasMap.containsKey(alias)) {
			return aliasMap.get(alias);
		} else {
			System.out.println("Alias "+alias+" was not in the AliasMap, returning null");
			return null;
		}
	}

	public static boolean destroy(String alias) {
		if(aliasMap.containsKey(alias)) {
			Shader removed = aliasMap.get(alias);
			Iterator<Entry<String, Shader>> it = aliasMap.entrySet().iterator();
			while(it.hasNext()) {
				Entry<String, Shader> entry = it.next();
				if(entry.getValue().equals(removed)) it.remove();
			}
			return true;
		} else {
			System.err.println("Alias "+alias+" was not in the aliasMap, return false");
			return false;
		}
		
	}
	
	public static boolean create(ShaderObject shadeObj, Map<Integer, String> attributes, String... alias) {
		if(!init) {
			init();
			init = true;
		}
		
		if(alias.length <= 0) {
			System.err.println("You must supply at least one alias for a Shader!");
			return false;
		}

		if(shadeObj == null) {
			System.err.println("The ShaderObject cannot be null!");
			return false;
		}
		
		int program = GL20.glCreateProgram();
		int[] shaders = ShaderUtils.compileShader(shadeObj);
		
		GL20.glAttachShader(program, shaders[0]);
		GL20.glAttachShader(program, shaders[1]);
		
		//TODO: Move this into the ShaderObject's preprocessor
		if(attributes != null)
			for(Integer i : attributes.keySet())
				GL20.glBindAttribLocation(program, i, attributes.get(i));
		
		ShaderUtils.linkProgram(program);
		
		GL20.glDetachShader(program, shaders[0]);
		GL20.glDetachShader(program, shaders[1]);

		GL20.glDeleteShader(shaders[0]);
		GL20.glDeleteShader(shaders[1]);

		Shader shader = new Shader(program);

		for(int i = 0; i < alias.length; i++)
			aliasMap.put(alias[i], shader);

		return true;
		
	}
	
	//Method to convert flatfile shaders into a object
	public static boolean create(URI vertex, URI fragment,
			Map<Integer, String> attributes, String... alias) {
		if(vertex == null || fragment == null) {
			System.err.println("The Vertex or Fragment shader URI's cannot be null!");
			return false;
		}
		
		System.out.println("Converting the shaders to a ShaderObject...");
		ConvertedShader shader = new ConvertedShader(vertex, fragment);
		
		return create(shader, attributes, alias);
	}

}
